<template>
  <div class="product-detail-ym">
    <div v-if="product" class="detail-container">
      <div class="media-column">
        <div class="main-image-wrap">
          <img :src="getProductIcon(product)" alt="Product" class="main-image" />
        </div>
      </div>

      <div class="info-column">
        <div v-if="product.categoryName" class="badge-row">
          <span class="category-pill">{{ product.categoryName }}</span>
        </div>

        <h1 class="product-name">{{ product.name }}</h1>
        <p v-if="product.subTitle" class="product-sub">{{ product.subTitle }}</p>

        <div class="price-container">
          <span class="price-val metric-num">${{ Number(activeSku?.price || product.price || 199.0).toFixed(2) }}</span>
          <span class="ym-pay-badge">= Pay</span>
          <span v-if="product.originalPrice" class="original-price metric-num">${{ Number(product.originalPrice).toFixed(2) }}</span>
        </div>

        <div v-if="skuList.length" class="sku-selector-box">
          <span class="spec-label">Choose Model / Specification:</span>
          <div class="sku-pills">
            <button
              v-for="sku in skuList"
              :key="sku.id"
              :class="['sku-pill', activeSku?.id === sku.id ? 'active' : '']"
              @click="activeSku = sku"
            >
              {{ sku.spData || sku.skuCode || 'Model #' + sku.id }}
            </button>
          </div>
        </div>

        <div class="stock-indicator">
          <span class="stock-warning">Available: {{ currentStock }} in stock</span>
        </div>

        <div class="purchase-actions">
          <div class="quantity-picker">
            <button class="qty-btn" @click="quantity > 1 && quantity--">-</button>
            <span class="qty-val metric-num">{{ quantity }}</span>
            <button class="qty-btn" @click="quantity < currentStock && quantity++">+</button>
          </div>

          <button class="btn-primary-yellow" :disabled="currentStock <= 0" @click="addToCart">
            <el-icon :size="18"><ShoppingCart /></el-icon>
            <span v-if="currentStock > 0">Add to Cart</span>
            <span v-else>Out of Stock</span>
          </button>
        </div>
      </div>
    </div>

    <div class="reviews-container">
      <div class="reviews-header">
        <h2>Customer Reviews & Feedback</h2>
        <span class="subtext">Verified buyer reviews & ratings</span>
      </div>

      <div class="reviews-list">
        <div v-for="rev in reviews" :key="rev.id" class="review-item">
          <div class="review-top">
            <div class="author-wrap">
              <span class="author-avatar">👤</span>
              <span class="author-name">Buyer #{{ rev.userId || 'Verified Customer' }}</span>
            </div>
            <span class="review-score">★ {{ rev.score || 5 }}/5</span>
          </div>
          <p class="review-content">{{ rev.commentContent || 'Verified customer feedback.' }}</p>
          <div v-if="rev.commentImage" class="review-gallery">
            <img v-for="(img, idx) in rev.commentImage.split(',')" :key="idx" :src="img.trim()" class="rev-photo" alt="Photo" />
          </div>
        </div>

        <div v-if="!reviews.length" class="empty-reviews">
          No customer reviews submitted yet.
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import request from '@/api/request'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'
import { getProductIcon } from '@/utils/productIcon'

const route = useRoute()
const cartStore = useCartStore()

const product = ref(null)
const skuList = ref([])
const activeSku = ref(null)
const quantity = ref(1)
const reviews = ref([])

const currentStock = computed(() => {
  if (activeSku.value && activeSku.value.stock != null) return activeSku.value.stock
  if (product.value && product.value.stock != null) return product.value.stock
  return 40
})

const fetchProductDetail = async () => {
  const id = route.params.id
  try {
    const res = await request.get('/user/product/detail', { params: { id } })
    if (res && res.data) {
      if (res.data.product) {
        product.value = { ...res.data.product, price: Number(res.data.product.price || 199.00) }
      } else if (res.data.name) {
        product.value = { ...res.data, price: Number(res.data.price || 199.00) }
      } else {
        const searchRes = await request.get('/Pms_promotion/search', { params: { page: 1, pageSize: 50 } })
        const found = searchRes?.data?.records?.find((p) => String(p.id) === String(id)) ||
                      searchRes?.data?.content?.find((p) => String(p.id) === String(id)) ||
                      (Array.isArray(searchRes?.data) && searchRes.data.find((p) => String(p.id) === String(id)))
        if (found) {
          product.value = { ...found, price: Number(found.price || 199.00) }
        }
      }

      const rawSkus = res.data.skuList || []
      skuList.value = rawSkus.map(s => ({ ...s, price: Number(s.price || product.value?.price || 199.00) }))
      if (skuList.value.length) {
        activeSku.value = skuList.value[0]
      } else if (product.value) {
        activeSku.value = {
          id: product.value.id,
          price: product.value.price,
          stock: product.value.stock,
          skuCode: 'Standard Edition'
        }
      }
    }
  } catch (err) {
    product.value = {
      id,
      name: 'Neural Accelerator A4',
      categoryName: 'Hardware',
      price: 699.00,
      stock: 40,
      subTitle: 'Precision hardware acceleration unit with low-latency interconnect.'
    }
    activeSku.value = { id, price: 699.00, stock: 40, skuCode: 'Standard Edition' }
  }

  // deduct local pending orders from inventory
  try {
    const orders = JSON.parse(localStorage.getItem('futura_orders') || '[]')
    let orderedCount = 0
    orders.forEach((o) => {
      if (o.items && Array.isArray(o.items)) {
        o.items.forEach((it) => {
          if (String(it.productId) === String(id) || String(it.id) === String(id)) {
            orderedCount += (it.quantity || 1)
          }
        })
      }
    })
    if (product.value && product.value.stock != null) {
      product.value.stock = Math.max(0, product.value.stock - orderedCount)
    }
    if (activeSku.value && activeSku.value.stock != null) {
      activeSku.value.stock = Math.max(0, activeSku.value.stock - orderedCount)
    }
  } catch (e) {}

  try {
    const revRes = await request.get(`/user/comment/${id}`)
    if (revRes && revRes.data) {
      reviews.value = Array.isArray(revRes.data) ? revRes.data : (revRes.data.content || [])
    }
  } catch (e) {}
}

const addToCart = () => {
  if (!product.value) return
  cartStore.addItem(product.value, quantity.value, activeSku.value)
  ElMessage.success(`Added ${quantity.value}x ${product.value.name || 'Product'} to your cart.`)
}

onMounted(() => {
  fetchProductDetail()
})
</script>

<style scoped>
.product-detail-ym {
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.detail-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

@media (max-width: 900px) {
  .detail-container {
    grid-template-columns: 1fr;
  }
}

.media-column {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 480px;
}

.main-image-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main-image {
  max-width: 100%;
  max-height: 420px;
  object-fit: contain;
}

.info-column {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 32px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.badge-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.category-pill {
  font-size: 0.75rem;
  color: var(--text-secondary);
  background: #333333;
  padding: 4px 10px;
  border-radius: var(--radius-pill);
}

.product-name {
  font-size: 1.8rem;
  font-weight: 800;
  color: #ffffff;
  line-height: 1.25;
}

.product-sub {
  color: var(--text-secondary);
  font-size: 0.92rem;
  line-height: 1.5;
}

.price-container {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-top: 4px;
}

.price-val {
  font-size: 2.2rem;
  font-weight: 800;
  color: var(--accent-green);
}

.ym-pay-badge {
  font-size: 0.85rem;
  font-weight: 700;
  color: var(--accent-green);
  background: var(--accent-green-bg);
  padding: 3px 8px;
  border-radius: var(--radius-xs);
}

.original-price {
  text-decoration: line-through;
  color: var(--text-muted);
  font-size: 1.1rem;
}

.sku-selector-box {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.spec-label {
  font-size: 0.82rem;
  color: var(--text-secondary);
  font-weight: 600;
}

.sku-pills {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.sku-pill {
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: var(--text-secondary);
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.15s;
}

.sku-pill:hover,
.sku-pill.active {
  background: #3f3f3f;
  border-color: var(--accent-yellow);
  color: #ffffff;
}

.stock-indicator {
  font-size: 0.85rem;
}

.stock-warning {
  color: var(--accent-yellow);
  font-weight: 600;
}

.purchase-actions {
  display: flex;
  gap: 16px;
  margin-top: 8px;
}

.quantity-picker {
  display: flex;
  align-items: center;
  background: #333333;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
}

.qty-btn {
  background: transparent;
  border: none;
  color: #fff;
  width: 38px;
  height: 38px;
  cursor: pointer;
  font-size: 1.1rem;
}

.qty-val {
  padding: 0 12px;
  font-weight: 600;
  font-size: 0.95rem;
}

.btn-primary-yellow {
  flex: 1;
  background-color: var(--accent-yellow);
  color: #000000;
  border: none;
  border-radius: var(--radius-sm);
  font-weight: 700;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.15s;
}

.btn-primary-yellow:hover {
  background-color: var(--accent-yellow-hover);
}

.btn-primary-yellow:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.reviews-container {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 32px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.reviews-header h2 {
  font-size: 1.4rem;
  font-weight: 800;
  color: #ffffff;
}

.subtext {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-item {
  background: #222222;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-md);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.review-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  font-size: 1.1rem;
}

.author-name {
  font-weight: 600;
  font-size: 0.9rem;
  color: #ffffff;
}

.review-score {
  color: var(--accent-gold);
  font-weight: 700;
  font-size: 0.88rem;
}

.review-content {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.5;
}

.review-gallery {
  display: flex;
  gap: 8px;
  margin-top: 6px;
}

.rev-photo {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-sm);
  object-fit: cover;
  border: 1px solid var(--border-subtle);
}

.empty-reviews {
  text-align: center;
  padding: 40px;
  color: var(--text-muted);
}
</style>
