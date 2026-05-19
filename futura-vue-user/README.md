# Futura (`futura-vue-user`)

Modern consumer electronic commerce web client built with Vue 3, Vite, Pinia, and Element Plus, themed in an obsidian fintech aesthetic.

---

## Key Modules & Capabilities

### 1. Showcase & Hero Feed (`views/Home.vue`)
- Real-time product highlights and telemetry-driven recommendation stream.
- Seamless interest reporting to backend Redis hash counters (`/user/category/click/report`) to adapt personalized suggestions.

### 2. Elasticsearch Catalog Search (`views/Catalog.vue`)
- Multi-field keyword query across title, category, and sub-attributes.
- Instant category filter pills with active state indicators.

### 3. Precision SKU Specification Matrix (`views/ProductDetail.vue`)
- Model and specification switcher updating live stock, code, and pricing.
- Verified buyer review feed with score badges and photo attachments.

### 4. High-Throughput Seckill Checkout (`views/Seckill.vue`)
- Round countdown timer with synchronized millisecond updates.
- Real-time stock consumption progress bars.
- Asynchronous RabbitMQ queue order submission to protect transactional databases.

### 5. Shopping Bag & Order Fulfillment (`views/Cart.vue`, `views/Orders.vue`)
- Shopping cart with reactive quantity modifiers, selection toggles, and total payable calculator.
- Order history with carrier telemetry tracking and review submission modal.

---

## Local Development & Execution

```bash
cd futura-vue-user
npm install
npm run dev
```
