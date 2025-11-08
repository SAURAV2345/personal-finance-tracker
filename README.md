# Personal Finance Tracker (Spring Boot + JWT + MariaDB)

## ✅ Project Overview

A **Personal Finance Tracker** built using Java + Spring Boot. It allows a user to:

* Register and login securely
* Add expenses
* View expenses (with authentication)
* Uses **JWT authentication** instead of session-based login
* Auto-creates database tables using Hibernate ORM

---

## ✅ Tech Stack

| Layer                                 | Technology                        |
| ------------------------------------- | --------------------------------- |
| Backend                               | Spring Boot (3.x)                 |
| Auth                                  | Spring Security + JWT             |
| Database                              | MariaDB                           |
| ORM                                   | Hibernate + JPA                   |
| Build Tool                            | Maven                             |
| Monitoring (optional)                 | Micrometer + Prometheus + Grafana |
| Message Queue (optional future addon) | Kafka                             |
| Cache (optional future addon)         | Redis                             |

---

## ✅ Features Completed

### 🔐 Authentication & Authorization

* User Signup (`/api/auth/signup`)
* User Login (`/api/auth/login`) → returns JWT token
* OncePerRequest JWT filter validates incoming requests
* Extracts the user from JWT and sets authentication in `SecurityContext`

### 📦 Expense Management

| Endpoint                 | Action                                   |
| ------------------------ | ---------------------------------------- |
| `POST /api/expenses/add` | Add expense (authenticated)              |
| `GET /api/expenses/list` | View all expenses for the logged-in user |

---

## ✅ Architecture Diagram

```
[Image: Architecture Flow Diagram]
```

> (Diagram added below in the repository - reference: /assets/architecture.png)

---

## 🔁 Flow Explanation (End‑to‑End)

### ✅ User Signup

```
POST /api/auth/signup
↓
UserController → UserService → UserRepository → MariaDB
```

Stores user into **Users table**.

### ✅ Login + JWT Generation

```
POST /api/auth/login
↓
Spring Security AuthenticationManager
↓
On success → Generate JWT → Return to user
```

Client stores token.

### ✅ Adding Expense (Authenticated)

```
Client sends:  Authorization: Bearer <jwt>
↓
JwtAuthFilter (OncePerRequestFilter)
✓ Extract token
✓ Validate token
✓ Load user using CustomUserDetailsService
✓ Set SecurityContext
↓
ExpenseController → ExpenseService → ExpenseRepository → MariaDB
```

---

## Database Schema

### Users Table

| Column   | Type        |
| -------- | ----------- |
| id       | BIGINT (PK) |
| email    | VARCHAR     |
| password | VARCHAR     |

### Expense Table

| Column      | Type                   |
| ----------- | ---------------------- |
| id          | BIGINT (PK)            |
| user_id     | BIGINT (FK → Users.id) |
| amount      | DECIMAL                |
| description | VARCHAR                |
| category    | VARCHAR                |
| date        | DATE                   |

---

## ✅ JWT Filter Logic (Simplified)

```
1. Extract Authorization header
2. Validate token
3. Load user
4. Set authentication into SecurityContext
5. Continue with filter chain
```

Implemented using `OncePerRequestFilter`.

---

## 🗂 Project Folder Structure

```
src/main/java/com/saurav/finance_tracker/
├── controller
├── service
├── repository
├── security
│     ├── JwtAuthFilter.java
│     ├── SecurityConfig.java
└── model
```

---

## 📦 Environment Setup

### application.properties

```
spring.datasource.url=jdbc:mariadb://localhost:3306/finance_tracker
spring.datasource.username=root
spring.datasource.password=****

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🚀 How to Run

```sh
./mvnw spring-boot:run
```

Open Postman → Signup → Login → Copy Token → Call authenticated APIs.

---

## 📈 Optional Enhancements (Next Planned)

* List & Filter expenses by category/date range
* Monthly summary feature
* AOP Logging
* Export expense reports
* Deploy on Render / AWS

---

## 👏 Contributions

This project is built purely through learning and iterative feature addition.
Next goals: filtering, analytics dashboard, email notifications.

---

### Author

👤 Saurav Choudhary

> If you're reviewing this project, ⭐ the repo and follow for future updates!
