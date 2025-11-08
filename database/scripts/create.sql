DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS menu_items CASCADE;
DROP TABLE IF EXISTS menu_categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    student_number VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

COMMENT ON TABLE users IS 'User accounts for IADE GO app (students and staff)';
COMMENT ON COLUMN users.student_number IS 'IADE student identification number';
COMMENT ON COLUMN users.email IS 'Institutional or personal email address';
COMMENT ON COLUMN users.password_hash IS 'Bcrypt hashed password';

CREATE TABLE menu_categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE menu_categories IS 'Menu categories for organizing food items';
COMMENT ON COLUMN menu_categories.display_order IS 'Order for displaying categories in app';

CREATE TABLE menu_items (
    item_id SERIAL PRIMARY KEY,
    category_id INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    preparation_time INT DEFAULT 0,
    calories INT,
    allergens TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_category FOREIGN KEY (category_id) REFERENCES menu_categories(category_id) ON DELETE CASCADE,
    CONSTRAINT check_price_positive CHECK (price >= 0),
    CONSTRAINT check_prep_time CHECK (preparation_time >= 0)
);

COMMENT ON TABLE menu_items IS 'Available menu items in cantina/bar';
COMMENT ON COLUMN menu_items.preparation_time IS 'Estimated preparation time in minutes';
COMMENT ON COLUMN menu_items.allergens IS 'Comma-separated list of allergens';

CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    user_id INT,
    total_price DECIMAL(10,2) NOT NULL,
    item_count INT NOT NULL,
    order_status VARCHAR(20) DEFAULT 'PENDING',
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(20) DEFAULT 'QR_CODE',
    qr_code_data TEXT,
    order_notes TEXT,
    pickup_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT check_total_price CHECK (total_price >= 0),
    CONSTRAINT check_item_count CHECK (item_count > 0),
    CONSTRAINT check_order_status CHECK (order_status IN ('PENDING', 'PAID', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT check_payment_status CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED'))
);

COMMENT ON TABLE orders IS 'Customer orders placed through the app';
COMMENT ON COLUMN orders.order_id IS 'Unique order identifier (format: ORDxxxxxxxxXXXX)';
COMMENT ON COLUMN orders.qr_code_data IS 'JSON data encoded in QR code for payment verification';
COMMENT ON COLUMN orders.payment_status IS 'Separate payment tracking from order fulfillment';

CREATE TABLE order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id VARCHAR(50) NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    special_instructions TEXT,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_item FOREIGN KEY (item_id) REFERENCES menu_items(item_id) ON DELETE CASCADE,
    CONSTRAINT check_quantity CHECK (quantity > 0),
    CONSTRAINT check_unit_price CHECK (unit_price >= 0),
    CONSTRAINT check_subtotal CHECK (subtotal >= 0)
);

COMMENT ON TABLE order_items IS 'Individual items within each order';
COMMENT ON COLUMN order_items.unit_price IS 'Price at time of order (historical record)';
COMMENT ON COLUMN order_items.special_instructions IS 'Customer notes for item preparation';

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_student_number ON users(student_number) WHERE student_number IS NOT NULL;
CREATE INDEX idx_menu_items_category ON menu_items(category_id);
CREATE INDEX idx_menu_items_available ON menu_items(is_available) WHERE is_available = TRUE;
CREATE INDEX idx_menu_items_price ON menu_items(price);
CREATE INDEX idx_orders_user_id ON orders(user_id) WHERE user_id IS NOT NULL;
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX idx_orders_completed_at ON orders(completed_at DESC) WHERE completed_at IS NOT NULL;
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_item_id ON order_items(item_id);

CREATE OR REPLACE VIEW v_active_menu AS
SELECT 
    mi.item_id,
    mi.item_name,
    mi.description,
    mi.price,
    mi.image_url,
    mi.preparation_time,
    mi.calories,
    mi.allergens,
    mc.category_id,
    mc.category_name,
    mc.display_order
FROM menu_items mi
JOIN menu_categories mc ON mi.category_id = mc.category_id
WHERE mi.is_available = TRUE 
  AND mc.is_active = TRUE
ORDER BY mc.display_order, mi.item_name;

COMMENT ON VIEW v_active_menu IS 'Active menu items with category information for app display';

CREATE OR REPLACE VIEW v_order_history AS
SELECT 
    o.order_id,
    o.user_id,
    u.full_name,
    u.student_number,
    o.total_price,
    o.item_count,
    o.order_status,
    o.payment_status,
    o.created_at,
    o.completed_at
FROM orders o
LEFT JOIN users u ON o.user_id = u.user_id
ORDER BY o.created_at DESC;

COMMENT ON VIEW v_order_history IS 'Order history with user details';

CREATE OR REPLACE FUNCTION generate_order_id()
RETURNS VARCHAR(50) AS $$
DECLARE
    new_id VARCHAR(50);
    exists BOOLEAN;
BEGIN
    LOOP
        new_id := 'ORD' || 
                  LPAD(FLOOR(EXTRACT(EPOCH FROM NOW()) * 100)::TEXT, 8, '0') ||
                  UPPER(SUBSTR(MD5(RANDOM()::TEXT), 1, 4));
        SELECT EXISTS(SELECT 1 FROM orders WHERE order_id = new_id) INTO exists;
        EXIT WHEN NOT exists;
    END LOOP;
    RETURN new_id;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION generate_order_id() IS 'Generates unique order ID in format ORDxxxxxxxxXXXX';

CREATE OR REPLACE FUNCTION calculate_order_total(p_order_id VARCHAR(50))
RETURNS DECIMAL(10,2) AS $$
DECLARE
    total DECIMAL(10,2);
BEGIN
    SELECT COALESCE(SUM(subtotal), 0)
    INTO total
    FROM order_items
    WHERE order_id = p_order_id;
    RETURN total;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION calculate_order_total(VARCHAR) IS 'Calculates total price for an order';

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_menu_items_updated_at
    BEFORE UPDATE ON menu_items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON FUNCTION update_updated_at_column() IS 'Automatically updates updated_at timestamp';

INSERT INTO menu_categories (category_name, description, display_order) VALUES
('Snacks', 'Light snacks and quick bites', 1),
('Meals', 'Full meals and sandwiches', 2),
('Drinks', 'Hot and cold beverages', 3),
('Desserts', 'Sweet treats and desserts', 4);

CREATE TABLE IF NOT EXISTS db_metadata (
    key VARCHAR(50) PRIMARY KEY,
    value TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO db_metadata (key, value) VALUES
('version', '1.0'),
('created_date', CURRENT_TIMESTAMP::TEXT),
('description', 'IADE GO Mobile Ordering System Database');

SELECT 'Database schema created successfully!' AS status;
