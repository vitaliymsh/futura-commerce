<template>
  <div class="storefront-catalog-ym">
    <div class="catalog-header-panel">
      <div class="header-titles">
        <h1 class="catalog-title">{{ pageTitle }}</h1>
        <span class="results-counter">{{ products.length }} items available</span>
      </div>

      <div class="search-bar-wrap">
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="Search products, keywords, categories..."
          class="catalog-search-input"
          @keyup.enter="handleSearch"
        />
        <button class="catalog-search-btn" @click="handleSearch">
          <el-icon :size="18"><Search /></el-icon>
          <span>Search</span>
        </button>
      </div>
    </div>

    <div class="catalog-body-layout">
      <aside class="catalog-sidebar">
        <div class="sidebar-box">
          <h3 class="sidebar-title">Categories</h3>
          <div class="sidebar-category-list">
            <button
              :class="['category-nav-link', selectedCategoryId === null ? 'active' : '']"
              @click="selectCategory(null)"
            >
              All Products
            </button>
            <button
              v-for="cat in categories"
              :key="cat.id"
              :class="['category-nav-link', selectedCategoryId === cat.id ? 'active' : '']"
              @click="selectCategory(cat.id)"
            >
              {{ cat.name }}
            </button>
          </div>
        </div>
      </aside>

      <main class="catalog-main-content">
        <div v-if="products.length" class="catalog-product-grid">
          <div
            v-for="item in products"
            :key="item.id"
            class="ym-product-card"
            @click="viewDetail(item)"
          >
            <div class="ym-image-wrapper">
              <img
                :src="getProductIcon(item)"
                alt="Product"
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
                <span class="ym-main-price metric-num">${{ item.price ? Number(item.price).toFixed(2) : '149.00' }}</span>
                <span class="ym-pay-badge">= Pay</span>
              </div>

              <span class="ym-category-label">{{ item.categoryName || 'General' }}</span>
              <h4 class="ym-product-title">{{ item.name }}</h4>

              <div class="ym-bottom-action">
                <button class="details-btn" @click.stop="viewDetail(item)">View Details</button>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-catalog-state">
          <p>No products match your search or selected category.</p>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import request from '@/api/request'
import { Search, ShoppingCart } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getProductIcon } from '@/utils/productIcon'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

const searchKeyword = ref('')
const selectedCategoryId = ref(null)
const categories = ref([])
const products = ref([])

const pageTitle = computed(() => {
  if (route.query.q) return `Search Results for "${route.query.q}"`
  if (selectedCategoryId.value) {
    const found = categories.value.find(c => c.id === selectedCategoryId.value)
    if (found) return found.name
  }
  return 'Product Catalog'
})

const fetchCategories = async () => {
  try {
    const res = await request.get('/user/category/list')
    if (res && res.data) {
      categories.value = Array.isArray(res.data) ? res.data : []
    }
  } catch (err) {
    categories.value = [
      { id: 1, name: 'Compute' },
      { id: 2, name: 'Optics' },
      { id: 3, name: 'Peripherals' }
    ]
  }
}

const fetchProducts = async () => {
  try {
    const res = await request.get('/Pms_promotion/search', {
      params: {
        page: 1,
        pageSize: 24,
        name: route.query.q || searchKeyword.value || undefined,
        categoryId: selectedCategoryId.value || undefined,
        publishStatus: 1
      }
    })
    if (res && res.data) {
      let rawList = []
      if (Array.isArray(res.data)) rawList = res.data
      else if (res.data.records) rawList = res.data.records
      else if (res.data.content) rawList = res.data.content
      products.value = rawList.map(p => ({
        ...p,
        price: p.price != null ? Number(p.price) : 149.00
      }))
    }
    if (!products.value.length) throw new Error('No items')
  } catch (err) {
    products.value = [
      { id: 201, name: 'Neural Accelerator A4', categoryName: 'Compute', price: 699.00, promotionStatus: 1 },
      { id: 202, name: 'Precision Obsidian Stylus', categoryName: 'Peripherals', price: 89.00, promotionStatus: 0 },
      { id: 203, name: 'Fiber Optical Router 10G', categoryName: 'Optics', price: 219.00, promotionStatus: 1 }
    ]
  }
}

const handleSearch = () => {
  fetchProducts()
}

const selectCategory = (id) => {
  selectedCategoryId.value = id
  if (id) {
    request.post('/user/category/click/report', { categoryId: id }).catch(() => {})
  }
  fetchProducts()
}

const viewDetail = (item) => {
  router.push(`/product/${item.id}`)
}

const quickAddToCart = (item) => {
  cartStore.addItem(item, 1)
  ElMessage.success(`Added ${item.name} to cart.`)
}

watch(() => route.query.q, (newVal) => {
  if (newVal !== undefined) {
    searchKeyword.value = newVal || ''
    fetchProducts()
  }
})

onMounted(() => {
  if (route.query.q) {
    searchKeyword.value = route.query.q
  }
  fetchCategories()
  fetchProducts()
})
</script>

<style scoped>
.storefront-catalog-ym {
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.catalog-header-panel {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.header-titles {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.catalog-title {
  font-size: 1.6rem;
  font-weight: 800;
  color: #ffffff;
}

.results-counter {
  font-size: 0.82rem;
  color: var(--text-muted);
}

.search-bar-wrap {
  display: flex;
  gap: 10px;
}

.catalog-search-input {
  flex: 1;
  background: #333333;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
  padding: 12px 16px;
  color: #fff;
  font-size: 0.95rem;
  outline: none;
}

.catalog-search-input:focus {
  border-color: var(--accent-yellow);
}

.catalog-search-btn {
  background: var(--accent-yellow);
  color: #000000;
  border: none;
  border-radius: var(--radius-sm);
  padding: 0 20px;
  font-weight: 700;
  font-size: 0.92rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background-color 0.15s;
}

.catalog-search-btn:hover {
  background: var(--accent-yellow-hover);
}

.catalog-body-layout {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 24px;
  align-items: flex-start;
}

@media (max-width: 850px) {
  .catalog-body-layout {
    grid-template-columns: 1fr;
  }
}

.catalog-sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-box {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.sidebar-title {
  font-size: 1rem;
  font-weight: 700;
  color: #ffffff;
}

.sidebar-category-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.category-nav-link {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  text-align: left;
  font-size: 0.88rem;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: var(--radius-sm);
  transition: all 0.15s;
}

.category-nav-link:hover,
.category-nav-link.active {
  background: #333333;
  color: #ffffff;
  font-weight: 600;
}

.catalog-main-content {
  display: flex;
  flex-direction: column;
}

.catalog-product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

@media (max-width: 1200px) {
  .catalog-product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .catalog-product-grid {
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
  font-size: 1.25rem;
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
  font-size: 0.92rem;
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

.empty-catalog-state {
  padding: 60px 20px;
  text-align: center;
  color: var(--text-muted);
  background: #282828;
  border-radius: var(--radius-lg);
}
</style>
