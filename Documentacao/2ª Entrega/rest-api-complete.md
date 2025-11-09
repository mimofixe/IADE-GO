# Documentação REST API - IADE GO

**Base URL:** `http://localhost:8081`

**Versão:** 1.0  
**Formato:** JSON  
**Charset:** UTF-8

---

## Índice

1. [Autenticação](#1-autenticação)
2. [Menu - Categorias](#2-menu---categorias)
3. [Menu - Items](#3-menu---items)
4. [Encomendas](#4-encomendas)
5. [Utilizadores](#5-utilizadores)
6. [Códigos de Estado HTTP](#6-códigos-de-estado-http)
7. [Estrutura de Erros](#7-estrutura-de-erros)

---

## 1. Autenticação

### 1.1 Registar Utilizador

**Endpoint:** `POST /api/auth/register`

**Descrição:** Cria um novo utilizador no sistema.

**Request Body:**
```json
{
  "email": "utilizador@iade.pt",
  "fullName": "Nome Completo",
  "studentNumber": "20240001",
  "password": "minhapassword"
}
```

**Response (201 Created):**
```json
{
  "userId": 1,
  "email": "utilizador@iade.pt",
  "fullName": "Nome Completo",
  "studentNumber": "20240001",
  "createdAt": "2025-01-19T10:30:00"
}
```

**Possíveis Erros:**
- `409 CONFLICT` - Email ou número de estudante já existe

---

### 1.2 Login

**Endpoint:** `POST /api/auth/login`

**Descrição:** Autentica um utilizador e retorna os seus dados.

**Request Body:**
```json
{
  "email": "joao.silva@iade.pt",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "userId": 1,
  "email": "joao.silva@iade.pt",
  "fullName": "João Silva",
  "studentNumber": "20240001",
  "message": "Login successful"
}
```

**Possíveis Erros:**
- `401 UNAUTHORIZED` - Email ou password inválidos

---

### 1.3 Ver Perfil do Utilizador

**Endpoint:** `GET /api/auth/users/{userId}`

**Descrição:** Retorna os dados de um utilizador.

**URL Parameters:**
- `userId` (Long) - ID do utilizador

**Response (200 OK):**
```json
{
  "userId": 1,
  "email": "joao.silva@iade.pt",
  "fullName": "João Silva",
  "studentNumber": "20240001",
  "createdAt": "2024-09-01T09:00:00"
}
```

**Possíveis Erros:**
- `404 NOT FOUND` - Utilizador não existe

---

## 2. Menu - Categorias

### 2.1 Listar Todas as Categorias

**Endpoint:** `GET /api/menu/categories`

**Descrição:** Retorna todas as categorias de menu ordenadas por `displayOrder`.

**Response (200 OK):**
```json
[
  {
    "categoryId": 1,
    "categoryName": "Snacks",
    "description": "Light snacks and quick bites",
    "displayOrder": 1
  },
  {
    "categoryId": 2,
    "categoryName": "Meals",
    "description": "Full meals and sandwiches",
    "displayOrder": 2
  }
]
```

---

### 2.2 Ver Categoria Específica

**Endpoint:** `GET /api/menu/categories/{id}`

**URL Parameters:**
- `id` (Long) - ID da categoria

**Response (200 OK):**
```json
{
  "categoryId": 1,
  "categoryName": "Snacks",
  "description": "Light snacks and quick bites",
  "displayOrder": 1
}
```

---

### 2.3 Criar Categoria

**Endpoint:** `POST /api/menu/categories`

**Request Body:**
```json
{
  "categoryName": "Bebidas Quentes",
  "description": "Hot beverages",
  "displayOrder": 5
}
```

**Response (201 Created):**
```json
{
  "categoryId": 5,
  "categoryName": "Bebidas Quentes",
  "description": "Hot beverages",
  "displayOrder": 5
}
```

---

## 3. Menu - Items

### 3.1 Listar Todos os Produtos

**Endpoint:** `GET /api/menu/items`

**Descrição:** Retorna todos os produtos do menu.

**Response (200 OK):**
```json
[
  {
    "itemId": 1,
    "itemName": "Cookies",
    "description": "Chocolate chip cookies (3 units)",
    "price": 1.50,
    "imageUrl": "cookies.jpg",
    "isAvailable": true,
    "createdAt": "2025-01-15T10:00:00"
  }
]
```

---

### 3.2 Ver Produto Específico

**Endpoint:** `GET /api/menu/items/{id}`

**URL Parameters:**
- `id` (Long) - ID do produto

---

### 3.3 Listar Produtos por Categoria

**Endpoint:** `GET /api/menu/items/category/{categoryId}`

**URL Parameters:**
- `categoryId` (Long) - ID da categoria

**Exemplo:** `GET /api/menu/items/category/1`

**Response (200 OK):**
```json
[
  {
    "itemId": 1,
    "itemName": "Cookies",
    "price": 1.50,
    "isAvailable": true
  },
  {
    "itemId": 2,
    "itemName": "Croissant",
    "price": 1.80,
    "isAvailable": true
  }
]
```

---

### 3.4 Listar Produtos Disponíveis

**Endpoint:** `GET /api/menu/items/available`

**Descrição:** Retorna apenas produtos com `isAvailable = true`.

---

### 3.5 Criar Produto

**Endpoint:** `POST /api/menu/items`

**Request Body:**
```json
{
  "itemName": "Muffin de Chocolate",
  "description": "Delicious chocolate muffin",
  "price": 2.50,
  "imageUrl": "muffin.jpg",
  "isAvailable": true,
  "categoryId": 1
}
```

**Response (201 Created):**
```json
{
  "itemId": 13,
  "itemName": "Muffin de Chocolate",
  "description": "Delicious chocolate muffin",
  "price": 2.50,
  "imageUrl": "muffin.jpg",
  "isAvailable": true,
  "createdAt": "2025-01-19T10:30:00"
}
```

---

### 3.6 Atualizar Produto

**Endpoint:** `PUT /api/menu/items/{id}`

**Request Body:** Mesma estrutura do POST

**Response (200 OK):** Produto atualizado

---

### 3.7 Apagar Produto

**Endpoint:** `DELETE /api/menu/items/{id}`

**Response (204 No Content)**

---

### 3.8 Toggle Disponibilidade

**Endpoint:** `PATCH /api/menu/items/{id}/availability`

**Descrição:** Inverte o estado `isAvailable` do produto.

**Response (200 OK):**
```json
{
  "itemId": 1,
  "itemName": "Cookies",
  "isAvailable": false
}
```

---

## 4. Encomendas

### 4.1 Criar Encomenda

**Endpoint:** `POST /api/orders`

**Descrição:** Cria uma nova encomenda com múltiplos produtos.

**Request Body:**
```json
{
  "userId": 1,
  "items": [
    {
      "itemId": 1,
      "quantity": 2
    },
    {
      "itemId": 7,
      "quantity": 1
    }
  ],
  "paymentMethod": "PAY_NOW"
}
```

**Campos:**
- `userId` (Long) - ID do utilizador
- `items` (Array) - Lista de produtos e quantidades
  - `itemId` (Long) - ID do produto
  - `quantity` (Integer) - Quantidade (mínimo 1)
- `paymentMethod` (String) - "PAY_NOW" ou "PAY_LATER"

**Response (201 Created):**
```json
{
  "orderId": "ORD1737334000ABCD",
  "userId": 1,
  "userName": "João Silva",
  "totalPrice": 3.70,
  "itemCount": 3,
  "orderStatus": "PENDING",
  "paymentStatus": "PAID",
  "qrCodeData": "{\"type\":\"IADE_GO_PAYMENT\",\"order_id\":\"ORD1737334000ABCD\",\"user_id\":1,\"total_price\":3.70,\"item_count\":3}",
  "createdAt": "2025-01-19T10:30:00",
  "items": [
    {
      "itemId": 1,
      "itemName": "Cookies",
      "quantity": 2,
      "unitPrice": 1.50,
      "subtotal": 3.00
    },
    {
      "itemId": 7,
      "itemName": "Café",
      "quantity": 1,
      "unitPrice": 0.70,
      "subtotal": 0.70
    }
  ]
}
```

**Lógica de Payment Status:**
- Se `paymentMethod = "PAY_NOW"` → `paymentStatus = "PAID"`
- Se `paymentMethod = "PAY_LATER"` → `paymentStatus = "PENDING"`

**Possíveis Erros:**
- `400 BAD REQUEST` - User ou MenuItem não encontrado
- `400 BAD REQUEST` - Dados inválidos

---

### 4.2 Ver Encomenda

**Endpoint:** `GET /api/orders/{orderId}`

**URL Parameters:**
- `orderId` (String) - ID da encomenda (ex: "ORD1737334000ABCD")

**Response (200 OK):** Mesma estrutura do POST

**Possíveis Erros:**
- `404 NOT FOUND` - Encomenda não existe

---

### 4.3 Ver Histórico de Encomendas do Utilizador

**Endpoint:** `GET /api/orders/user/{userId}`

**URL Parameters:**
- `userId` (Long) - ID do utilizador

**Response (200 OK):**
```json
[
  {
    "orderId": "ORD1737334000ABCD",
    "userId": 1,
    "totalPrice": 3.70,
    "orderStatus": "PENDING",
    "createdAt": "2025-01-19T10:30:00",
    ...
  },
  {
    "orderId": "ORD1736950800WXYZ",
    "userId": 1,
    "totalPrice": 5.50,
    "orderStatus": "COMPLETED",
    "createdAt": "2025-01-15T08:30:00",
    ...
  }
]
```

**Nota:** Ordenado por data de criação (mais recente primeiro).

---

### 4.4 Atualizar Status da Encomenda

**Endpoint:** `PATCH /api/orders/{orderId}/status`

**Descrição:** Atualiza o estado da encomenda e/ou pagamento.

**Request Body:**
```json
{
  "orderStatus": "PREPARING",
  "paymentStatus": "PAID"
}
```

**Campos Opcionais:**
- `orderStatus` - "PENDING", "PAID", "PREPARING", "READY", "COMPLETED", "CANCELLED"
- `paymentStatus` - "PENDING", "PAID", "FAILED"

**Response (200 OK):** Encomenda atualizada

---

## 6. Códigos de Estado HTTP

| Código | Significado | Uso |
|--------|-------------|-----|
| 200 | OK | Sucesso (GET, PUT, PATCH) |
| 201 | Created | Recurso criado (POST) |
| 204 | No Content | Sucesso sem corpo (DELETE) |
| 400 | Bad Request | Dados inválidos |
| 401 | Unauthorized | Autenticação falhou |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Conflito (ex: email duplicado) |
| 500 | Internal Server Error | Erro no servidor |

---

## 7. Estrutura de Erros

**Exemplo de erro (400 Bad Request):**
```json
{
  "timestamp": "2025-01-19T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User not found with ID: 999",
  "path": "/api/orders"
}
```

---

## 8. Notas de Implementação

### Validações
- Email deve ser único
- Student number deve ser único
- Quantidade de items deve ser > 0
- UserId e ItemId devem existir na base de dados

### QR Code
O campo `qrCodeData` contém um JSON string com informação para validação:
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

### Order ID Generation
Formato: `ORD` + `timestamp` + `4 letras aleatórias`

Exemplo: `ORD1737334000ABCD`

---

## 9. Exemplos de Uso Completo

### Fluxo: Utilizador faz Login e cria Encomenda

**1. Login**
```bash
POST /api/auth/login
Body: {"email": "joao.silva@iade.pt", "password": "password123"}
Response: {"userId": 1, ...}
```

**2. Ver Menu**
```bash
GET /api/menu/items
Response: [array de produtos]
```

**3. Criar Encomenda**
```bash
POST /api/orders
Body: {"userId": 1, "items": [...], "paymentMethod": "PAY_NOW"}
Response: {"orderId": "ORD...", "qrCodeData": "...", ...}
```

**4. Ver Encomenda**
```bash
GET /api/orders/ORD1737334000ABCD
Response: {detalhes completos da encomenda}
```

---

**Fim da Documentação REST API**
