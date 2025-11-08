DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM menu_items;
DELETE FROM menu_categories WHERE category_id > 4;
DELETE FROM users;

ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE menu_items_item_id_seq RESTART WITH 1;
ALTER SEQUENCE order_items_order_item_id_seq RESTART WITH 1;

INSERT INTO users (student_number, email, full_name, password_hash, created_at) VALUES
('20240001', 'joao.silva@iade.pt', 'João Silva', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-01 09:00:00'),
('20240002', 'maria.santos@iade.pt', 'Maria Santos', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-01 09:15:00'),
('20240003', 'pedro.costa@iade.pt', 'Pedro Costa', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-02 10:30:00'),
('20240004', 'ana.ferreira@iade.pt', 'Ana Ferreira', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-02 11:00:00'),
('20240005', 'tiago.rodrigues@iade.pt', 'Tiago Rodrigues', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-03 08:45:00'),
('20240006', 'sofia.almeida@iade.pt', 'Sofia Almeida', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-03 14:20:00'),
('20240007', 'ricardo.martins@iade.pt', 'Ricardo Martins', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-04 09:30:00'),
(NULL, 'staff.cantina@iade.pt', 'Staff Cantina', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', '2024-09-01 08:00:00');

INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available, preparation_time, calories, allergens) VALUES
(1, 'Cookies', 'Delicious chocolate chip cookies (pack of 3)', 1.50, 'cookies.jpg', TRUE, 0, 450, 'Gluten, Milk, Eggs'),
(1, 'Croissant', 'Fresh butter croissant', 1.80, 'croissant.jpg', TRUE, 2, 230, 'Gluten, Milk'),
(1, 'Brownie', 'Rich chocolate brownie', 2.00, 'brownie.jpg', TRUE, 0, 380, 'Gluten, Milk, Eggs, Nuts'),
(1, 'Torradas', 'Toasted bread with butter and jam', 1.50, 'torradas.jpg', TRUE, 3, 280, 'Gluten, Milk'),
(1, 'Muffin', 'Blueberry muffin', 2.20, 'muffin.jpg', TRUE, 0, 320, 'Gluten, Milk, Eggs'),
(1, 'Donuts', 'Glazed donuts (pack of 2)', 2.50, 'donuts.jpg', TRUE, 0, 520, 'Gluten, Milk, Eggs');

INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available, preparation_time, calories, allergens) VALUES
(2, 'Tosta Mista', 'Grilled ham and cheese sandwich', 2.50, 'tosta_mista.jpg', TRUE, 5, 380, 'Gluten, Milk'),
(2, 'Sandes Mista', 'Ham and cheese sandwich', 3.00, 'sandes_mista.jpg', TRUE, 3, 420, 'Gluten, Milk'),
(2, 'Prego', 'Portuguese steak sandwich', 4.50, 'prego.jpg', TRUE, 8, 580, 'Gluten'),
(2, 'Hamburguer', 'Classic beef hamburger with fries', 4.00, 'hamburguer.jpg', TRUE, 10, 650, 'Gluten, Milk, Eggs'),
(2, 'Pizza Slice', 'Margherita pizza slice', 3.50, 'pizza.jpg', TRUE, 4, 380, 'Gluten, Milk'),
(2, 'Sandes de Frango', 'Grilled chicken sandwich', 3.80, 'sandes_frango.jpg', TRUE, 7, 420, 'Gluten'),
(2, 'Tosta de Queijo', 'Grilled cheese sandwich', 2.00, 'tosta_queijo.jpg', TRUE, 4, 350, 'Gluten, Milk');

INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available, preparation_time, calories, allergens) VALUES
(3, 'Café', 'Espresso coffee', 0.70, 'cafe.jpg', TRUE, 2, 5, NULL),
(3, 'Galão', 'Portuguese latte', 1.20, 'galao.jpg', TRUE, 3, 120, 'Milk'),
(3, 'Cappuccino', 'Italian cappuccino', 1.50, 'cappuccino.jpg', TRUE, 3, 140, 'Milk'),
(3, 'Sumo Natural', 'Fresh squeezed orange juice', 2.50, 'sumo.jpg', TRUE, 4, 180, NULL),
(3, 'Água', 'Mineral water 500ml', 1.00, 'agua.jpg', TRUE, 0, 0, NULL),
(3, 'Coca-Cola', 'Coca-Cola 330ml', 1.50, 'coca.jpg', TRUE, 0, 139, NULL),
(3, 'Ice Tea', 'Lipton Ice Tea Lemon', 1.50, 'icetea.jpg', TRUE, 0, 105, NULL),
(3, 'Chá', 'Hot tea (various flavors)', 1.00, 'cha.jpg', TRUE, 3, 2, NULL),
(3, 'Sumol', 'Sumol Orange 330ml', 1.50, 'sumol.jpg', TRUE, 0, 135, NULL);

INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available, preparation_time, calories, allergens) VALUES
(4, 'Pastel de Nata', 'Traditional Portuguese custard tart', 1.30, 'pastelnata.jpg', TRUE, 0, 220, 'Gluten, Milk, Eggs'),
(4, 'Bolo de Chocolate', 'Chocolate cake slice', 2.50, 'bolo.jpg', TRUE, 0, 380, 'Gluten, Milk, Eggs'),
(4, 'Iogurte', 'Greek yogurt with honey', 1.80, 'iogurte.jpg', TRUE, 0, 150, 'Milk'),
(4, 'Mousse de Chocolate', 'Chocolate mousse', 2.20, 'mousse.jpg', TRUE, 0, 320, 'Milk, Eggs'),
(4, 'Salada de Fruta', 'Fresh fruit salad', 2.80, 'salada_fruta.jpg', TRUE, 5, 120, NULL);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at, completed_at) VALUES
('ORD1736950800ABCD', 1, 5.50, 3, 'COMPLETED', 'PAID', 
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1736950800ABCD","user_id":"1","student_number":"20240001","total_price":5.50,"item_count":3,"timestamp":"2025-01-15 08:30:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-15 08:30:00', '2025-01-15 08:45:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1736950800ABCD', 2, 1, 1.80, 1.80),
('ORD1736950800ABCD', 15, 1, 0.70, 0.70),
('ORD1736950800ABCD', 22, 1, 1.30, 1.30);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at, completed_at) VALUES
('ORD1737037200EFGH', 2, 11.00, 4, 'COMPLETED', 'PAID',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737037200EFGH","user_id":"2","student_number":"20240002","total_price":11.00,"item_count":4,"timestamp":"2025-01-15 12:00:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-15 12:00:00', '2025-01-15 12:20:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737037200EFGH', 9, 1, 4.50, 4.50),
('ORD1737037200EFGH', 16, 1, 1.20, 1.20),
('ORD1737037200EFGH', 19, 1, 2.50, 2.50),
('ORD1737037200EFGH', 23, 1, 2.50, 2.50);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at, completed_at) VALUES
('ORD1737048000IJKL', 3, 8.30, 5, 'COMPLETED', 'PAID',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737048000IJKL","user_id":"3","student_number":"20240003","total_price":8.30,"item_count":5,"timestamp":"2025-01-15 15:00:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-15 15:00:00', '2025-01-15 15:10:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737048000IJKL', 1, 2, 1.50, 3.00),
('ORD1737048000IJKL', 4, 1, 2.00, 2.00),
('ORD1737048000IJKL', 21, 2, 1.50, 3.00);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at, pickup_time) VALUES
('ORD1737123600MNOP', 4, 9.80, 4, 'READY', 'PAID',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737123600MNOP","user_id":"4","student_number":"20240004","total_price":9.80,"item_count":4,"timestamp":"2025-01-16 12:00:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-16 12:00:00', '2025-01-16 12:15:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737123600MNOP', 7, 1, 2.50, 2.50),
('ORD1737123600MNOP', 8, 1, 3.00, 3.00),
('ORD1737123600MNOP', 16, 1, 1.20, 1.20),
('ORD1737123600MNOP', 26, 1, 2.80, 2.80);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at) VALUES
('ORD1737127200QRST', 5, 18.50, 7, 'PREPARING', 'PAID',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737127200QRST","user_id":"5","student_number":"20240005","total_price":18.50,"item_count":7,"timestamp":"2025-01-16 13:00:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-16 13:00:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737127200QRST', 10, 1, 4.00, 4.00),
('ORD1737127200QRST', 11, 2, 3.50, 7.00),
('ORD1737127200QRST', 21, 3, 1.50, 4.50),
('ORD1737127200QRST', 22, 2, 1.30, 2.60);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at) VALUES
('ORD1737187200UVWX', 6, 6.70, 4, 'PENDING', 'PENDING',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737187200UVWX","user_id":"6","student_number":"20240006","total_price":6.70,"item_count":4,"timestamp":"2025-01-17 09:00:00","payment_method":"QR_CODE","status":"PENDING"}',
'2025-01-17 09:00:00');

INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737187200UVWX', 2, 1, 1.80, 1.80),
('ORD1737187200UVWX', 4, 1, 2.00, 2.00),
('ORD1737187200UVWX', 15, 1, 0.70, 0.70),
('ORD1737187200UVWX', 24, 1, 1.80, 1.80);

INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at, completed_at) VALUES
('ORD1737198000YZAB', 7, 4.20, 3, 'COMPLETED', 'PAID',
'{"type":"IADE_GO_PAYMENT","order_id":"ORD1737198000YZAB","user_id":"7","student_number":"20240007","total_price":4.20,"item_count":3,"timestamp":"2025-01-17 12:00:00","payment_method":"QR_CODE","status":"PAID"}',
'2025-01-17 12:00:00', '2025-01-17 12:08:00');
