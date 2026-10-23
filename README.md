# ECommerce Microservices Architecture

This project is a robust, enterprise-grade E-Commerce microservices ecosystem built with Java 21, Spring Boot 3.x, and Spring Cloud. It demonstrates advanced architectural patterns including Domain-Driven Design (DDD), Event-Driven Architecture (EDA/EDD), and the Transactional Outbox Pattern to ensure reliable inter-service communication.

Developed and maintained by **uburakcakmak**.

## Architecture Overview

The system consists of several microservices, each managing its own database to adhere strictly to microservices database-per-service principles. The ecosystem uses an API Gateway as a single entry point and a centralized configuration server.

### Microservices

1. **API Gateway (`api-gateway`)**: Built with Spring Cloud Gateway. It handles routing and includes Resilience4j for circuit breaking to fail gracefully when downstream services (like Order Service) are unavailable.
2. **Config Server (`config-server`)**: Serves centralized configurations to all microservices using the native profile.
3. **Product Service (`product-service`)**: Manages products. Uses **Redis** for efficient caching and PostgreSQL for persistent storage.
4. **Order Service (`order-service`)**: Core domain of the system. Demonstrates rich Domain-Driven Design (DDD) with `AggregateRoot`. Employs the **Transactional Outbox Pattern** to write domain events (like `OrderCreatedEvent`) to a local PostgreSQL outbox table within the same transaction as the order creation, ensuring 100% atomicity. A scheduler then reliably publishes these events to RabbitMQ.
5. **Inventory Service (`inventory-service`)**: Subscribes to `ecommerce.exchange` on RabbitMQ. Deducts stock when an order is created.
6. **Payment Service (`payment-service`)**: Subscribes to `ecommerce.exchange`. Processes payments asynchronously upon receiving domain events.
7. **Notification Service (`notification-service`)**: Subscribes to `ecommerce.exchange`. Sends emails/notifications based on domain events.
8. **Shared Kernel (`shared-kernel`)**: A common library module holding DDD base classes (e.g., `AggregateRoot`) and domain events shared across services.

### Technologies & Frameworks

*   **Java 21**: Leveraging the latest LTS features.
*   **Spring Boot 3.x & Spring Cloud**: Core application framework.
*   **PostgreSQL**: Relational database. Each service has its own logical database.
*   **RabbitMQ**: Message broker for Event-Driven communication.
*   **Redis**: Caching layer (primarily in the Product Service).
*   **Micrometer + Zipkin (Brave)**: Used for distributed tracing. Allows tracking a single request (e.g., placing an order) as it flows from the API Gateway, to the Order Service, and asynchronously through RabbitMQ into the Payment, Inventory, and Notification services.
*   **Resilience4j**: Implements Circuit Breaker pattern in the API Gateway.
*   **Docker & Docker Compose**: For containerized infrastructure.
*   **Maven**: Build automation tool.

## Architectural Patterns Explained

### Domain-Driven Design (DDD)
The project structures business logic around domains. In `shared-kernel`, you'll find the `AggregateRoot` base class which captures domain events internally. The `Order` entity extends this, encapsulating its own invariants and raising an `OrderCreatedEvent` upon creation.

### Event-Driven Architecture (EDA) & Transactional Outbox Pattern
A common problem in distributed systems is updating a local database and publishing a message to a broker simultaneously without distributed transactions (Two-Phase Commit). This project solves it using the **Transactional Outbox Pattern**:
1. `OrderApplicationService` saves the `Order` and the `OutboxMessage` to PostgreSQL in a single ACID transaction.
2. The `OutboxProcessor` periodically polls the outbox table.
3. It relays unpublished events to RabbitMQ and marks them as processed.
4. Downstream services (Inventory, Payment, Notification) consume these messages reliably.

## Getting Started

### Prerequisites
- Java 21
- Maven
- Docker and Docker Compose

### Infrastructure Setup
Spin up PostgreSQL, RabbitMQ, Redis, and Zipkin using Docker Compose:
```bash
docker-compose up -d
```
*Note: The docker-compose setup automatically runs `init-dbs.sql` to create `order_db`, `product_db`, `inventory_db`, `payment_db`, and `notification_db`.*

### Running the Application
1. Compile the entire project from the root:
```bash
mvn clean install
```
2. Start the services in the following order:
   - Config Server (Port 8888)
   - API Gateway (Port 8080)
   - Business Services (Ports 8081-8085)

### Testing the Flow
1. Check Zipkin UI at `http://localhost:9411`
2. Check RabbitMQ UI at `http://localhost:15672` (guest/guest)
3. POST a request to the API Gateway to create an order:
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"123e4567-e89b-12d3-a456-426614174000", "productId":"123e4567-e89b-12d3-a456-426614174001", "quantity":2, "totalAmount": 150.00}'
```
4. Observe the console logs of Inventory, Payment, and Notification services as they react to the RabbitMQ messages.

---
**Created by uburakcakmak**
