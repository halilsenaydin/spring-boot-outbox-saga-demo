# Distributed Inventory Reservation System

A demo application showcasing event-driven microservices architecture built with Java Spring Boot. This project demonstrates real-world distributed system patterns including the **Outbox Pattern**, **Saga Choreography**, **Decorator Pattern**, and **Event-Driven Architecture** using RabbitMQ. The implemented services are tested with **100% unit test coverage** and integration tests powered by **Testcontainers**.

## Author

**Halil İbrahim ŞENAYDIN**  
- E-mail: halilsenaydin@gmail.com  
- GitHub: [github.com/halilsenaydin](https://github.com/halilsenaydin)
- Linkedin: [linkedin.com/in/halilsenaydin](https://www.linkedin.com/in/halilsenaydin/) 

---

## Project Overview

This system manages product inventory and order reservations across independent microservices. Each service owns its data and communicates exclusively through asynchronous events — no direct service-to-service HTTP calls. The architecture is designed to be **extensible**, **resilient**, and **observable**.

### Key Design Decisions

- **No shared database** — each service has its own PostgreSQL instance
- **No synchronous inter-service communication** — all coordination through RabbitMQ events
- **Outbox pattern** — guarantees at-least-once event delivery even if the broker is temporarily unavailable
- **Saga choreography** — distributed transactions coordinated through domain events, no central orchestrator
- **Rich domain model** — business logic lives in entities, not anemic service layers
- **Decorator pattern** — cross-cutting concerns (outbox persistence) separated from core business logic

---

## Architecture

```
┌────────────────────────────────────────────────────────────┐
│                        Docker Network                      │
│                                                            │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │   product-   │    │  inventory-  │    │   order-     │  │
│  │   service    │    │  service     │    │   service    │  │
│  │   :8081      │    │  :8082       │    │   :8083      │  │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘  │
│         │                  │                    │          │
│         └──────────────────┴────────────────────┘          │
│                            │                               │
│                    ┌───────▼────────┐                      │
│                    │   RabbitMQ     │                      │
│                    │   :5672        │                      │
│                    │   :15672 (UI)  │                      │
│                    └───────────────┘                       │
│                                                            │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  product-db  │    │ inventory-db │    │  order-db    │  │
│  │  PostgreSQL  │    │ PostgreSQL   │    │  PostgreSQL  │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
└────────────────────────────────────────────────────────────┘
```

---

## Microservices

### product-service
Manages the product catalog. Handles product creation, deletion (soft delete), and image management.

**Responsibilities:**
- CRUD operations for products and product images
- Publishes `ProductCreatedEvent` and `ProductCreationFailedEvent` via outbox
- Listens for `InventoryCreationFailedEvent` — retries product creation if inventory initialization fails

**API:** `GET/POST /api/v1/products`, `GET /api/v1/products/{id}`, `POST /api/v1/products/{id}/images`, `DELETE /api/v1/products/{productId}/images/{imageId}`

---

### inventory-service
Manages stock levels for each product. Initialized automatically when a product is created.

**Responsibilities:**
- Initializes inventory when a `ProductCreatedEvent` is received
- Handles stock reservation and release requests
- Publishes `InventoryCreated`, `InventoryCreationFailed`, `InventoryStockReserved`, `InventoryStockReservationFailed`, `InventoryStockReleased`, `InventoryStockReleaseFailed` via outbox
- Failed reservations are persisted in a **separate transaction** (`REQUIRES_NEW`) to survive rollbacks
- Listens for `OrderCreatedEvent` — reserves stock for the given product and quantity

---

### order-service
Manages the order lifecycle using a saga state machine.

**Order States:** `PENDING → STOCK_RESERVED → PAYMENT_PENDING → CONFIRMED / CANCELLED`

> Note: Payment service integration is not included in this demo. The saga currently completes at the `STOCK_RESERVED` stage.

**Responsibilities:**
- Creates orders and initiates the saga
- Listens for inventory events to advance order state
- Publishes `OrderCreated`, `OrderCreationFailed`, `OrderConfirmed`, `OrderCancelled` via outbox

---

### shared
A common library containing shared models, base classes, and utilities used across all services.

**Contents:**
- `BaseEntity` — common JPA fields: `id`, `createdDate`, `updatedDate`, `deletedDate`, `isActive`
- `BaseOutbox` — common outbox fields: `eventType`, `payload`, `processed`
- `OutboxRepository` — generic Spring Data repository with `findByProcessedFalse`
- Event DTOs: `ProductEvent`, `InventoryEvent`, `OrderEvent`
- `JsonUtil` — Jackson serialization/deserialization utilities
- `OperationResult<Entity, Response, Event>` — typed result wrapper for service operations
- Shared Spring configurations: `SharedRabbitMQConfig`, `SharedSwaggerConfig`, `SharedJacksonConfig`

---

## Patterns & Design

### Outbox Pattern

Guarantees reliable event delivery. Instead of publishing directly to RabbitMQ, each service first writes the event to its own `outbox` table within the same database transaction. A scheduled publisher (`@Scheduled(fixedDelay = 5000)`) polls the outbox and sends unpublished events to RabbitMQ.

```
Business Transaction:
  1. Save domain entity
  2. Save outbox record (same transaction)
  → Commit

Outbox Publisher (every 5 seconds):
  3. Poll unprocessed outbox records
  4. Publish to RabbitMQ
  5. Mark as processed
```

This ensures **no event is lost** even if RabbitMQ is temporarily unavailable.

---

### Saga Choreography

Distributed transactions are coordinated through domain events — no central orchestrator. Each service reacts to events and publishes its own, forming a chain of reactions.

```
product-service                inventory-service                           order-service
      |                               |                                         |
      |-- ProductCreatedEvent ------->|                                         |
      |                               |-- (inventory initialized)               |
      |                               |                                         |
      |<-- InventoryCreationFailed ---|                                         |
      |    (retries product creation) |                                         |
      |                               |                                         |
      |                               |<-- OrderCreatedEvent -------------------|
      |                               |                                         |
      |                               |-- InventoryStockReservedEvent --------->|
      |                               |                                         |--> OrderConfirmedEvent
      |                               |                                         |
      |                               |-- InventoryStockReservationFailed ----->|
      |                               |                                         |--> OrderCancelledEvent
```

Compensating transactions (e.g., soft-deleting a product when inventory initialization fails) are handled by consuming failure events.

---

### Decorator Pattern

The Decorator pattern separates outbox persistence from core business logic. Each service has:

- `ProductManager` / `InventoryManager` — pure domain logic, no outbox awareness
- `ProductOutboxDecorator` / `InventoryOutboxDecorator` — wraps the manager, adds outbox persistence
- `ProductServiceConfig` / `InventoryServiceConfig` — wires the decorator chain via Spring `@Bean`

```java
// Core logic (no outbox)
class ProductManager implements ProductService { ... }

// Decorator (adds outbox without modifying core)
class ProductOutboxDecorator extends ProductServiceDecorator {
    @Override
    public OperationResult create(CreateProductRequest request) {
        OperationResult result = delegate.create(request);
        outboxService.save(PRODUCT_CREATED_EVENT, result.getEvent());
        return result;
    }
}

// Wired in config
@Bean
public ProductService productService(...) {
    return new ProductOutboxDecorator(new ProductManager(...), outboxService);
}
```

---

### RabbitMQ Topology

Each service publishes to its own **Topic Exchange** and declares its own queues. Consumer services only know the queue name — not the exchange or routing key of the publisher.

| Exchange | Routing Key | Queue |
|---|---|---|
| `product-exchange` | `product.created` | `product-created-queue` |
| `product-exchange` | `product.creation.failed` | `product-creation-failed-queue` |
| `inventory-exchange` | `inventory.created` | `inventory-created-queue` |
| `inventory-exchange` | `inventory.creation.failed` | `inventory-creation-failed-queue` |
| `inventory-exchange` | `inventory.stock.reserved` | `inventory-stock-reserved-queue` |
| `inventory-exchange` | `inventory.stock.reservation.failed` | `inventory-stock-reservation-failed-queue` |
| `inventory-exchange` | `inventory.stock.released` | `inventory-stock-released-queue` |
| `inventory-exchange` | `inventory.stock.release.failed` | `inventory-stock-release-failed-queue` |
| `order-exchange` | `order.created` | `order-created-queue` |
| `order-exchange` | `order.creation.failed` | `order-creation-failed-queue` |
| `order-exchange` | `order.confirmed` | `order-confirmed-queue` |
| `order-exchange` | `order.cancelled` | `order-cancelled-failed-queue` |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Messaging | RabbitMQ (Topic Exchange) |
| Database | PostgreSQL 16 |
| ORM | Hibernate / Spring Data JPA |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Containerization | Docker, Docker Compose |
| Testing | JUnit 5, Mockito, Testcontainers |
| Coverage | JaCoCo |

---

## Testing

> **Note:** Tests are currently implemented for `product-service` only. `inventory-service` and `order-service` tests are planned for a future iteration.

### Unit Tests

Every business logic class is covered with unit tests using **JUnit 5** and **Mockito**. All dependencies are mocked — no database or message broker required.

**Coverage: 100% on all business logic classes**

Tested classes include:
- `ProductManager` — all CRUD operations and exception paths
- `ProductOutboxDecorator` — verifies outbox `save` is called with correct event type
- `ProductServiceDecorator` — verifies delegation behavior including non-overridden methods
- `ProductOutboxManager` — save and saveInNewTransaction variants
- `ProductOutboxEventPublisher` — routing key resolution for each event type
- `ProductOutboxPublisher` — scheduled polling, batch processing, empty queue handling
- `InventoryEventConsumer` — soft delete and retry logic on failed inventory creation
- `ProductMapper` — entity-to-DTO and entity-to-event conversions
- `Product` / `ProductImage` — domain logic including equals/hashCode contracts
- `RabbitMQConfig` — exchange name, queue name, routing key, durability
- `ProductServiceConfig` — correct bean wiring (OutboxDecorator wrapping Manager)

Run unit tests:
```bash
cd product-service
mvn test
```

---

### Integration Tests

Integration tests use **Testcontainers** to spin up real PostgreSQL and RabbitMQ containers. Tests run against a full Spring context with actual database queries and message publishing.

**Covered scenarios:**
- `ProductsControllerIT` — full HTTP request/response cycle for all endpoints
- `InventoryEventConsumerIT` — consumer processes event, soft-deletes product, retries creation
- `ProductOutboxManagerIT` — outbox records are persisted correctly for all save variants
- `ProductOutboxPublisherIT` — scheduled outbox polling publishes events to RabbitMQ and marks records as processed

Run integration tests:
```bash
cd product-service
mvn failsafe:integration-test
```

Run all tests:
```bash
mvn clean verify -Pcoverage-it -DskipUT
```

> **Note:** Docker Desktop must be running for Testcontainers to work. Integration tests create isolated containers — your development database is never touched.

---

### Run all tests with coverage report:
```bash
mvn clean verify -Pcoverage-all
```

Coverage report is generated at `target/site/jacoco/index.html`.

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/get-started)
- Java 21 (for local development)
- Maven 3.9+ (for local development)

### Run All Services

```bash
docker compose up --build
```

### Run a Single Service (after building shared)

```bash
cd shared && mvn install
cd ../product-service && mvn spring-boot:run
```

### Access Points

| Service | URL |
|---|---|
| product-service API | http://localhost:8081/swagger-ui.html |
| inventory-service API | http://localhost:8082/swagger-ui.html |
| order-service API | http://localhost:8083/swagger-ui.html |
| RabbitMQ Management | http://localhost:15672 (guest/guest) |

---

## Project Structure

```
distributed-inventory-reservation-system/
├── shared/                          # Shared library
│   └── src/main/java/.../shared/
│       ├── business/
│       │   ├── constants/
│       │   ├── exceptions/
│       │   ├── models/
│       │   └── utils/
│       ├── core/config/
│       ├── dataAccess/abstracts/
│       └── entities/
│           ├── abstracts/
│           └── events/
│
├── product-service/
│   └── src/main/java/.../product_service/
│       ├── api/controllers/
│       ├── business/
│       │   ├── abstracts/
│       │   ├── concretes/
│       │   ├── consumers/
│       │   ├── decorators/
│       │   ├── mapper/
│       │   ├── messaging/
│       │   └── constants/
│       ├── config/
│       ├── core/config/
│       ├── dataAccess/abstracts/
│       └── entities/
│           ├── concretes/
│           └── dtos/
│
├── inventory-service/               # Same structure as product-service
├── order-service/                   # Same structure — saga state machine
└── docker-compose.yml
```

---

## Extending the System

### Adding a New Service

1. Create a new Spring Boot module
2. Add `shared` as a Maven dependency
3. Define your entity extending `BaseEntity`, outbox entity extending `BaseOutbox`
4. Implement the Manager → Decorator → Config pattern
5. Declare your exchange, queues, and bindings in a `RabbitMQConfig`
6. Add your service to `docker-compose.yml` with its own PostgreSQL instance

### Adding a New Event

1. Add the event DTO to `shared/entities/events/`
2. Add routing key and queue constants to the relevant `*EventConstant` class in `shared`
3. Add the queue/binding to the publisher service's `RabbitMQConfig`
4. Add the `case` to the publisher's `resolveRoutingKey` switch
5. Implement the consumer's `@RabbitListener` in the consuming service

---

## License

This project is licensed under the MIT License.  
You are free to use, modify, and distribute this code for both personal and commercial purposes.  
See the [LICENSE](./LICENSE) file for full license text.