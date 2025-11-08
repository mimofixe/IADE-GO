-- Query 1: List all users
SELECT 
    user_id,
    student_number,
    full_name,
    email,
    TO_CHAR(created_at, 'DD/MM/YYYY') as registration_date
FROM users;

-- Query 2: List all menu items with categories
SELECT 
    mc.category_name,
    mi.item_name,
    mi.description,
    CONCAT('€', mi.price) as price,
    mi.is_available
FROM menu_items mi
JOIN menu_categories mc ON mi.category_id = mc.category_id
ORDER BY mc.display_order, mi.item_name;

-- Query 3: User order history
SELECT 
    u.full_name,
    o.order_id,
    TO_CHAR(o.created_at, 'DD/MM/YYYY HH24:MI') as order_time,
    o.item_count,
    CONCAT('€', o.total_price) as total,
    o.order_status,
    o.payment_status
FROM orders o
JOIN users u ON o.user_id = u.user_id
ORDER BY o.created_at DESC;

-- Query 4: Order details with items
SELECT 
    o.order_id,
    u.full_name,
    mi.item_name,
    oi.quantity,
    CONCAT('€', oi.unit_price) as unit_price,
    CONCAT('€', oi.subtotal) as subtotal
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN menu_items mi ON oi.item_id = mi.item_id
WHERE o.order_id = 'ORD1736950800ABCD';

-- Query 5: Most popular items
SELECT 
    mi.item_name,
    COUNT(oi.order_item_id) as times_ordered,
    SUM(oi.quantity) as total_quantity,
    CONCAT('€', ROUND(SUM(oi.subtotal)::NUMERIC, 2)) as total_revenue
FROM menu_items mi
LEFT JOIN order_items oi ON mi.item_id = oi.item_id
LEFT JOIN orders o ON oi.order_id = o.order_id AND o.payment_status = 'PAID'
GROUP BY mi.item_id, mi.item_name
ORDER BY total_quantity DESC NULLS LAST;

-- Query 6: Revenue by category
SELECT 
    mc.category_name,
    COUNT(DISTINCT o.order_id) as orders,
    SUM(oi.quantity) as items_sold,
    CONCAT('€', ROUND(SUM(oi.subtotal)::NUMERIC, 2)) as revenue
FROM menu_categories mc
JOIN menu_items mi ON mc.category_id = mi.category_id
LEFT JOIN order_items oi ON mi.item_id = oi.item_id
LEFT JOIN orders o ON oi.order_id = o.order_id AND o.payment_status = 'PAID'
GROUP BY mc.category_id, mc.category_name
ORDER BY revenue DESC NULLS LAST;

-- Query 7: Customer lifetime value
SELECT 
    u.full_name,
    u.student_number,
    COUNT(o.order_id) as total_orders,
    CONCAT('€', COALESCE(ROUND(SUM(o.total_price)::NUMERIC, 2), 0)) as lifetime_value
FROM users u
LEFT JOIN orders o ON u.user_id = o.user_id AND o.payment_status = 'PAID'
GROUP BY u.user_id, u.full_name, u.student_number
ORDER BY SUM(o.total_price) DESC NULLS LAST;

-- Query 8: Today's revenue
SELECT 
    COUNT(*) as orders_today,
    SUM(item_count) as items_sold,
    CONCAT('€', ROUND(SUM(total_price)::NUMERIC, 2)) as revenue_today
FROM orders
WHERE DATE(created_at) = CURRENT_DATE
  AND payment_status = 'PAID';
