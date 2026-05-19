<template>
  <div class="storefront-cart-ym">
    <div class="cart-header">
      <h1 class="cart-title">Shopping Cart</h1>
      <span class="cart-sub">{{ selectedCount }} items selected</span>
    </div>

    <div v-if="cartStore.items.length" class="cart-layout">
      <div class="items-column">
        <div v-for="item in cartStore.items" :key="item.id" class="cart-item-card">
          <input
            type="checkbox"
            :checked="item.selected === 1"
            class="ym-checkbox"
            @change="toggleSelect(item)"
          />
          <img :src="getProductIcon(item)" alt="Thumb" class="item-thumb" />
          
          <div class="item-info">
            <h4 class="item-title">{{ item.productName || item.spData || 'Item #' + item.skuId }}</h4>
            <span class="sku-spec">{{ item.skuCode || 'Standard Spec' }}</span>
            <div class="price-wrap">
              <span class="item-price metric-num">${{ item.price ? Number(item.price).toFixed(2) : '149.00' }}</span>
              <span class="ym-pay-badge">= Pay</span>
            </div>
          </div>

          <div class="qty-stepper">
            <button class="qty-btn" @click="updateQuantity(item, -1)">-</button>
            <span class="qty-val metric-num">{{ item.quantity || 1 }}</span>
            <button class="qty-btn" @click="updateQuantity(item, 1)">+</button>
          </div>

          <button class="remove-btn" @click="removeItem(item.id)">
            <el-icon><Delete /></el-icon>
          </button>
        </div>
      </div>

      <div class="summary-column">
        <div class="summary-card">
          <h3 class="summary-heading">Order Total</h3>
          
          <div class="summary-row">
            <span>Selected Items ({{ selectedCount }})</span>
            <span class="metric-num">${{ Number(totalPrice).toFixed(2) }}</span>
          </div>

          <div class="summary-row">
            <span>Delivery</span>
            <span class="free-delivery-badge">Free</span>
          </div>

          <div class="summary-divider"></div>

          <div class="summary-row total-row">
            <span>Total to Pay</span>
            <div class="total-price-box">
              <span class="total-val metric-num">${{ Number(totalPrice).toFixed(2) }}</span>
              <span class="ym-pay-badge">= Pay</span>
            </div>
          </div>

          <button class="checkout-btn-yellow" :disabled="selectedCount === 0" @click="handleCheckout">
            Proceed to Checkout
          </button>
        </div>
      </div>
    </div>

    <div v-else class="empty-cart-card">
      <div class="empty-icon-wrap">
        <el-icon :size="56"><ShoppingCart /></el-icon>
      </div>
      <h3>Your cart is currently empty</h3>
      <p>Explore recommended items and discover special limited-time promotions.</p>
      <router-link to="/catalog" class="explore-btn-yellow">Browse Catalog</router-link>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { Delete, ShoppingCart } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getProductIcon } from '@/utils/productIcon'

const router = useRouter()
const cartStore = useCartStore()

const selectedCount = computed(() =>
  cartStore.items.filter((i) => i.selected === 1).reduce((acc, i) => acc + (i.quantity || 1), 0)
)

const totalPrice = computed(() =>
  cartStore.items
    .filter((i) => i.selected === 1)
    .reduce((acc, i) => acc + (i.price || 149.0) * (i.quantity || 1), 0)
)

const toggleSelect = (item) => {
  cartStore.toggleSelect(item)
}

const updateQuantity = (item, delta) => {
  cartStore.updateQuantity(item, delta)
}

const removeItem = (id) => {
  cartStore.removeItem(id)
  ElMessage.success('Item removed from cart.')
}

const handleCheckout = () => {
  const selectedItems = cartStore.items.filter((i) => i.selected === 1)
  if (!selectedItems.length) {
    ElMessage.warning('Please select at least one item to proceed.')
    return
  }

  // generate order entity
  const now = new Date()
  const orderSn = 'FC' + now.getFullYear() + String(now.getMonth() + 1).padStart(2, '0') + String(now.getDate()).padStart(2, '0') + Math.floor(1000 + Math.random() * 9000)
  const newOrder = {
    id: Date.now(),
    orderSn,
    createTime: now.toISOString().replace('T', ' ').substring(0, 19),
    status: 1,
    totalAmount: totalPrice.value,
    payAmount: totalPrice.value,
    productName: selectedItems.length === 1 ? selectedItems[0].productName : `${selectedItems[0].productName} + ${selectedItems.length - 1} more items`,
    productPic: selectedItems[0].pic || '/favicon.svg',
    productQuantity: selectedCount.value,
    items: [...selectedItems]
  }

  try {
    const existingOrders = JSON.parse(localStorage.getItem('futura_orders') || '[]')
    existingOrders.unshift(newOrder)
    localStorage.setItem('futura_orders', JSON.stringify(existingOrders))
  } catch (e) {
    console.error('Failed to save order:', e)
  }

  cartStore.items = cartStore.items.filter((i) => i.selected !== 1)
  ElMessage.success(`Order #${orderSn} successfully placed!`)
  router.push('/orders')
}

onMounted(() => {
  cartStore.fetchCart()
})
</script>

<style scoped>
.storefront-cart-ym {
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.cart-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.cart-title {
  font-size: 1.8rem;
  font-weight: 800;
  color: #ffffff;
}

.cart-sub {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.cart-layout {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 24px;
  align-items: flex-start;
}

@media (max-width: 900px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }
}

.items-column {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cart-item-card {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 18px 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.ym-checkbox {
  width: 18px;
  height: 18px;
  accent-color: var(--accent-yellow);
  cursor: pointer;
}

.item-thumb {
  width: 72px;
  height: 72px;
  border-radius: var(--radius-sm);
  background: #333333;
  object-fit: contain;
  padding: 6px;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-title {
  font-size: 0.95rem;
  font-weight: 600;
  color: #ffffff;
}

.sku-spec {
  font-size: 0.78rem;
  color: var(--text-muted);
}

.price-wrap {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.item-price {
  font-size: 1.15rem;
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

.qty-stepper {
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
  width: 32px;
  height: 32px;
  cursor: pointer;
  font-size: 1rem;
}

.qty-val {
  padding: 0 10px;
  font-size: 0.88rem;
  font-weight: 600;
}

.remove-btn {
  background: transparent;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  padding: 8px;
  transition: color 0.15s;
}

.remove-btn:hover {
  color: var(--accent-red);
}

.summary-card {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.summary-heading {
  font-size: 1.2rem;
  font-weight: 800;
  color: #ffffff;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.free-delivery-badge {
  color: var(--accent-green);
  font-weight: 700;
}

.summary-divider {
  height: 1px;
  background: var(--border-subtle);
}

.total-row {
  font-size: 1rem;
  font-weight: 700;
  color: #ffffff;
}

.total-price-box {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.total-val {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--accent-green);
}

.checkout-btn-yellow {
  background-color: var(--accent-yellow);
  color: #000000;
  border: none;
  border-radius: var(--radius-sm);
  padding: 14px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background-color 0.15s;
}

.checkout-btn-yellow:hover {
  background-color: var(--accent-yellow-hover);
}

.checkout-btn-yellow:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.empty-cart-card {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 80px 20px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.empty-icon-wrap {
  color: var(--text-muted);
}

.empty-cart-card h3 {
  font-size: 1.3rem;
  font-weight: 700;
  color: #ffffff;
}

.empty-cart-card p {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.explore-btn-yellow {
  background-color: var(--accent-yellow);
  color: #000000;
  text-decoration: none;
  padding: 10px 24px;
  border-radius: var(--radius-sm);
  font-size: 0.9rem;
  font-weight: 700;
  margin-top: 8px;
}
</style>
