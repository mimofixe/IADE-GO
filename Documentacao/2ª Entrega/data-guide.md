# 📊 Guia de Dados - IADE GO

**Versão:** 1.0  
**Data:** Novembro 2025  
**Base de Dados:** iade_go  
**Propósito:** Documentar a estrutura e dados de exemplo para desenvolvimento e testes

---

## 📑 Índice

1. [Visão Geral](#1-visão-geral)
2. [Dados de Exemplo - Users](#2-dados-de-exemplo---users)
3. [Dados de Exemplo - Menu Categories](#3-dados-de-exemplo---menu-categories)
4. [Dados de Exemplo - Menu Items](#4-dados-de-exemplo---menu-items)
5. [Dados de Exemplo - Orders](#5-dados-de-exemplo---orders)
6. [Dados de Exemplo - Order Items](#6-dados-de-exemplo---order-items)
7. [Queries Úteis](#7-queries-úteis)
8. [Cenários de Teste](#8-cenários-de-teste)

---

## 1. Visão Geral

### **Estrutura da Base de Dados**
```
iade_go/
├── users (2 registos)
├── menu_categories (4 registos)
├── menu_items (12 registos)
├── orders (1 registo)
└── order_items (3 registos)
```

### **Total de Registos:** 22

### **Propósito dos Dados:**
- Ambiente de desenvolvimento e testes
- Demonstração de funcionalidades
- Validação de queries e endpoints REST
- Base para testes automatizados

---

## 2. Dados de Exemplo - Users

### **Total:** 2 utilizadores

| user_id | student_number | email | full_name | password | created_at |
|---------|----------------|-------|-----------|----------|------------|
| 1 | 20240001 | joao.silva@iade.pt | João Silva | password123 | 2024-09-01 09:00:00 |
| 2 | 20240002 | maria.santos@iade.pt | Maria Santos | password123 | 2024-09-02 10:00:00 |

### **SQL de Inserção:**
```sql
INSERT INTO users (student_number, email, full_name, password_hash, created_at) VALUES
('20240001', 'joao.silva@iade.pt', 'João Silva', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 
 '2024-09-01 09:00:00'),
('20240002', 'maria.santos@iade.pt', 'Maria Santos', 
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 
 '2024-09-02 10:00:00');
```

### **Credenciais de Teste:**

**User 1:**
- Email: `joao.silva@iade.pt`
- Password: `password123`
- Student Number: `20240001`

**User 2:**
- Email: `maria.santos@iade.pt`
- Password: `password123`
- Student Number: `20240002`

### **Notas:**
- Password hash é BCrypt de "password123"
- Ambos os users são estudantes ativos
- Podem ser usados para testes de autenticação

---

## 3. Dados de Exemplo - Menu Categories

### **Total:** 4 categorias

| category_id | category_name | description | display_order |
|-------------|---------------|-------------|---------------|
| 1 | Snacks | Light snacks and quick bites | 1 |
| 2 | Meals | Full meals and sandwiches | 2 |
| 3 | Beverages | Hot and cold drinks | 3 |
| 4 | Desserts | Sweet treats and desserts | 4 |

### **SQL de Inserção:**
```sql
INSERT INTO menu_categories (category_name, description, display_order) VALUES
('Snacks', 'Light snacks and quick bites', 1),
('Meals', 'Full meals and sandwiches', 2),
('Beverages', 'Hot and cold drinks', 3),
('Desserts', 'Sweet treats and desserts', 4);
```

### **Distribuição de Produtos:**
- Snacks: 3 produtos
- Meals: 3 produtos
- Beverages: 4 produtos
- Desserts: 2 produtos

### **Ordem de Apresentação:**
As categorias aparecem pela ordem do campo `display_order` (1, 2, 3, 4).

---

## 4. Dados de Exemplo - Menu Items

### **Total:** 12 produtos

### **Categoria 1: Snacks (3 produtos)**

| item_id | item_name | description | price | image_url | is_available |
|---------|-----------|-------------|-------|-----------|--------------|
| 1 | Cookies | Chocolate chip cookies (3 units) | 1.50 | cookies.jpg | TRUE |
| 2 | Croissant | Butter croissant | 1.80 | croissant.jpg | TRUE |
| 3 | Muffin | Blueberry muffin | 2.00 | muffin.jpg | TRUE |

---

### **Categoria 2: Meals (3 produtos)**

| item_id | item_name | description | price | image_url | is_available |
|---------|-----------|-------------|-------|-----------|--------------|
| 4 | Tosta Mista | Grilled ham and cheese sandwich | 2.50 | tosta_mista.jpg | TRUE |
| 5 | Wrap de Frango | Chicken wrap with vegetables | 3.50 | wrap_frango.jpg | TRUE |
| 6 | Salada Caesar | Caesar salad with chicken | 4.50 | salada_caesar.jpg | TRUE |

---

### **Categoria 3: Beverages (4 produtos)**

| item_id | item_name | description | price | image_url | is_available |
|---------|-----------|-------------|-------|-----------|--------------|
| 7 | Café | Espresso coffee | 0.70 | cafe.jpg | TRUE |
| 8 | Cappuccino | Cappuccino coffee | 1.50 | cappuccino.jpg | TRUE |
| 9 | Sumo Natural | Fresh orange juice | 2.00 | sumo_laranja.jpg | TRUE |
| 10 | Água | Mineral water 500ml | 0.80 | agua.jpg | TRUE |

---

### **Categoria 4: Desserts (2 produtos)**

| item_id | item_name | description | price | image_url | is_available |
|---------|-----------|-------------|-------|-----------|--------------|
| 11 | Pastel de Nata | Traditional Portuguese custard tart | 1.30 | pastel_nata.jpg | TRUE |
| 12 | Brownie | Chocolate brownie | 2.20 | brownie.jpg | TRUE |

---

### **SQL de Inserção Completo:**
```sql
-- Snacks (category_id = 1)
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(1, 'Cookies', 'Chocolate chip cookies (3 units)', 1.50, 'cookies.jpg', TRUE),
(1, 'Croissant', 'Butter croissant', 1.80, 'croissant.jpg', TRUE),
(1, 'Muffin', 'Blueberry muffin', 2.00, 'muffin.jpg', TRUE),

-- Meals (category_id = 2)
(2, 'Tosta Mista', 'Grilled ham and cheese sandwich', 2.50, 'tosta_mista.jpg', TRUE),
(2, 'Wrap de Frango', 'Chicken wrap with vegetables', 3.50, 'wrap_frango.jpg', TRUE),
(2, 'Salada Caesar', 'Caesar salad with chicken', 4.50, 'salada_caesar.jpg', TRUE),

-- Beverages (category_id = 3)
(3, 'Café', 'Espresso coffee', 0.70, 'cafe.jpg', TRUE),
(3, 'Cappuccino', 'Cappuccino coffee', 1.50, 'cappuccino.jpg', TRUE),
(3, 'Sumo Natural', 'Fresh orange juice', 2.00, 'sumo_laranja.jpg', TRUE),
(3, 'Água', 'Mineral water 500ml', 0.80, 'agua.jpg', TRUE),

-- Desserts (category_id = 4)
(4, 'Pastel de Nata', 'Traditional Portuguese custard tart', 1.30, 'pastel_nata.jpg', TRUE),
(4, 'Brownie', 'Chocolate brownie', 2.20, 'brownie.jpg', TRUE);
```

### **Estatísticas de Preços:**

| Categoria | Preço Mínimo | Preço Máximo | Preço Médio |
|-----------|--------------|--------------|-------------|
| Snacks | €1.50 | €2.00 | €1.77 |
| Meals | €2.50 | €4.50 | €3.50 |
| Beverages | €0.70 | €2.00 | €1.25 |
| Desserts | €1.30 | €2.20 | €1.75 |

**Geral:** Preço médio de todos os produtos: **€2.02**

---

## 5. Dados de Exemplo - Orders

### **Total:** 1 encomenda de exemplo

| order_id | user_id | total_price | item_count | order_status | payment_status | created_at |
|----------|---------|-------------|------------|--------------|----------------|------------|
| ORD1736950800ABCD | 1 | 5.50 | 3 | COMPLETED | PAID | 2025-01-15 08:30:00 |

### **SQL de Inserção:**
```sql
INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data, created_at) VALUES
('ORD1736950800ABCD', 1, 5.50, 3, 'COMPLETED', 'PAID',
 '{"type":"IADE_GO_PAYMENT","order_id":"ORD1736950800ABCD","user_id":1,"student_number":"20240001","total_price":5.50,"item_count":3,"timestamp":"2025-01-15T08:30:00"}',
 '2025-01-15 08:30:00');
```

### **Detalhes da Encomenda:**

**Encomenda:** ORD1736950800ABCD
- **Cliente:** João Silva (user_id: 1)
- **Data:** 15 de Janeiro de 2025, 08:30
- **Total:** €5.50
- **Itens:** 3 produtos
- **Estado:** COMPLETED (encomenda concluída)
- **Pagamento:** PAID (pago)

### **QR Code Data:**
```json
{
  "type": "IADE_GO_PAYMENT",
  "order_id": "ORD1736950800ABCD",
  "user_id": 1,
  "student_number": "20240001",
  "total_price": 5.50,
  "item_count": 3,
  "timestamp": "2025-01-15T08:30:00"
}
```

---

## 6. Dados de Exemplo - Order Items

### **Total:** 3 itens (da encomenda ORD1736950800ABCD)

| order_item_id | order_id | item_id | item_name | quantity | unit_price | subtotal |
|---------------|----------|---------|-----------|----------|------------|----------|
| 1 | ORD1736950800ABCD | 2 | Croissant | 1 | 1.80 | 1.80 |
| 2 | ORD1736950800ABCD | 7 | Café | 2 | 0.70 | 1.40 |
| 3 | ORD1736950800ABCD | 11 | Pastel de Nata | 2 | 1.30 | 2.60 |

### **SQL de Inserção:**
```sql
INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1736950800ABCD', 2, 1, 1.80, 1.80),   -- 1x Croissant
('ORD1736950800ABCD', 7, 2, 0.70, 1.40),   -- 2x Café
('ORD1736950800ABCD', 11, 2, 1.30, 2.60);  -- 2x Pastel de Nata
```

### **Resumo da Encomenda:**
```
Encomenda: ORD1736950800ABCD
Cliente: João Silva (20240001)
Data: 15/01/2025 08:30

─────────────────────────────────────
ITEM                QTY  PREÇO  TOTAL
─────────────────────────────────────
Croissant            1   €1.80  €1.80
Café                 2   €0.70  €1.40
Pastel de Nata       2   €1.30  €2.60
─────────────────────────────────────
TOTAL                3          €5.50
═════════════════════════════════════
Estado: COMPLETED
Pagamento: PAID
```

### **Cálculo do Total:**
```
1.80 (Croissant) + 1.40 (2x Café) + 2.60 (2x Pastel) = 5.50 €
```

---

## 7. Queries Úteis

### **7.1 Ver Todos os Dados**
```sql
-- Ver todos os users
SELECT * FROM users;

-- Ver todas as categorias
SELECT * FROM menu_categories ORDER BY display_order;

-- Ver todos os produtos
SELECT * FROM menu_items ORDER BY category_id, item_name;

-- Ver todas as encomendas
SELECT * FROM orders ORDER BY created_at DESC;

-- Ver todos os order items
SELECT * FROM order_items;
```

---

### **7.2 Queries Relacionais**

**Produtos por Categoria:**
```sql
SELECT 
    c.category_name,
    m.item_name,
    m.price,
    m.is_available
FROM menu_items m
JOIN menu_categories c ON m.category_id = c.category_id
ORDER BY c.display_order, m.item_name;
```

**Resultado:**
```
category_name | item_name      | price | is_available
--------------|----------------|-------|-------------
Snacks        | Cookies        | 1.50  | TRUE
Snacks        | Croissant      | 1.80  | TRUE
Snacks        | Muffin         | 2.00  | TRUE
Meals         | Salada Caesar  | 4.50  | TRUE
...
```

---

**Encomendas com Detalhes do User:**
```sql
SELECT 
    o.order_id,
    u.full_name,
    u.student_number,
    o.total_price,
    o.item_count,
    o.order_status,
    o.created_at
FROM orders o
JOIN users u ON o.user_id = u.user_id
ORDER BY o.created_at DESC;
```

**Resultado:**
```
order_id             | full_name   | student_number | total_price | item_count | order_status | created_at
---------------------|-------------|----------------|-------------|------------|--------------|--------------------
ORD1736950800ABCD    | João Silva  | 20240001       | 5.50        | 3          | COMPLETED    | 2025-01-15 08:30:00
```

---

**Detalhes Completos de uma Encomenda:**
```sql
SELECT 
    o.order_id,
    o.created_at,
    u.full_name,
    mi.item_name,
    oi.quantity,
    oi.unit_price,
    oi.subtotal
FROM orders o
JOIN users u ON o.user_id = u.user_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN menu_items mi ON oi.item_id = mi.item_id
WHERE o.order_id = 'ORD1736950800ABCD';
```

**Resultado:**
```
order_id             | created_at          | full_name   | item_name        | quantity | unit_price | subtotal
---------------------|---------------------|-------------|------------------|----------|------------|----------
ORD1736950800ABCD    | 2025-01-15 08:30:00 | João Silva  | Croissant        | 1        | 1.80       | 1.80
ORD1736950800ABCD    | 2025-01-15 08:30:00 | João Silva  | Café             | 2        | 0.70       | 1.40
ORD1736950800ABCD    | 2025-01-15 08:30:00 | João Silva  | Pastel de Nata   | 2        | 1.30       | 2.60
```

---

### **7.3 Queries de Análise**

**Top 5 Produtos Mais Caros:**
```sql
SELECT 
    item_name, 
    price, 
    category_id
FROM menu_items
ORDER BY price DESC
LIMIT 5;
```

**Resultado:**
```
item_name        | price | category_id
-----------------|-------|------------
Salada Caesar    | 4.50  | 2
Wrap de Frango   | 3.50  | 2
Tosta Mista      | 2.50  | 2
Brownie          | 2.20  | 4
Muffin           | 2.00  | 1
```

---

**Produtos por Faixa de Preço:**
```sql
SELECT 
    CASE 
        WHEN price < 1.00 THEN '< €1.00'
        WHEN price BETWEEN 1.00 AND 2.00 THEN '€1.00 - €2.00'
        WHEN price BETWEEN 2.01 AND 3.00 THEN '€2.01 - €3.00'
        ELSE '> €3.00'
    END AS price_range,
    COUNT(*) as product_count
FROM menu_items
GROUP BY price_range
ORDER BY MIN(price);
```

**Resultado:**
```
price_range      | product_count
-----------------|---------------
< €1.00          | 2
€1.00 - €2.00    | 6
€2.01 - €3.00    | 2
> €3.00          | 2
```

---

**Histórico de Encomendas por User:**
```sql
SELECT 
    u.full_name,
    COUNT(o.order_id) as total_orders,
    SUM(o.total_price) as total_spent,
    AVG(o.total_price) as avg_order_value,
    MAX(o.created_at) as last_order
FROM users u
LEFT JOIN orders o ON u.user_id = o.user_id
GROUP BY u.user_id, u.full_name;
```

**Resultado:**
```
full_name     | total_orders | total_spent | avg_order_value | last_order
--------------|--------------|-------------|-----------------|--------------------
João Silva    | 1            | 5.50        | 5.50            | 2025-01-15 08:30:00
Maria Santos  | 0            | NULL        | NULL            | NULL
```

---

**Produtos Mais Vendidos:**
```sql
SELECT 
    mi.item_name,
    SUM(oi.quantity) as times_ordered,
    SUM(oi.subtotal) as total_revenue
FROM menu_items mi
LEFT JOIN order_items oi ON mi.item_id = oi.item_id
GROUP BY mi.item_id, mi.item_name
ORDER BY times_ordered DESC;
```

**Resultado:**
```
item_name        | times_ordered | total_revenue
-----------------|---------------|---------------
Café             | 2             | 1.40
Pastel de Nata   | 2             | 2.60
Croissant        | 1             | 1.80
Cookies          | 0             | NULL
Muffin           | 0             | NULL
...
```

---

## 8. Cenários de Teste

### **Cenário 1: Login e Ver Menu**

**Passo 1:** Login
```sql
SELECT * FROM users WHERE email = 'joao.silva@iade.pt';
```

**Passo 2:** Ver categorias
```sql
SELECT * FROM menu_categories ORDER BY display_order;
```

**Passo 3:** Ver produtos de uma categoria
```sql
SELECT * FROM menu_items WHERE category_id = 1;
```

**Resultado Esperado:** 3 produtos (Cookies, Croissant, Muffin)

---

### **Cenário 2: Criar Nova Encomenda**

**Dados de Entrada:**
- User: João Silva (user_id: 1)
- Produtos:
  - 2x Cookies (€1.50 cada)
  - 1x Café (€0.70)

**SQL:**
```sql
-- Criar order
INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, created_at)
VALUES ('ORD1737400000TEST', 1, 3.70, 3, 'PENDING', 'PAID', NOW());

-- Adicionar items
INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737400000TEST', 1, 2, 1.50, 3.00),  -- 2x Cookies
('ORD1737400000TEST', 7, 1, 0.70, 0.70);  -- 1x Café
```

**Verificação:**
```sql
SELECT 
    o.order_id,
    o.total_price,
    mi.item_name,
    oi.quantity,
    oi.subtotal
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
JOIN menu_items mi ON oi.item_id = mi.item_id
WHERE o.order_id = 'ORD1737400000TEST';
```

**Resultado Esperado:**
```
order_id            | total_price | item_name | quantity | subtotal
--------------------|-------------|-----------|----------|----------
ORD1737400000TEST   | 3.70        | Cookies   | 2        | 3.00
ORD1737400000TEST   | 3.70        | Café      | 1        | 0.70
```

---

### **Cenário 3: Ver Histórico de Encomendas**

**Query:**
```sql
SELECT 
    o.order_id,
    o.created_at,
    o.total_price,
    o.order_status,
    COUNT(oi.order_item_id) as num_items
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
WHERE o.user_id = 1
GROUP BY o.order_id
ORDER BY o.created_at DESC;
```

**Resultado Esperado:**
```
order_id            | created_at          | total_price | order_status | num_items
--------------------|---------------------|-------------|--------------|----------
ORD1737400000TEST   | 2025-01-19 12:00:00 | 3.70        | PENDING      | 2
ORD1736950800ABCD   | 2025-01-15 08:30:00 | 5.50        | COMPLETED    | 3
```

---

### **Cenário 4: Atualizar Status da Encomenda**

**Fluxo:**
1. PENDING → PREPARING
2. PREPARING → READY
3. READY → COMPLETED

**SQL:**
```sql
-- Mudar para PREPARING
UPDATE orders 
SET order_status = 'PREPARING' 
WHERE order_id = 'ORD1737400000TEST';

-- Verificar
SELECT order_id, order_status FROM orders WHERE order_id = 'ORD1737400000TEST';
```

**Resultado:**
```
order_id            | order_status
--------------------|-------------
ORD1737400000TEST   | PREPARING
```

---

### **Cenário 5: Produtos Indisponíveis**

**Marcar produto como indisponível:**
```sql
UPDATE menu_items 
SET is_available = FALSE 
WHERE item_id = 1;
```

**Ver apenas produtos disponíveis:**
```sql
SELECT item_name, price 
FROM menu_items 
WHERE is_available = TRUE
ORDER BY item_name;
```

**Resultado:** Lista sem "Cookies"

---

## 9. Scripts de Reset

### **Reset Completo da Base de Dados:**
```sql
-- Apagar todos os dados (manter estrutura)
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM menu_items;
DELETE FROM menu_categories;
DELETE FROM users;

-- Reset AUTO_INCREMENT
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE menu_categories AUTO_INCREMENT = 1;
ALTER TABLE menu_items AUTO_INCREMENT = 1;
ALTER TABLE order_items AUTO_INCREMENT = 1;

-- Reinserir dados de exemplo
-- (copiar inserts das secções anteriores)
```

---

### **Backup Rápido:**
```sql
-- Criar backup
mysqldump -u root iade_go > backup_iade_go.sql

-- Restaurar backup
mysql -u root iade_go < backup_iade_go.sql
```

---

## 10. Estatísticas Finais

### **Resumo dos Dados:**

| Tabela | Registos | Tamanho Estimado |
|--------|----------|------------------|
| users | 2 | ~1 KB |
| menu_categories | 4 | < 1 KB |
| menu_items | 12 | ~5 KB |
| orders | 1 | ~1 KB |
| order_items | 3 | ~1 KB |
| **TOTAL** | **22** | **~8 KB** |

---

### **Produtos por Categoria:**
```
Snacks:     ███ 3 produtos (25%)
Meals:      ███ 3 produtos (25%)
Beverages:  ████ 4 produtos (33%)
Desserts:   ██ 2 produtos (17%)
```

---

### **Distribuição de Preços:**
```
€0.00 - €1.00:  ██ 2 produtos (17%)
€1.01 - €2.00:  ██████ 6 produtos (50%)
€2.01 - €3.00:  ██ 2 produtos (17%)
€3.01 - €5.00:  ██ 2 produtos (17%)
```

---

**Fim do Guia de Dados**
```

---

## **✅ FICHEIROS CRIADOS PARA A ENTREGA:**

Tens agora **4 documentos completos:**

1. ✅ **`class-diagram.md`** - Diagrama de Classes Conceptual
2. ✅ **`rest-api-complete.md`** - Documentação REST API Completa
3. ✅ **`data-dictionary.md`** - Dicionário de Dados Detalhado
4. ✅ **`data-guide.md`** - Guia de Dados com Exemplos

---

## **📁 Organização Sugerida para Entrega:**
```
Relatório_Intermédio/
├── 01_Introdução.md
├── 02_Arquitetura_Sistema.md
├── 03_Modelo_Dados/
│   ├── ER_Diagram.png
│   ├── class-diagram.md
│   ├── data-dictionary.md
│   └── data-guide.md
├── 04_REST_API/
│   └── rest-api-complete.md
├── 05_Backend/
│   ├── Tecnologias.md
│   └── Estrutura_Código.md
├── 06_App_Android/
│   ├── UI_Screens.md
│   └── Funcionalidades.md
└── 07_Conclusão.md
