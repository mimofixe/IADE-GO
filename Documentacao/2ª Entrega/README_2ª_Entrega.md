# Projeto_3o_Semestre
Projeto de 3º semestre IADE 
# 📱 IADE GO — 2ª Entrega

### Unidade Curricular: Projeto Integrador — 3º Semestre  
**Curso:** Engenharia Informática  
**Instituição:** IADE — Universidade Europeia  
**Ano Letivo:** 2024/2025  

---

## 🧩 Equipa

| Nome | Nº Aluno | Papel |
|------|-----------|-------|
| **Yosvany Nunes** | 20230853 | Mobile Development |
| **Francisco Lourinho** | 202301001 | Backend & API Integration |
| **David Bação** | 20230331 | Base de Dados & Infraestrutura |
| **Vivandro Kambaza** | 20241805 | UI/UX Design & Documentação |

---

## 📖 Descrição do Projeto

**IADE GO** é uma aplicação mobile que permite aos estudantes da IADE realizar **pedidos e pagamentos de refeições ou snacks** na cafetaria de forma digital e rápida, através de **códigos QR**.  

A aplicação é composta por:
- **Frontend (Android / Kotlin)** — desenvolvido na disciplina de PDM;  
- **Backend (Node.js / Express / PostgreSQL)** — desenvolvido para esta entrega;  
- **Base de Dados** — gerida via scripts SQL (`create.sql`, `populate.sql`, `queries.sql`).

---

## 🧱 Arquitetura Geral

O sistema é baseado num modelo **cliente-servidor RESTful**, onde o frontend comunica com o backend através de uma API segura com **autenticação JWT**.

```
Frontend (Android)
       │
       ▼
Backend (Node.js / Express)
       │
       ▼
Database (PostgreSQL)
```

---

## 🧠 Protótipo Funcional

O protótipo da aplicação Android apresenta as seguintes telas principais:

1. **Login / Registo** (autenticação de estudante)  
2. **Menu da Cafetaria** (listagem de produtos)  
3. **Carrinho e Checkout** (criação de pedidos)  
4. **QR Code Payment** (pagamento digital)  
5. **Histórico de Pedidos**  
6. **Perfil de Utilizador**  
7. **Formulário de Contacto**

---

## 🗄️ Base de Dados

### 📂 Estrutura

A base de dados é composta por 4 tabelas principais:

| Tabela | Descrição |
|--------|------------|
| `users` | Armazena dados dos estudantes |
| `menu_items` | Itens do menu da cafetaria |
| `orders` | Pedidos realizados |
| `contact_requests` | Mensagens enviadas pelo formulário de contacto |

---

### 🧰 Scripts SQL

- **`create.sql`**: Criação das tabelas e relações (chaves primárias e estrangeiras).  
- **`populate.sql`**: Inserção de dados iniciais (utilizadores e itens do menu).  
- **`queries.sql`**: Consultas otimizadas (pedidos por estado, histórico de utilizador, etc.).

---

## 🌐 Documentação REST (Backend API)

A API foi estruturada segundo princípios REST, com **respostas padronizadas**, autenticação via **JWT**, e **validação de dados** em todos os endpoints.

---

## ⚙️ MODELS (Estrutura de Dados)

### 🧍‍♂️ User Model
```json
{
  "id": "string (uuid)",
  "name": "string",
  "student_number": "string (unique, 8 digits)",
  "email": "string (format: studentnumber@iade.pt)",
  "course": "string",
  "qr_code": "string (unique user identifier)",
  "created_at": "timestamp",
  "updated_at": "timestamp"
}
```

### 🍪 MenuItem Model
```json
{
  "id": "integer",
  "name": "string",
  "price": "decimal",
  "category": "enum: SNACKS | MEALS | DRINKS",
  "image_url": "string (optional)",
  "available": "boolean",
  "description": "string (optional)",
  "created_at": "timestamp",
  "updated_at": "timestamp"
}
```

### 🧾 Order Model
```json
{
  "id": "string (ORDxxxxxxxxXXXX)",
  "user_id": "uuid",
  "items": [
    {
      "item_id": "integer",
      "quantity": "integer",
      "price": "decimal"
    }
  ],
  "total_price": "decimal",
  "qr_code": "string (payment QR content)",
  "status": "enum: PENDING | CONFIRMED | PREPARING | READY | COMPLETED | CANCELLED",
  "payment_status": "enum: UNPAID | PAID | REFUNDED",
  "payment_method": "QR_CODE",
  "timestamp": "timestamp",
  "expires_at": "timestamp",
  "completed_at": "timestamp (nullable)",
  "created_at": "timestamp",
  "updated_at": "timestamp"
}
```

### 💬 ContactRequest Model
```json
{
  "id": "uuid",
  "name": "string",
  "last_name": "string",
  "email": "string",
  "message": "string",
  "status": "enum: NEW | READ | REPLIED",
  "created_at": "timestamp",
  "replied_at": "timestamp (nullable)"
}
```

---

## 🚀 API ENDPOINTS

### 1️⃣ Autenticação
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `POST` | `/auth/login` | Autentica o utilizador |
| `POST` | `/auth/logout` | Termina sessão |

---

### 2️⃣ Menu
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `GET` | `/menu/items` | Retorna todos os itens disponíveis |
| `GET` | `/menu/items/{category}` | Filtra itens por categoria |

---

### 3️⃣ Pedidos (Orders)
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `POST` | `/orders/create` | Cria um novo pedido |
| `POST` | `/orders/confirm-payment` | Confirma pagamento via QR |
| `GET` | `/orders/{orderId}` | Retorna detalhes do pedido |
| `GET` | `/orders/user/{userId}` | Histórico de pedidos |
| `PATCH` | `/orders/{orderId}/status` | Atualiza estado do pedido (staff) |

---

### 4️⃣ Contactos
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `POST` | `/contact/submit` | Submete formulário de contacto |

---

### 5️⃣ Perfil
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `GET` | `/users/{userId}` | Retorna dados do utilizador |
| `PUT` | `/users/{userId}` | Atualiza perfil |

---

## 💳 QR Code Payment System

### Estrutura do QR Code
```json
{
  "type": "IADE_GO_PAYMENT",
  "order_id": "ORD17358473XYZW",
  "user_id": "550e8400-e29b-41d4-a716-446655440000",
  "student_number": "20240000",
  "total_price": 5.50,
  "item_count": 3,
  "timestamp": "2025-01-15 14:30:00",
  "payment_method": "QR_CODE",
  "status": "PENDING"
}
```

### Validação do Pagamento
O backend deve:
1. Decodificar e validar o JSON do QR  
2. Confirmar a existência e o estado do pedido  
3. Verificar validade (30 minutos)  
4. Marcar como **PAID + CONFIRMED**  
5. Evitar pagamentos duplicados  

---

## 🔄 Fluxo de Estados do Pedido

| Estado | Descrição |
|---------|------------|
| `PENDING` | Criado, à espera de pagamento |
| `CONFIRMED` | Pagamento efetuado |
| `PREPARING` | Pedido em preparação |
| `READY` | Pronto para levantamento |
| `COMPLETED` | Pedido levantado |
| `CANCELLED` | Pedido cancelado |

---

## ⚠️ Error Handling

Formato padrão:
```json
{
  "success": false,
  "error": "ERROR_CODE",
  "message": "Descrição do erro"
}
```

| Código | Status | Descrição |
|--------|---------|-----------|
| `INVALID_CREDENTIALS` | 401 | Credenciais inválidas |
| `TOKEN_EXPIRED` | 401 | Token expirado |
| `UNAUTHORIZED` | 403 | Acesso negado |
| `NOT_FOUND` | 404 | Recurso inexistente |
| `QR_EXPIRED` | 410 | QR expirado (>30min) |
| `ORDER_ALREADY_PAID` | 409 | Pedido já pago |
| `SERVER_ERROR` | 500 | Erro interno |

---

## 🧪 Testes e Mock Data

**Conta de Teste:**
```
Student Number: 20240000
Password: password123
```

**Menu de Teste:**
1. Cookies — €1.50  
2. Croissant Brioche — €1.80  
3. Tosta Mista — €2.50  
4. Brownie — €2.00  
5. Sandes Mista — €3.00  
6. Torradas — €1.50  

**Trocar Mock API por Real API:**
```
AppPDM/app/src/main/java/com/example/projeto_3o_semestre_pdm/api/RetrofitClient.kt

const val USE_MOCK_DATA = false
private const val BASE_URL = "https://your-api-url.com/api/"
```

**Checklist de Testes:**
- Login válido e inválido  
- Listagem de menu  
- Criação de pedido  
- Confirmação de pagamento (válido e expirado)  
- Prevenção de duplo pagamento  
- Histórico de pedidos  
- Atualização de estado  
- Submissão de contacto  
- Gestão de token expirado  

---

## 🏁 Conclusão

O **IADE GO** representa uma solução tecnológica moderna e prática para facilitar a experiência dos estudantes na cafetaria da IADE, centralizando **pagamentos, pedidos e comunicações** numa única aplicação móvel.  

A 2ª Entrega marca o início da **integração completa entre frontend e backend**, com foco em escalabilidade, segurança e eficiência.
