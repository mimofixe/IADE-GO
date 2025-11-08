INSERT INTO users (student_number, email, full_name, password_hash, created_at) VALUES
('20240001', 'joao.silva@iade.pt', 'João Silva', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-01 09:00:00'),
('20240002', 'maria.santos@iade.pt', 'Maria Santos', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-01 09:15:00');

-- MENU ITEMS (Based on your app)

-- SNACKS (Category 1)
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(1, 'Cookies', 'Chocolate chip cookies (3 units)', 1.50, 'cookies.jpg', TRUE),
(1, 'Croissant', 'Fresh butter croissant', 1.80, 'croissant.jpg', TRUE),
(1, 'Brownie', 'Rich chocolate brownie', 2.00, 'brownie.jpg', TRUE),
(1, 'Torradas', 'Toast with butter and jam', 1.50, 'torradas.jpg', TRUE);

-- MEALS (Category 2)
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(2, 'Tosta Mista', 'Grilled ham and cheese sandwich', 2.50, 'tosta_mista.jpg', TRUE),
(2, 'Sandes Mista', 'Ham and cheese sandwich', 3.00, 'sandes_mista.jpg', TRUE);

-- DRINKS (Category 3)
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(3, 'Café', 'Espresso coffee', 0.70, 'cafe.jpg', TRUE),
(3, 'Galão', 'Portuguese latte', 1.20, 'galao.jpg', TRUE),
(3, 'Água', 'Mineral water 500ml', 1.00, 'agua.jpg', TRUE),
(3, 'Coca-Cola', 'Coca-Cola 330ml', 1.50, 'coca.jpg', TRUE);

-- DESSERTS (Category 4)
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(4, 'Pastel de Nata', 'Portuguese custard tart', 1.30, 'pastelnata.jpg', TRUE),
(4, 'Bolo de Chocolate', 'Chocolate cake slice', 2.50, 'bolo.jpg', TRUE);

-- SAMPLE ORDERS (2 orders)

-- Order 1: João Silva (COMPLETED)
INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at) VALUES
('ORD1736950800ABCD', 1, 5.50, 3, 'COMPLETED', 'PAID', 
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1736950800ABCD","user_id":"1","student_number":"20240001","total_price":5.50,"item_count":3,"timestamp":"2025-01-15 08:30:00"}',
'2025-01-15 08:30:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1736950800ABCD', 2, 1, 1.80, 1.80),  -- Croissant
('ORD1736950800ABCD', 7, 1, 0.70, 0.70),  -- Café
('ORD1736950800ABCD', 11, 1, 1.30, 1.30); -- Pastel de Nata

-- Order 2: Maria Santos (PENDING)
INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at) VALUES
('ORD1737037200EFGH', 2, 7.00, 3, 'PENDING', 'PENDING',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737037200EFGH","user_id":"2","student_number":"20240002","total_price":7.00,"item_count":3,"timestamp":"2025-01-15 12:00:00"}',
'2025-01-15 12:00:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737037200EFGH', 5, 1, 2.50, 2.50),  -- Tosta Mista
('ORD1737037200EFGH', 8, 1, 1.20, 1.20),  -- Galão
('ORD1737037200EFGH', 12, 1, 2.50, 2.50); -- Bolo de Chocolate

-- Verification

SELECT 'Data populated successfully!' AS status;

SELECT 'Users' AS table_name, COUNT(*) AS count FROM users
UNION ALL
SELECT 'Menu Items', COUNT(*) FROM menu_items
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders;
