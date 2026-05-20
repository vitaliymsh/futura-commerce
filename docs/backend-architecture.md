# Backend Architecture & Administration Overview

## Dashboard & Analytics
The administrative dashboard displays daily sales turnover, order metrics, and visitor transaction trends across configurable date ranges.

## Promotional Showcase & Quota Management
Promoted products are given top priority ranking and visibility on the customer store.
The categorization hierarchy uses an optimized 3-level tree structure (`CategoryNode`) backed by relational mapping and caching.

## SKU Management
Product SKU management allows configuring pricing, costs, dimensional weights, attribute combinations, and operational price modification history.

## Order Management
Order lifecycle states visible to merchants:
- Pending Payment (`0`)
- Pending Shipment / Paid (`1`)
- Shipped / In Transit (`2`)
- Delivered / Completed (`3`)
- Closed / Cancelled (`4`)

## Fulfillment & Shipment Dispatch
Enables merchants to assign logistical carriers (`OmsDeliveryCompany`), input tracking numbers, and trigger shipment dispatches.

## Logistics & Delivery Tracking
The logistics console tracks package milestones:
1. Picked Up (`1`)
2. In Transit (`2`)
3. Out for Delivery (`3`)
4. Signed / Delivered (`4`)

If an order has not been dispatched by the merchant, logistics actions remain disabled.

## AI Operations Assistant
The back-office AI assistant processes inquiries using LLMs and session memory. When staff query an order number (e.g. `ORDER...`), the assistant invokes internal service endpoints via Feign to retrieve structured order amounts, SKU details, and customer shipping destinations directly from the database.
