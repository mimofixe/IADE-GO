# Dicionário de Dados - IADE GO

**Versão:** 1.0  
**Data:** Janeiro 2025  
**Base de Dados:** MySQL 8.0  
**Charset:** UTF-8  
**Collation:** utf8mb4_unicode_ci

---

## Índice de Tabelas

1. [users](#1-users---utilizadores)
2. [menu_categories](#2-menu_categories---categorias-de-menu)
3. [menu_items](#3-menu_items---produtos-do-menu)
4. [orders](#4-orders---encomendas)
5. [order_items](#5-order_items---itens-da-encomenda)
6. [Relações entre Tabelas](#6-relações-entre-tabelas)
7. [Índices](#7-índices)
8. [Constraints](#8-constraints)

---

## 1. users - Utilizadores

**Descrição:** Armazena informação dos utilizadores do sistema (estudantes e staff).

**Nome da Tabela:** `users`

| Campo | Tipo | Null | Key | Default | Descrição |
|-------|------|------|-----|---------|-----------|
| `user_id` | INT | NO | PK | AUTO_INCREMENT | Identificador único do utilizador |
| `student_number` | VARCHAR(20) | YES | UNI | NULL | Número de estudante IADE (único) |
| `email` | VARCHAR(100) | NO | UNI | - | Email institucional (único, obrigatório) |
| `full_name` | VARCHAR(100) | NO | - | - | Nome completo do utilizador |
| `password_hash` | VARCHAR(255) | NO | - | - | Password encriptada (bcrypt) |
| `created_at` | TIMESTAMP | NO | - | CURRENT_TIMESTAMP | Data/hora de criação da conta |

### **Constraints:**
- **Primary Key:** `user_id`
- **Unique:** `email`, `student_number`
- **Not Null:** `email`, `full_name`, `password_hash`

### **Índices:**
- `PRIMARY KEY` em `user_id`
- `UNIQUE` em `email`
- `UNIQUE` em `student_number`

### **Relações:**
- **1:N** com `orders` (Um user pode ter múltiplas encomendas)

### **Regras de Negócio:**
- Email deve ser válido e único
- Student number é opcional mas se fornecido deve ser único
- Password deve ser armazenada encriptada (bcrypt, mínimo 8 caracteres)
- Created_at é gerado automaticamente no momento da criação

### **Exemplo de Dados:**
```sql
INSERT INTO users (student_number, email, full_name, password_hash) VALUES
('20240001', 'joao.silva@iade.pt', 'João Silva', '$2a$10$...');
```

---

## 2. menu_categories - Categorias de Menu

**Descrição:** Categorias de produtos disponíveis (Snacks, Refeições, Bebidas, Sobremesas).

**Nome da Tabela:** `menu_categories`

| Campo | Tipo | Null | Key | Default | Descrição |
|-------|------|------|-----|---------|-----------|
| `category_id` | INT | NO | PK | AUTO_INCREMENT | Identificador único da categoria |
| `category_name` | VARCHAR(50) | NO | UNI | - | Nome da categoria (único) |
| `description` | TEXT | YES | - | NULL | Descrição detalhada da categoria |
| `display_order` | INT | NO | - | 0 | Ordem de apresentação na interface |

### **Constraints:**
- **Primary Key:** `category_id`
- **Unique:** `category_name`
- **Not Null:** `category_name`

### **Relações:**
- **1:N** com `menu_items` (Uma categoria contém múltiplos produtos)

### **Regras de Negócio:**
- Category_name deve ser único
- Display_order define a ordem de apresentação (1, 2, 3, ...)
- Ao apagar categoria, produtos associados são apagados (CASCADE)

### **Exemplo de Dados:**
```sql
INSERT INTO menu_categories (category_name, description, display_order) VALUES
('Snacks', 'Light snacks and quick bites', 1),
('Meals', 'Full meals and sandwiches', 2);
```

---

## 3. menu_items - Produtos do Menu

**Descrição:** Produtos disponíveis para encomenda (comida e bebidas).

**Nome da Tabela:** `menu_items`

| Campo | Tipo | Null | Key | Default | Descrição |
|-------|------|------|-----|---------|-----------|
| `item_id` | INT | NO | PK | AUTO_INCREMENT | Identificador único do produto |
| `category_id` | INT | NO | FK | - | Referência à categoria do produto |
| `item_name` | VARCHAR(100) | NO | - | - | Nome do produto |
| `description` | TEXT | YES | - | NULL | Descrição detalhada do produto |
| `price` | DECIMAL(10,2) | NO | - | - | Preço em euros (ex: 2.50) |
| `image_url` | VARCHAR(255) | YES | - | NULL | Nome do ficheiro de imagem |
| `is_available` | BOOLEAN | NO | - | TRUE | Disponibilidade (TRUE/FALSE) |
| `created_at` | TIMESTAMP | NO | - | CURRENT_TIMESTAMP | Data/hora de criação |

### **Constraints:**
- **Primary Key:** `item_id`
- **Foreign Key:** `category_id` → `menu_categories(category_id)`
- **Check:** `price >= 0`
- **Not Null:** `category_id`, `item_name`, `price`

### **Índices:**
- `PRIMARY KEY` em `item_id`
- `INDEX` em `category_id` (para queries por categoria)
- `INDEX` em `is_available` (para filtrar disponíveis)

### **Relações:**
- **N:1** com `menu_categories` (Muitos produtos pertencem a uma categoria)
- **1:N** com `order_items` (Um produto pode aparecer em múltiplas encomendas)

### **Regras de Negócio:**
- Price deve ser >= 0
- Is_available controla se o produto aparece no menu
- Image_url contém apenas o nome do ficheiro (ex: "cookies.jpg")
- Ao apagar categoria, produtos são apagados (CASCADE)

### **Exemplo de Dados:**
```sql
INSERT INTO menu_items (category_id, item_name, description, price, image_url, is_available) VALUES
(1, 'Cookies', 'Chocolate chip cookies (3 units)', 1.50, 'cookies.jpg', TRUE),
(2, 'Tosta Mista', 'Grilled ham and cheese sandwich', 2.50, 'tosta_mista.jpg', TRUE);
```

---

## 4. orders - Encomendas

**Descrição:** Encomendas realizadas pelos utilizadores.

**Nome da Tabela:** `orders`

| Campo | Tipo | Null | Key | Default | Descrição |
|-------|------|------|-----|---------|-----------|
| `order_id` | VARCHAR(50) | NO | PK | - | Código único da encomenda |
| `user_id` | INT | YES | FK | NULL | Referência ao utilizador |
| `total_price` | DECIMAL(10,2) | NO | - | - | Preço total da encomenda |
| `item_count` | INT | NO | - | - | Quantidade total de itens |
| `order_status` | VARCHAR(20) | NO | - | 'PENDING' | Estado da encomenda |
| `payment_status` | VARCHAR(20) | NO | - | 'PENDING' | Estado do pagamento |
| `qr_code_data` | TEXT | YES | - | NULL | Dados do QR Code (JSON) |
| `created_at` | TIMESTAMP | NO | - | CURRENT_TIMESTAMP | Data/hora de criação |

### **Constraints:**
- **Primary Key:** `order_id`
- **Foreign Key:** `user_id` → `users(user_id)` ON DELETE SET NULL
- **Check:** `total_price >= 0`
- **Check:** `item_count > 0`
- **Check:** `order_status` IN ('PENDING', 'PAID', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED')
- **Check:** `payment_status` IN ('PENDING', 'PAID', 'FAILED')

### **Índices:**
- `PRIMARY KEY` em `order_id`
- `INDEX` em `user_id` (para queries de histórico)
- `INDEX` em `order_status` (para filtrar por estado)

### **Relações:**
- **N:1** com `users` (Muitas encomendas pertencem a um user)
- **1:N** com `order_items` (Uma encomenda contém múltiplos itens)

### **Formato do Order ID:**
Formato: `ORD{timestamp}{4_letras_aleatorias}`

Exemplo: `ORD1737334000ABCD`

### **Estados Possíveis:**

**Order Status:**
- `PENDING` - Encomenda criada, aguardando processamento
- `PAID` - Pagamento confirmado
- `PREPARING` - Em preparação na cozinha
- `READY` - Pronta para levantamento
- `COMPLETED` - Encomenda concluída e levantada
- `CANCELLED` - Cancelada

**Payment Status:**
- `PENDING` - Pagamento pendente
- `PAID` - Pagamento confirmado
- `FAILED` - Pagamento falhou

### **QR Code Data (JSON):**
```json
{
  "type": "IADE_GO_PAYMENT",
  "order_id": "ORD1737334000ABCD",
  "user_id": 1,
  "student_number": "20240001",
  "total_price": 3.70,
  "item_count": 3,
  "timestamp": "2025-01-19T10:30:00"
}
```

### **Regras de Negócio:**
- Order_id é gerado automaticamente pelo sistema
- Total_price é calculado somando subtotals dos order_items
- Item_count é a soma das quantities dos order_items
- Se user for apagado, user_id fica NULL mas encomenda mantém-se

### **Exemplo de Dados:**
```sql
INSERT INTO orders (order_id, user_id, total_price, item_count, order_status, payment_status, qr_code_data) VALUES
('ORD1737334000ABCD', 1, 3.70, 3, 'PENDING', 'PAID', '{"type":"IADE_GO_PAYMENT",...}');
```

---

## 5. order_items - Itens da Encomenda

**Descrição:** Produtos individuais dentro de cada encomenda (linhas da encomenda).

**Nome da Tabela:** `order_items`

| Campo | Tipo | Null | Key | Default | Descrição |
|-------|------|------|-----|---------|-----------|
| `order_item_id` | INT | NO | PK | AUTO_INCREMENT | Identificador único do item |
| `order_id` | VARCHAR(50) | NO | FK | - | Referência à encomenda |
| `item_id` | INT | NO | FK | - | Referência ao produto |
| `quantity` | INT | NO | - | - | Quantidade do produto |
| `unit_price` | DECIMAL(10,2) | NO | - | - | Preço unitário (no momento) |
| `subtotal` | DECIMAL(10,2) | NO | - | - | Subtotal (quantity × unit_price) |

### **Constraints:**
- **Primary Key:** `order_item_id`
- **Foreign Key:** `order_id` → `orders(order_id)` ON DELETE CASCADE
- **Foreign Key:** `item_id` → `menu_items(item_id)` ON DELETE CASCADE
- **Check:** `quantity > 0`
- **Check:** `unit_price >= 0`
- **Check:** `subtotal >= 0`

### **Índices:**
- `PRIMARY KEY` em `order_item_id`
- `INDEX` em `order_id` (para queries dos itens de uma encomenda)

### **Relações:**
- **N:1** com `orders` (Muitos itens pertencem a uma encomenda)
- **N:1** com `menu_items` (Muitos order items referenciam um produto)

### **Regras de Negócio:**
- Unit_price é o preço do produto **no momento da compra**
- Subtotal = quantity × unit_price (calculado automaticamente)
- Se encomenda for apagada, itens são apagados (CASCADE)
- Se produto for apagado, itens são apagados (CASCADE)

### **Por que guardar unit_price?**
O preço do produto pode mudar no futuro. Guardamos o preço no momento da compra para manter histórico correto.

### **Exemplo de Dados:**
```sql
INSERT INTO order_items (order_id, item_id, quantity, unit_price, subtotal) VALUES
('ORD1737334000ABCD', 1, 2, 1.50, 3.00),
('ORD1737334000ABCD', 7, 1, 0.70, 0.70);
```

---

## 6. Relações entre Tabelas

### **Diagrama Textual:**
```
users (1) ──────< orders (N)
                     │
                     │
menu_categories (1) ─┤
       │             │
       │             ├──< order_items (N)
       │             │
       └──< menu_items (N) ──┘
```

### **Descrição das Relações:**

| Tabela Pai | Tabela Filha | Tipo | FK | On Delete |
|------------|--------------|------|-----|-----------|
| users | orders | 1:N | user_id | SET NULL |
| menu_categories | menu_items | 1:N | category_id | CASCADE |
| orders | order_items | 1:N | order_id | CASCADE |
| menu_items | order_items | 1:N | item_id | CASCADE |

### **Significado das Relações:**

**users → orders:**
- Um utilizador pode criar múltiplas encomendas
- Se user for apagado, encomendas mantêm-se mas user_id fica NULL

**menu_categories → menu_items:**
- Uma categoria contém múltiplos produtos
- Se categoria for apagada, produtos são apagados

**orders → order_items:**
- Uma encomenda contém múltiplos itens
- Se encomenda for apagada, itens são apagados

**menu_items → order_items:**
- Um produto pode aparecer em múltiplas encomendas
- Se produto for apagado, order_items são apagados

---

## 7. Índices

### **Índices Criados:**
```sql
-- Primary Keys (automáticos)
CREATE INDEX pk_users ON users(user_id);
CREATE INDEX pk_menu_categories ON menu_categories(category_id);
CREATE INDEX pk_menu_items ON menu_items(item_id);
CREATE INDEX pk_orders ON orders(order_id);
CREATE INDEX pk_order_items ON order_items(order_item_id);

-- Unique Constraints
CREATE UNIQUE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX idx_users_student_number ON users(student_number);
CREATE UNIQUE INDEX idx_menu_categories_name ON menu_categories(category_name);

-- Performance Indexes
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_menu_items_category ON menu_items(category_id);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
```

### **Justificação dos Índices:**

- `idx_orders_user_id` - Usado frequentemente para histórico de encomendas
- `idx_orders_status` - Filtrar encomendas por estado (PENDING, READY, etc)
- `idx_menu_items_category` - Listar produtos por categoria

---

## 8. Constraints

### **Check Constraints:**
```sql
-- Preços não podem ser negativos
ALTER TABLE menu_items ADD CONSTRAINT chk_price CHECK (price >= 0);
ALTER TABLE orders ADD CONSTRAINT chk_total_price CHECK (total_price >= 0);
ALTER TABLE order_items ADD CONSTRAINT chk_unit_price CHECK (unit_price >= 0);
ALTER TABLE order_items ADD CONSTRAINT chk_subtotal CHECK (subtotal >= 0);

-- Quantidades devem ser positivas
ALTER TABLE orders ADD CONSTRAINT chk_item_count CHECK (item_count > 0);
ALTER TABLE order_items ADD CONSTRAINT chk_quantity CHECK (quantity > 0);

-- Estados válidos
ALTER TABLE orders ADD CONSTRAINT chk_order_status 
  CHECK (order_status IN ('PENDING', 'PAID', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED'));

ALTER TABLE orders ADD CONSTRAINT chk_payment_status 
  CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED'));
```

---

## 9. Triggers e Funções (Futuro)

### **Possíveis Melhorias:**

**Trigger:** Atualizar total_price automaticamente
```sql
CREATE TRIGGER update_order_total 
AFTER INSERT ON order_items
FOR EACH ROW
UPDATE orders 
SET total_price = (SELECT SUM(subtotal) FROM order_items WHERE order_id = NEW.order_id)
WHERE order_id = NEW.order_id;
```

**Function:** Calcular subtotal
```sql
CREATE FUNCTION calculate_subtotal(qty INT, price DECIMAL(10,2)) 
RETURNS DECIMAL(10,2)
RETURN qty * price;
```

---

## 10. Estatísticas da Base de Dados

### **Tamanho Estimado:**

| Tabela | Linhas (Exemplo) | Tamanho Estimado |
|--------|------------------|------------------|
| users | 100-1000 | ~100 KB |
| menu_categories | 4-10 | < 1 KB |
| menu_items | 50-200 | ~50 KB |
| orders | 1000-10000 | ~1 MB |
| order_items | 3000-30000 | ~3 MB |

**Total Estimado:** ~5 MB para base de dados de exemplo

---

**Fim do Dicionário de Dados**
