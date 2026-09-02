# E-commerce Backend

Modular monolith e-commerce backend written in **Kotlin** and **Spring Boot**, designed as a portfolio project with clear module boundaries, JWT authentication, and a PostgreSQL database.

## Tech stack

| Technology | Version / notes |
|------------|-|
| Java | 21 |
| Kotlin | 2.3.x |
| Spring Boot | 4.1 |
| Spring Modulith | 2.1 |
| Spring Security + JWT | jjwt 0.12.x |
| Spring Data JPA / Hibernate | |
| PostgreSQL | 16+ |
| Flyway | DB migrations |
| Maven | |

## Architecture

**Modular Monolith** with module boundaries enforced by Spring Modulith.

Each module follows a hexagonal-style layout:

```text
module/
├── domain/          # entities, repository interfaces
├── application/     # services, DTOs, mappers, public API
├── adapter/
│   ├── in/          # REST controllers
│   └── out/         # JPA, security, external systems
└── package-info.java
```

### Modules

| Module | Responsibility |
|--------|----------------|
| **user** | Registration, login (JWT), profile, addresses |
| **catalog** | Categories, products |
| **inventory** | Stock, reserve / release / confirm reservation |
| **cart** | Shopping cart, stock reservation on add/update |
| **order** | Checkout from cart, order snapshots |
| **shared** | Exceptions, utilities, shared events |

Cross-module communication:

- **Public API packages** (`application.api` + `@NamedInterface`)
- **Domain events** (e.g. `ProductCreatedEvent` → create stock)

### Database schemas

PostgreSQL schemas mirror modules:

- `user`, `catalog`, `inventory`, `cart`, `order`, `payment` (prepared)

Migrations: `src/main/resources/db/migration`

## Features

- JWT authentication (register / login)
- User profile + shipping/billing addresses
- Category & product CRUD
- Stock management with reservation
- Cart (add / update / remove) with inventory integration
- Create order from cart (price/name snapshot, confirm reservation, clear cart)
- Modulith module verification tests
- Bruno API collection in `docs/bruno`

## Prerequisites

- JDK 21+
- Maven 3.9+ (or use `./mvnw`)
- PostgreSQL 16+

## Configuration

Set database credentials via environment variables (or `application.yaml`):

```yaml
spring:
  application:
    name: ecommerce
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
  modulith:
    events:
      jdbc:
        schema-initialization:
          enabled: true

server:
  servlet:
    context-path: /api
```

Also configure your JWT secret (do **not** commit real secrets).

## Run

```bash
# create DB
createdb ecommerce

# run app
./mvnw spring-boot:run
```

API base URL: `http://localhost:8080/api`

## Tests

```bash
./mvnw test
```

Includes:

- context load
- Spring Modulith `ApplicationModules.verify()`
- unit tests (MockK)

## API overview

| Area | Examples |
|------|----------|
| Auth | `POST /auth/register`, `POST /auth/login` |
| User | `GET /users/me`, `PUT /users/me` |
| Catalog | `GET/POST /catalog/products`, categories |
| Cart | `GET /cart`, `POST /cart/items`, update/remove items |
| Orders | `POST /orders` (checkout from cart) |

Protected endpoints require:

```http
Authorization: Bearer <token>
```

Bruno collection: [`docs/bruno`](docs/bruno)

## Documentation

- [Module implementation order](docs/ecommerce-moduly-poradi.md)
- [Software architecture](docs/Software%20architecture%20(Kotlin%20+%20Spring).md)
- [Database architecture](docs/Database%20architecture%20(PostgreSQL).md)

## Project status

| Module | Status |
|--------|--------|
| User + Auth | Done |
| Catalog | Done |
| Inventory | Done |
| Cart | Done |
| Order | In progress / basic checkout |
| Payment | Planned |

## License

This repository is a personal portfolio project.  
Add a license (e.g. MIT) if you want others to reuse the code.
