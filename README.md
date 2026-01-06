# 🎸 RiffHouse API — E-commerce Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.12-orange?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker)

## 📖 About the Project

**RiffHouse API** is the robust backend REST API for the RiffHouse e-commerce platform, specialized in musical instruments. It is built with **Java 21** and **Spring Boot 3**, following modern architectural patterns to ensure scalability, maintainability, and performance.

This project was designed to demonstrate advanced backend concepts, including **Event-Driven Architecture** for asynchronous processing, **CQRS** principles for order management, and secure **JWT Authentication**.

---

## 🚀 Key Features

### 🛒 E-commerce Core
*   **Product Management**: CRUD for products, categories, and inventory control.
*   **Order Processing**: Complete lifecycle management (Creation, Payment, Shipping, Delivery).
    *   *Architecture Highlight*: Separation of `ReadOrderController` (queries) and `WriteOrderController` (commands).
*   **Shopping Cart & Checkout**: Logic for validation and order placement.

### ⚡ Asynchronous & Event-Driven
*   **RabbitMQ Integration**: Decouples heavy operations from the main request thread.
    *   **Order Creation**: Orders are processed asynchronously.
    *   **Email Notifications**: Confirmation emails are sent via background workers.
    *   **Stock Alerts**: Automatic alerts when product stock is low.
*   **Reliability**: Implements **Dead Letter Queues (DLQ)** and retry mechanisms for fault tolerance.

### 🔒 Security & Users
*   **Authentication**: Secure login with **JWT (JSON Web Tokens)** via Auth0.
*   **Authorization**: Role-based access control (Admin vs. User).
*   **User Management**: Registration, profile updates, and address management.

### 🌐 Integrations
*   **BrasilAPI**: Automated ZIP code (CEP) lookup for addresses.
*   **MelhorEnvio**: Shipping calculation integration (via OpenFeign).
*   **MailHog**: Email testing in development environment.

---

## 🏗️ Architecture & Tech Stack

The application follows a **Layered Architecture** with strict separation of concerns, enhanced by event-driven components.

### 🛠️ Technologies
*   **Language**: Java 21
*   **Framework**: Spring Boot 3.5.5 (Web, Data JPA, Security, Validation, AMQP, Mail)
*   **Database**: PostgreSQL (Production/Dev), H2 (Test)
*   **Migration**: Flyway
*   **Messaging**: RabbitMQ
*   **Documentation**: SpringDoc OpenAPI (Swagger UI)
*   **Containerization**: Docker & Docker Compose

### 📐 Architectural Decisions
*   **CQRS-Lite**: The Order domain splits Read and Write operations into different controllers to optimize performance and clarity.
*   **DTO Pattern**: Uses Data Transfer Objects for all external communication to decouple the internal domain model from the API contract.
*   **Strategy Pattern**: Implements generic `Validator<T>` interfaces for complex business rule validations.
*   **Infrastructure Isolation**: External services (like Shipping) are accessed via Interfaces/Feign Clients to allow easy mocking and testing.

---

## ⚙️ Configuration & Environment

The application uses Spring Profiles to manage configurations for different environments.

### 📁 Profiles
*   `dev`: Active by default. Connects to local PostgreSQL and RabbitMQ. Uses MailHog for emails.
*   `prod`: For production deployment (e.g., Render). Uses environment variables for secrets.
*   `test`: Uses H2 in-memory database for fast integration testing.

### 🔑 Environment Variables (Production)
| Variable | Description |
| :--- | :--- |
| `DB_HOST`, `DB_PORT`, `DB_NAME` | Database connection details |
| `DB_USER`, `DB_PASS` | Database credentials |
| `RABBITMQ_HOST`, `RABBITMQ_PORT` | RabbitMQ connection |
| `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD` | RabbitMQ credentials |
| `EMAIL_USERNAME`, `EMAIL_PASSWORD` | SMTP credentials for sending emails |
| `JWT_SECRET` | Secret key for token generation |

---

## ⚡ Getting Started

### Prerequisites
*   **Docker & Docker Compose** (Recommended)
*   **Java 21 JDK** (If running manually)
*   **Maven** (Included wrapper `./mvnw`)

### 🐳 Run with Docker (Recommended)
This is the easiest way to start the entire stack (API + DB + RabbitMQ + MailHog).

```bash
# 1. Clone the repository
git clone https://github.com/DarkMatter015/server-ecommerce.git
cd server-ecommerce

# 2. Start services
docker-compose up --build -d
```
The API will be available at: `http://localhost:8080`

### 💻 Run Manually
If you prefer to run the application locally (e.g., for debugging), you still need PostgreSQL and RabbitMQ running.

1.  **Start Infrastructure**:
    ```bash
    docker-compose up postgres rabbitmq mailhog -d
    ```
2.  **Run Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 📚 Documentation

### Swagger UI
Interactive API documentation is available when the application is running.
👉 **URL**: `http://localhost:8080/swagger-ui.html`

### Postman Collection
A complete Postman collection with pre-configured requests is available in the `postman/` directory.
👉 [View Postman README](postman/README_Postman.md)

---

## 🧪 Testing

The project includes integration tests to ensure API reliability.

```bash
# Run all tests
./mvnw test
```

---

## 👨‍💻 Author

**Lucas Matheus de Camargo**
*   **LinkedIn**: [Lucas Matheus de Camargo](https://www.linkedin.com/in/lucas-matheus-de-camargo-49a315236/)
*   **Role**: Java Backend Developer

---
*Built with ❤️ for the Dev Community.*
