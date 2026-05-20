# Futura Commerce - Comprehensive Architecture & Design Deep Dive

## 1. System Architecture
Futura Commerce is built as a distributed microservice e-commerce ecosystem consisting of:
- **futura-gateway**: Spring Cloud Gateway providing API ingress routing, CORS policies, non-caching security headers, and rate limiting.
- **futura-admin**: Administrative backend service managing catalog, promotions, logistics, user RBAC permissions, and dashboard analytics.
- **futura-user**: Customer-facing service handling user authentication, profile details, and session verification.
- **futura-product**: Product microservice responsible for catalog hierarchies, SKU specifications, feature attributes, and high-performance read caches.
- **futura-order**: Order management service overseeing checkout processing, Redisson distributed locking, asynchronous RabbitMQ queuing, logistics dispatch, and Excel reports.
- **futura-seckill**: Flash sale marketing service governing flash promotions, coupons, and activity quotas.
- **futura-ai**: Back-office AI assistant service orchestrating LLM tool calling, Redis conversation memory, and order verification.
- **futura-search**: Elasticsearch full-text search microservice providing fuzzy product lookups, category filtering, and highlighted search results.
- **futura-mbg**: Shared domain entity definitions, JPA specifications, and repository interfaces.
- **futura-security**: Reusable stateless JWT authentication filters, token utilities, and Spring Security configurations.
- **futura-feign**: OpenFeign inter-service RPC contracts.
- **futura-common**: Unified API response wrappers (`CommonResult`), exception handlers, and DTO definitions.

## 2. High Concurrency & Flash Sale Resilience
- **Redis & Lua Atomicity**: Flash sale stock decrements and duplicate purchase prevention execute atomically in Redis via Lua scripts before hitting persistence queues.
- **RabbitMQ Asynchronous Processing**: Decouples incoming flash traffic from relational writes, safeguarding MySQL against traffic spikes.
- **Redisson Distributed Locks**: Prevents concurrency races and double-spending across distributed service nodes.
- **Decoupled Cache Invalidation**: Replaces brittle binlog scraping with Spring application events and asynchronous cache eviction for catalog details.
