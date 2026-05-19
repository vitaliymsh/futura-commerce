# Futura Commerce

[![Spring Boot 3.2](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Vue 3](https://img.shields.io/badge/Vue.js-3.5-emerald.svg)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-8.0-purple.svg)](https://vitejs.dev/)
[![Docker Compose](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A distributed e-commerce platform built with Spring Boot 3, Vue 3, and a microservice architecture. It includes product catalog management, flash sales (seckill), order processing with distributed locks, RabbitMQ messaging, Elasticsearch search, and an AI assistant for back-office order queries.

---

## Architecture

```
                           +-------------------------------+
                           |    HashiCorp Consul Mesh      |
                           |  (Discovery & Health Checks)  |
                           +---------------+---------------+
                                           |
    +---------------------+                v                +---------------------+
    |  futura-vue-admin   | ----> +-----------------+ ----> |  futura-vue-user    |
    | (Executive Console) |       | futura-gateway  |       | (Customer Store)    |
    |     [Port 5174]     |       |   [Port 8081]   |       |     [Port 5173]     |
    +---------------------+       +--------+--------+       +---------------------+
                                           |
          +----------------+---------------+---------------+----------------+
          |                |               |               |                |
          v                v               v               v                v
    +------------+   +------------+  +-----------+   +-----------+    +-----------+
    |futura-admin|   |futura-order|  |futura-user|   |futura-seck|    | futura-ai |
    |   (8086)   |   |   (8083)   |  |  (8084)   |   |  (8085)   |    |  (8087)   |
    +-----+------+   +-----+------+  +-----+-----+   +-----+-----+    +-----+-----+
          |                |               |               |                |
          |          +-----+-------+       |               |                |
          |          |futura-prod  |       |               |                |
          |          |   (8082)    |       |               |                |
          |          +-----+-------+       |               |                |
          +----------------+---------------+---------------+----------------+
          |                |               |               |
          v                v               v               v
    +------------+  +-------------+  +-----------+  +---------------+
    |   MySQL    |  |  Redis BF   |  | RabbitMQ  |  | Elasticsearch |
    |Spring Data |  |    Bloom    |  |   Async   |  |  Full-Text    |
    |    JPA     |  | Deduplication| |   Queues  |  | Catalog Search|
    +------------+  +-------------+  +-----------+  +---------------+
```

---

## Services and Modules

| Module | Type | Port | Description |
|---|---|---|---|
| `futura-gateway` | Service | `8081` | Spring Cloud Gateway for API routing, CORS, rate limiting, and security headers |
| `futura-product` | Service | `8082` | Product catalog, categories, SKU specs, and cached read endpoints |
| `futura-order` | Service | `8083` | Order lifecycle, Redisson locks, RabbitMQ queueing, delivery tracking, Excel exports |
| `futura-user` | Service | `8084` | User authentication, profile management, addresses, and session handling |
| `futura-seckill` | Service | `8085` | Flash sales, coupon wallets, and promotion activity quotas |
| `futura-admin` | Service | `8086` | Merchant back-office, RBAC permissions, catalog administration, and analytics |
| `futura-ai` | Service | `8087` | Spring AI assistant for conversational order telemetry and tool calling |
| `futura-search` | Service | `8207` | Elasticsearch integration for full-text search and highlighted results |
| `futura-vue-admin` | Frontend | `5174` | Admin dashboard built with Vue 3, Vite, Pinia, Element Plus, and ECharts |
| `futura-vue-user` | Frontend | `5173` | Customer storefront and checkout built with Vue 3, Vite, and Element Plus |
| `futura-mbg` | Library | — | JPA domain entities, specifications, and repository interfaces |
| `futura-security` | Library | — | JWT filters and Spring Security configurations |
| `futura-feign` | Library | — | OpenFeign RPC client interfaces for inter-service communication |
| `futura-common` | Library | — | API response wrappers (`CommonResult`), error handlers, and shared DTOs |

---

## How It Works

### Persistence and Data Access
The backend uses Spring Data JPA and Hibernate across all services, with typed repositories, declarative specifications, and explicit `@Entity` relationships.

### Service Discovery
Services register with HashiCorp Consul for service discovery and active health checks. Microservices call each other through OpenFeign clients with client-side load balancing.

### Flash Sales and Concurrency
To handle flash sale traffic without overwhelming the database:
- Redis Lua scripts handle stock pre-deductions and check for duplicate purchases atomically.
- Redisson distributed locks prevent race conditions across service instances.
- RabbitMQ queues incoming orders so database writes happen asynchronously.

### Search and Deduplication
Product catalog search runs on Elasticsearch for fuzzy matching and category filters. RedisBloom filters track viewed items so recommendations do not surface duplicate products.

### AI Operations Assistant
The `futura-ai` service uses Spring AI with conversation memory. When an admin enters an order number (like `ORDER12345`), the assistant calls `OrderFeignClient` to pull live tracking, status, and shipping information directly into the chat.

### Frontend
Both frontends (admin console and customer storefront) use a dark fintech theme with Vue 3, Vite, Pinia, and Element Plus. The admin dashboard includes ECharts for tracking sales, order volume, and traffic trends.

---

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Cloud Gateway, Spring Data JPA / Hibernate, Spring Security (JWT), Spring AI, OpenFeign, Redisson, Lombok
- **Datastores & Middleware**: MySQL 8.0, Redis (with RedisBloom module), RabbitMQ 3.12, Elasticsearch 8.11, HashiCorp Consul 1.16
- **Frontend**: Vue 3.5, Vite 8.0, Pinia, Vue Router 4, Element Plus, ECharts, Axios

---

## Prerequisites

- **Java 17+** (JDK 17 or 21 LTS)
- **Maven 3.9+** (or use the `./mvnw` wrapper in the repository root)
- **Node.js 18+** and **npm**
- **Docker and Docker Compose**

---

## Quickstart

### 1. Clone the repository
```bash
git clone https://github.com/your-org/futura-commerce.git
cd futura-commerce
```

### 2. Start infrastructure with Docker Compose
Start Consul, RedisBloom, RabbitMQ, MySQL, and Elasticsearch:
```bash
docker compose up -d
```

| Service | Address | Default Credentials |
|---|---|---|
| **Consul UI** | http://localhost:8500 | None |
| **RabbitMQ Management** | http://localhost:15672 | `guest` / `guest` |
| **MySQL Database** | `localhost:3306` (or `3307`) | `root` / `root` (Database: `mall`) |
| **RedisBloom** | `localhost:6379` | None |
| **Elasticsearch** | http://localhost:9200 | Disabled for local dev |

*Note: MySQL runs [`sql/init.sql`](sql/init.sql) on initial startup to seed tables and sample data.*

### 3. Start backend services

You can run individual microservices using the launcher script:

```bash
chmod +x run-service.sh

# Start the gateway first
./run-service.sh futura-gateway

# Start domain services as needed (in separate terminals)
./run-service.sh futura-user
./run-service.sh futura-product
./run-service.sh futura-order
./run-service.sh futura-seckill
./run-service.sh futura-admin
./run-service.sh futura-ai
```

Or run via Maven directly:
```bash
./mvnw clean install
./mvnw spring-boot:run -pl futura-gateway
```

### 4. Start frontend applications

#### Customer storefront (`futura-vue-user`)
```bash
cd futura-vue-user
npm install
npm run dev
```
Available at: **`http://localhost:5173`**

#### Admin console (`futura-vue-admin`)
```bash
cd futura-vue-admin
npm install
npm run dev
```
Available at: **`http://localhost:5174`**

---

## Directory Structure

```
futura-commerce/
├── docker-compose.yml       # Local infrastructure (Consul, RedisBloom, RabbitMQ, MySQL, ES)
├── run-service.sh           # Helper script to launch individual microservices
├── pom.xml                  # Parent Maven POM
├── sql/
│   └── init.sql             # Database schema and seed data
├── docs/                    # Architecture notes and documentation
├── futura-common/           # Common utilities, exceptions, and response models
├── futura-mbg/              # JPA entities, specifications, and repository interfaces
├── futura-security/         # JWT authentication filters and security helpers
├── futura-feign/            # Shared OpenFeign RPC interfaces
├── futura-gateway/          # Spring Cloud Gateway (routing, filters, rate limiting)
├── futura-user/             # User auth and profile management service
├── futura-product/          # Product catalog and category service
├── futura-order/            # Order processing, locking, and fulfillment service
├── futura-seckill/          # Flash sale promotion engine
├── futura-admin/            # Merchant back-office administration service
├── futura-ai/               # Spring AI order telemetry and chat assistant
├── futura-search/           # Elasticsearch search service
├── futura-vue-admin/        # Admin web app (Vue 3, Vite, Element Plus, ECharts)
└── futura-vue-user/         # Storefront web app (Vue 3, Vite, Element Plus)
```

---

## Standards and Conventions

- Code, comments, schemas, and UI strings are maintained in English.
- REST endpoints return standardized `CommonResult<T>` response payloads.

---

## License

This project is licensed under the [MIT License](LICENSE).
