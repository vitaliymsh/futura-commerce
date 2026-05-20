# Customer Storefront Overview

## Home & Showcase
Displays promotional banners, flash sales, and prioritized product recommendations based on catalog promotion quotas.

## Product Search & Discovery
Elasticsearch integration enables full-text indexing, fuzzy search matching, keyword highlighting, and category-filtered results.

## Flash Sale & Checkout Processing
High-concurrency flash sale purchases are secured using distributed locks (Redisson) and rate-governed through asynchronous RabbitMQ order queues. Inventory is verified atomically before queueing to protect relational databases from flash traffic spikes.

## Customer Reviews & Ratings
Verified purchasers can submit reviews, product star ratings, and feedback comments linked to completed orders.

## User Account & Order History
Provides authenticated customer profiles, shipping address books, coupon wallets, and real-time tracking for active orders.

## Category Navigation
Multi-tier category hierarchy allowing customers to browse by parent and subcategories.
