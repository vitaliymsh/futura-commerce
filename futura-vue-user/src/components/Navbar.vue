<template>
  <header class="storefront-navbar-wrapper">
    <div class="main-navbar">
      <div class="main-content">
        <router-link to="/" class="brand-logo">
          <div class="logo-circle">F</div>
          <div class="brand-text">
            <span class="brand-title">Futura</span>
          </div>
        </router-link>

        <router-link to="/catalog" class="catalog-btn">
          <svg class="burger-icon" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5">
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
          </svg>
          <span>Catalog</span>
        </router-link>

        <div class="search-container">
          <div class="search-box">
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="Search products, categories, smart hardware..."
              class="search-input"
              @keyup.enter="handleSearch"
            />
            <button class="search-action-btn" @click="handleSearch">
              <el-icon :size="18"><Search /></el-icon>
            </button>
          </div>
        </div>

        <div class="header-actions">
          <router-link to="/seckill" class="action-item">
            <span class="action-icon-pill red">SALE</span>
            <span class="action-label">Flash Deals</span>
          </router-link>

          <router-link to="/orders" class="action-item">
            <el-icon :size="20"><Box /></el-icon>
            <span class="action-label">Orders</span>
          </router-link>

          <router-link to="/cart" class="action-item cart-item">
            <div class="cart-icon-wrap">
              <el-icon :size="20"><ShoppingCart /></el-icon>
              <span v-if="cartStore.count > 0" class="cart-badge metric-num">{{ cartStore.count }}</span>
            </div>
            <span class="action-label">Cart</span>
          </router-link>

          <div v-if="userStore.isLoggedIn" class="user-menu-wrap">
            <router-link to="/orders" class="user-profile-btn">
              <span class="user-avatar-dot"></span>
              <span>{{ userStore.user?.nickname || 'Account' }}</span>
            </router-link>
          </div>
          <router-link v-else to="/login" class="signin-btn">
            Sign In
          </router-link>
        </div>
      </div>
    </div>

    <div class="sub-navbar">
      <div class="sub-content">
        <div class="category-quicklinks">
          <router-link to="/seckill" class="quick-pill sale-pill">🔥 Flash Sale</router-link>
          <router-link to="/" class="sub-link" active-class="active">Showcase</router-link>
          <router-link to="/catalog" class="sub-link" active-class="active">All Categories</router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { Search, ShoppingCart, Box } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()
const searchKeyword = ref('')

// route to catalog with query if keyword typed
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/catalog', query: { q: searchKeyword.value.trim() } })
  } else {
    router.push('/catalog')
  }
}

// sync cart state on load
onMounted(() => {
  if (userStore.isLoggedIn) {
    cartStore.fetchCart()
  }
})
</script>

<style scoped>
.storefront-navbar-wrapper {
  display: flex;
  flex-direction: column;
  background-color: var(--bg-primary);
  border-bottom: 1px solid var(--border-subtle);
  position: sticky;
  top: 0;
  z-index: 100;
}

.main-navbar {
  background-color: #212121;
  padding: 12px 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.main-content {
  max-width: 1440px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 16px;
}

.brand-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: #fff;
  margin-right: 8px;
}

.logo-circle {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: #f03838;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 1.25rem;
  color: #fff;
}

.brand-text {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.brand-title {
  font-weight: 900;
  font-size: 1.35rem;
  letter-spacing: -0.03em;
  color: #ffffff;
}

.catalog-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: var(--accent-yellow);
  color: #000000;
  padding: 10px 18px;
  border-radius: var(--radius-sm);
  font-weight: 700;
  font-size: 0.92rem;
  text-decoration: none;
  transition: background-color 0.15s ease;
  white-space: nowrap;
}

.catalog-btn:hover {
  background-color: var(--accent-yellow-hover);
}

.burger-icon {
  display: block;
}

.search-container {
  flex: 1;
  max-width: 680px;
}

.search-box {
  display: flex;
  align-items: center;
  background: var(--bg-input);
  border: 2px solid var(--accent-yellow);
  border-radius: var(--radius-sm);
  overflow: hidden;
  height: 42px;
}

.search-input {
  flex: 1;
  background: transparent;
  border: none;
  padding: 0 16px;
  color: #fff;
  font-size: 0.95rem;
  outline: none;
}

.search-input::placeholder {
  color: var(--text-muted);
}

.search-action-btn {
  background: var(--accent-yellow);
  border: none;
  width: 50px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000;
  cursor: pointer;
  transition: opacity 0.15s;
}

.search-action-btn:hover {
  opacity: 0.9;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-left: auto;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 3px;
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 0.75rem;
  padding: 4px 6px;
  border-radius: var(--radius-sm);
  transition: color 0.15s;
}

.action-item:hover {
  color: #ffffff;
}

.action-icon-pill {
  font-size: 0.68rem;
  font-weight: 800;
  padding: 1px 6px;
  border-radius: var(--radius-pill);
}

.action-icon-pill.red {
  background: var(--accent-red);
  color: #ffffff;
}

.action-label {
  font-weight: 500;
}

.cart-icon-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.cart-badge {
  position: absolute;
  top: -6px;
  right: -10px;
  background: var(--accent-yellow);
  color: #000;
  font-size: 0.68rem;
  font-weight: 800;
  padding: 1px 5px;
  border-radius: var(--radius-pill);
}

.signin-btn {
  background: #333333;
  color: #ffffff;
  padding: 8px 18px;
  border-radius: var(--radius-sm);
  font-size: 0.88rem;
  font-weight: 600;
  text-decoration: none;
  transition: background 0.15s;
  border: 1px solid var(--border-subtle);
}

.signin-btn:hover {
  background: #444444;
}

.user-profile-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #333333;
  color: #ffffff;
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  font-weight: 600;
  text-decoration: none;
  border: 1px solid var(--border-subtle);
}

.user-avatar-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--accent-green);
}

.sub-navbar {
  background-color: #262626;
  padding: 8px 24px;
}

.sub-content {
  max-width: 1440px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.category-quicklinks {
  display: flex;
  align-items: center;
  gap: 16px;
}

.quick-pill {
  text-decoration: none;
  padding: 4px 10px;
  border-radius: var(--radius-pill);
  font-size: 0.78rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 4px;
}

.sale-pill {
  background: var(--accent-red);
  color: #fff;
}

.sub-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 500;
  transition: color 0.15s;
}

.sub-link:hover,
.sub-link.active {
  color: #fff;
}
</style>
