<template>
  <div class="storefront-home-ym">
    <section class="hero-banner-section">
      <div class="hero-banner-content">
        <span class="hero-badge">Next-Gen E-Commerce Platform</span>
        <h1 class="hero-title">Experience Precision in Modern Commerce</h1>
        <p class="hero-desc">
          Instant checkout, autonomous carrier routing, and personalized recommendation feeds powered by Redis Bloom telemetry.
        </p>
        <div class="hero-cta-row">
          <router-link to="/catalog" class="cta-yellow">Browse Catalog</router-link>
          <router-link to="/seckill" class="cta-dark">⚡ Live Flash Deals</router-link>
        </div>
      </div>
    </section>

    <section class="products-section">
      <div class="section-top-bar">
        <div class="section-title-group">
          <h2 class="section-heading">Personalized Recommendation Stream</h2>
          <span class="section-subtext">Real-time interest weights with Bloom filter deduplication</span>
        </div>
        <button class="refresh-pill-btn" @click="fetchRecommendations">
          <span>↻</span> Refresh Stream
        </button>
      </div>

      <div class="ym-products-grid">
        <div
          v-for="item in recommendations"
          :key="item.id"
          class="ym-product-card"
          @click="viewProduct(item)"
        >
          <div class="ym-image-wrapper">
            <img
              :src="getProductIcon(item)"
              alt="product"
              class="ym-card-img"
            />

            <div v-if="item.promotionStatus === 1" class="promo-badge-stack">
              <span class="ym-badge red">SALE</span>
            </div>

            <button class="ym-quick-cart-btn" title="Add to cart" @click.stop="quickAddToCart(item)">
              <el-icon :size="18"><ShoppingCart /></el-icon>
            </button>
          </div>

          <div class="ym-card-info">
            <div class="ym-price-row">
              <span class="ym-main-price metric-num">${{ item.price ? Number(item.price).toFixed(2) : '99.00' }}</span>
              <span class="ym-pay-badge">= Pay</span>
            </div>

            <span class="ym-category-label">{{ item.categoryName || 'Standard Category' }}</span>
            <h4 class="ym-product-title">{{ item.name }}</h4>

            <div class="ym-bottom-action">
              <button class="details-btn" @click.stop="viewProduct(item)">View Details</button>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/api/request'
import { ElMessage } from 'element-plus'
import { useCartStore } from '@/stores/cart'
import { ShoppingCart } from '@element-plus/icons-vue'
import { getProductIcon } from '@/utils/productIcon'

const router = useRouter()
const cartStore = useCartStore()
const recommendations = ref([])

// load bloom recommendation feed
const fetchRecommendations = async () => {
  try {
    const res = await request.get('/user/product/recommend', { params: { pageNum: 1, pageSize: 8 } })
    if (res && res.data) {
      const items = Array.isArray(res.data) ? res.data : (res.data.content || res.data.records || [])
      recommendations.value = items.map((i) => ({
        ...i,
        id: i.productId || i.id,
        name: i.name || i.productName || 'Quantum Module',
        price: i.price != null ? Number(i.price) : 99.00,
        categoryName: i.categoryName || 'Flagship Edition'
      }))
    }
    if (!recommendations.value.length) {
      throw new Error('Empty recommendations')
    }
  } catch (err) {
    // offline fallback mock
    recommendations.value = [
      { id: 101, name: 'Neural Processing Core X1', categoryName: 'Hardware', price: 499.00, promotionStatus: 1 },
      { id: 102, name: 'Obsidian Smart Glass Touch', categoryName: 'Wearables', price: 299.00, promotionStatus: 0 },
      { id: 103, name: 'Quantum Low-Latency Hub', categoryName: 'Networking', price: 189.00, promotionStatus: 1 },
      { id: 104, name: 'Encrypted Cryptographic Key Vault', categoryName: 'Security', price: 129.00, promotionStatus: 0 }
    ]
  }
}

// telemetry hit tracking
const viewProduct = (item) => {
  request.post('/user/category/click/report', { categoryId: item.categoryId || 1, productId: item.id }).catch(() => {})
  router.push(`/product/${item.id}`)
}

const quickAddToCart = (item) => {
  cartStore.addItem(item, 1)
  ElMessage.success(`Added ${item.name} to cart.`)
}

onMounted(() => {
  fetchRecommendations()
})
</script>

<style scoped>
.storefront-home-ym {
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.hero-banner-section {
  padding: 48px;
  background: linear-gradient(135deg, #2b2b2b 0%, #1e1e1e 100%);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.hero-banner-content {
  max-width: 700px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hero-badge {
  align-self: flex-start;
  background: rgba(255, 204, 0, 0.12);
  color: var(--accent-yellow);
  border: 1px solid rgba(255, 204, 0, 0.3);
  padding: 4px 12px;
  border-radius: var(--radius-pill);
  font-size: 0.78rem;
  font-weight: 700;
}

.hero-title {
  font-size: 2.4rem;
  font-weight: 800;
  color: #ffffff;
  letter-spacing: -0.02em;
  line-height: 1.2;
}

.hero-desc {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

.hero-cta-row {
  display: flex;
  gap: 14px;
  margin-top: 8px;
}

.cta-yellow {
  background-color: var(--accent-yellow);
  color: #000000;
  padding: 12px 24px;
  border-radius: var(--radius-sm);
  font-weight: 700;
  font-size: 0.95rem;
  text-decoration: none;
  transition: background-color 0.15s;
}

.cta-yellow:hover {
  background-color: var(--accent-yellow-hover);
}

.cta-dark {
  background: #333333;
  color: #ffffff;
  border: 1px solid var(--border-subtle);
  padding: 12px 24px;
  border-radius: var(--radius-sm);
  font-weight: 600;
  font-size: 0.95rem;
  text-decoration: none;
  transition: background-0.15s;
}

.cta-dark:hover {
  background: #444444;
}

.products-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-top-bar {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.section-heading {
  font-size: 1.5rem;
  font-weight: 800;
  color: #ffffff;
}

.section-subtext {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.refresh-pill-btn {
  background: #282828;
  border: 1px solid var(--border-subtle);
  color: var(--text-secondary);
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.15s;
}

.refresh-pill-btn:hover {
  background: #333333;
  color: #ffffff;
}

.ym-products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

@media (max-width: 1024px) {
  .ym-products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.ym-product-card {
  background-color: var(--bg-card);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.ym-product-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-card);
  border-color: rgba(255, 255, 255, 0.18);
}

.ym-image-wrapper {
  position: relative;
  background: #2f2f2f;
  height: 220px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
}

.ym-card-img {
  max-height: 100%;
  max-width: 100%;
  object-fit: contain;
}

.promo-badge-stack {
  position: absolute;
  bottom: 10px;
  left: 10px;
}

.ym-badge {
  font-size: 0.65rem;
  font-weight: 800;
  padding: 2px 6px;
  border-radius: var(--radius-xs);
  line-height: 1;
}

.ym-badge.red {
  background: #f03838;
  color: #ffffff;
}

.ym-quick-cart-btn {
  position: absolute;
  bottom: 10px;
  right: 10px;
  width: 36px;
  height: 36px;
  background-color: var(--accent-yellow);
  color: #000000;
  border: none;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.15s, background-color 0.15s;
}

.ym-quick-cart-btn:hover {
  background-color: var(--accent-yellow-hover);
  transform: scale(1.08);
}

.ym-card-info {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
}

.ym-price-row {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.ym-main-price {
  font-size: 1.3rem;
  font-weight: 800;
  color: var(--accent-green);
}

.ym-pay-badge {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--accent-green);
  background: var(--accent-green-bg);
  padding: 2px 6px;
  border-radius: var(--radius-xs);
}

.ym-category-label {
  font-size: 0.72rem;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.ym-product-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: #ffffff;
  line-height: 1.35;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 2.7em;
}

.ym-bottom-action {
  margin-top: auto;
  padding-top: 10px;
}

.details-btn {
  width: 100%;
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #ffffff;
  padding: 8px;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}

.details-btn:hover {
  background: #444444;
}
</style>
