# Futura Admin (`futura-vue-admin`)

Enterprise back-office management console built with Vue 3, Vite, Pinia, and Element Plus, designed with a dark obsidian Revolut-style fintech visual hierarchy.

---

## Architectural & Functional Components

### 1. Grouped Executive Sidebar (`layouts/Layout.vue`)
- Two-tier grouped operations navigation: **Core Operations** (Analytics, Catalog, Categories) and **Fulfillment & Growth** (Logistics, Marketing, After-Sales, Futura Copilot).
- Live HashiCorp Consul mesh status badge and neon pulse telemetry indicator.
- Single-click session termination and operator profile chip.

### 2. Executive Analytics Cockpit (`views/Dashboard.vue`)
- Real-time GMV, Order Velocity, Active Shoppers, and Checkout Conversion metrics cards.
- Integrated date range interval picker.
- ECharts sales revenue velocity curve with smooth gradient fills and dark glass tooltips.

### 3. Catalog & Category Taxonomy (`views/GoodsList.vue`, `views/GoodsCategory.vue`)
- Filterable product catalog with instant status toggles (Delist/Publish) and quota counters.
- 3-tier hierarchical category taxonomy cards.

### 4. Logistics & Carrier Dispatch (`views/Delivery.vue`)
- Live fulfillment telemetry strip (Pending Dispatch, In Transit, Delivered & Settled).
- Carrier tracking assignments and waybill status timeline.

### 5. Marketing, After-Sales & AI Operations (`views/Activity.vue`, `views/AfterSale.vue`, `views/AIService.vue`)
- Flash sale rounds, coupon quotas, and follower discount configuration.
- Customer refund/return ticket arbitration queue.
- Real-time Spring AI copilot terminal with automated order number regex extraction (`ORDER\d+`) and persistent session memory.

---

## Local Development & Execution

```bash
cd futura-vue-admin
npm install
npm run dev
```
