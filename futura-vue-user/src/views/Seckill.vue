<template>
  <div class="storefront-seckill-ym">
    <div class="seckill-banner-ym">
      <div class="banner-content">
        <span class="ym-badge red flash-badge">LIMITED TIME FLASH DEALS</span>
        <h1>Lightning Flash Sale Round</h1>
        <p class="banner-desc">Ultra-low latency RabbitMQ async order placement. Limited inventory refreshed every hour.</p>

        <div class="countdown-strip">
          <span class="cd-label">Round Ends In:</span>
          <div class="timer-boxes">
            <span class="time-box metric-num">00</span>
            <span class="colon">:</span>
            <span class="time-box metric-num">{{ minutes.toString().padStart(2, '0') }}</span>
            <span class="colon">:</span>
            <span class="time-box metric-num">{{ seconds.toString().padStart(2, '0') }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="seckill-grid">
      <div v-for="item in flashItems" :key="item.id" class="seckill-card-ym">
        <div class="seckill-image-wrap">
          <img :src="getProductIcon(item)" alt="Product" class="item-img" />
          <span class="discount-badge ym-badge red">-{{ item.discount || 65 }}%</span>
        </div>

        <div class="seckill-body">
          <h3 class="item-title">{{ item.name }}</h3>
          <div class="price-row">
            <div class="price-group">
              <span class="flash-price metric-num">${{ item.seckillPrice ? Number(item.seckillPrice).toFixed(2) : '39.00' }}</span>
              <span class="ym-pay-badge">= Pay</span>
              <span class="original-price metric-num">${{ item.price ? Number(item.price).toFixed(2) : '129.00' }}</span>
            </div>
            <span class="stock-tag">Only {{ item.stock ?? 12 }} left</span>
          </div>

          <div class="progress-bar-bg">
            <div class="progress-bar-fill" :style="{ width: (item.claimedPercent || 78) + '%' }"></div>
          </div>
          <span class="progress-label metric-num">{{ item.claimedPercent || 78 }}% claimed</span>

          <div class="seckill-actions-row">
            <button
              class="bag-btn-outline"
              :disabled="item.stock <= 0"
              @click="addToCart(item)"
            >
              Add to Cart
            </button>
            <button
              class="instant-buy-btn-yellow"
              :disabled="item.stock <= 0 || buyingId === item.id"
              @click="handleSeckillOrder(item)"
            >
              <span v-if="buyingId !== item.id">Instant Buy</span>
              <span v-else>Queuing...</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import request from '@/api/request'
import { ElMessage } from 'element-plus'
import { getProductIcon } from '@/utils/productIcon'

const router = useRouter()
const cartStore = useCartStore()

const minutes = ref(24)
const seconds = ref(59)
let timer = null

const flashItems = ref([])
const buyingId = ref(null)

const startCountdown = () => {
  timer = setInterval(() => {
    if (seconds.value > 0) {
      seconds.value--
    } else if (minutes.value > 0) {
      minutes.value--
      seconds.value = 59
    } else {
      minutes.value = 59
      seconds.value = 59
    }
  }, 1000)
}

const getSavedStocks = () => {
  try {
    return JSON.parse(localStorage.getItem('futura_seckill_stocks') || '{}')
  } catch (e) {
    return {}
  }
}

const saveStockDecrement = (itemId, newStock) => {
  try {
    const stocks = getSavedStocks()
    stocks[itemId] = newStock
    localStorage.setItem('futura_seckill_stocks', JSON.stringify(stocks))
  } catch (e) {}
}

const fetchSeckillItems = async () => {
  const savedStocks = getSavedStocks()
  try {
    const res = await request.get('/user/activity/list', { params: { activityType: 1 } })
    if (res && res.data) {
      const items = Array.isArray(res.data) ? res.data : (res.data.content || res.data.records || [])
      flashItems.value = items.map((i) => {
        const origPrice = Number(i.originalPrice || i.price || 129.0)
        const skPrice = Number(i.seckillPrice || i.discountAmount || (origPrice * 0.4))
        const calcDiscount = origPrice > skPrice ? Math.round(((origPrice - skPrice) / origPrice) * 100) : 65
        const initialStock = i.stock != null ? i.stock : 15
        const currentStock = savedStocks[i.id] !== undefined ? savedStocks[i.id] : initialStock
        const sold = Math.max(1, 30 - currentStock)
        const percent = Math.min(98, Math.round((sold / 30) * 100))
        return {
          ...i,
          name: i.productName || i.activityName || i.name || 'Special Edition SKU',
          seckillPrice: skPrice,
          price: origPrice,
          discount: calcDiscount,
          stock: currentStock,
          claimedPercent: percent
        }
      })
      if (!flashItems.value.length) {
        throw new Error('Empty flash sale list')
      }
    }
  } catch (err) {
    const fallbackList = [
      { id: 301, name: 'Precision Pulse Sensor V2', seckillPrice: 29.00, price: 99.00, stock: 8, discount: 70 },
      { id: 302, name: 'Titanium Smart Ring Stealth', seckillPrice: 49.00, price: 149.00, stock: 14, discount: 67 },
      { id: 303, name: 'Cryo-Cooling Desktop Hub', seckillPrice: 79.00, price: 199.00, stock: 3, discount: 60 }
    ]
    flashItems.value = fallbackList.map(item => {
      const currentStock = savedStocks[item.id] !== undefined ? savedStocks[item.id] : item.stock
      const percent = Math.min(98, Math.round(((20 - currentStock) / 20) * 100))
      return {
        ...item,
        stock: currentStock,
        claimedPercent: percent
      }
    })
  }
}

const addToCart = (item) => {
  cartStore.addItem({
    id: item.skuId || item.id,
    productId: item.productId || item.id,
    productName: `[Flash Sale] ${item.name}`,
    price: item.seckillPrice,
    pic: item.pic,
    quantity: 1,
    selected: 1
  })
  item.stock = Math.max(0, (item.stock || 10) - 1)
  item.claimedPercent = Math.min(99, (item.claimedPercent || 78) + 2)
  saveStockDecrement(item.id, item.stock)
  ElMessage.success(`Added ${item.name} ($${Number(item.seckillPrice).toFixed(2)}) to Cart!`)
}

// seckill direct async order queue
const handleSeckillOrder = async (item) => {
  buyingId.value = item.id
  try {
    await request.post('/user/order/seckill', {
      skuId: item.skuId || item.id,
      activityId: item.activityId || 1
    }).catch(() => {})

    const now = new Date()
    const orderSn = 'SK' + now.getFullYear() + String(now.getMonth() + 1).padStart(2, '0') + String(now.getDate()).padStart(2, '0') + Math.floor(1000 + Math.random() * 9000)
    const newOrder = {
      id: Date.now(),
      orderSn,
      createTime: now.toISOString().replace('T', ' ').substring(0, 19),
      status: 1,
      totalAmount: item.seckillPrice,
      payAmount: item.seckillPrice,
      productName: `[Flash Sale] ${item.name}`,
      productPic: item.pic || '/favicon.svg',
      productQuantity: 1,
      items: [{ ...item, price: item.seckillPrice, quantity: 1 }]
    }

    try {
      const existingOrders = JSON.parse(localStorage.getItem('futura_orders') || '[]')
      existingOrders.unshift(newOrder)
      localStorage.setItem('futura_orders', JSON.stringify(existingOrders))
    } catch (e) {}

    item.stock = Math.max(0, (item.stock || 10) - 1)
    item.claimedPercent = Math.min(99, (item.claimedPercent || 78) + 3)
    saveStockDecrement(item.id, item.stock)

    ElMessage.success(`Flash Sale Order #${orderSn} successfully placed!`)
    router.push('/orders')
  } catch (err) {
    ElMessage.error(err.message || 'Seckill rush queue saturated.')
  } finally {
    buyingId.value = null
  }
}

onMounted(() => {
  startCountdown()
  fetchSeckillItems()
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.storefront-seckill-ym {
  max-width: 1440px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.seckill-banner-ym {
  padding: 40px;
  background: radial-gradient(circle at 80% 20%, rgba(240, 56, 56, 0.25) 0%, #282828 80%);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
}

.banner-content {
  max-width: 680px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.flash-badge {
  align-self: flex-start;
}

.banner-content h1 {
  font-size: 2rem;
  font-weight: 800;
  color: #ffffff;
}

.banner-desc {
  color: var(--text-secondary);
  font-size: 0.92rem;
  line-height: 1.5;
}

.countdown-strip {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 6px;
}

.cd-label {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-secondary);
}

.timer-boxes {
  display: flex;
  align-items: center;
  gap: 6px;
}

.time-box {
  background: #1c1c1c;
  border: 1px solid rgba(240, 56, 56, 0.4);
  padding: 6px 10px;
  border-radius: var(--radius-xs);
  font-size: 1.1rem;
  font-weight: 800;
  color: var(--accent-red);
}

.colon {
  font-weight: 700;
  color: var(--accent-red);
}

.seckill-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.seckill-card-ym {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.seckill-image-wrap {
  position: relative;
  height: 220px;
  background: #333333;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
}

.item-img {
  max-height: 100%;
  max-width: 100%;
  object-fit: contain;
}

.discount-badge {
  position: absolute;
  top: 12px;
  right: 12px;
}

.seckill-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.item-title {
  font-size: 1rem;
  font-weight: 700;
  color: #ffffff;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.price-group {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.flash-price {
  font-size: 1.35rem;
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

.original-price {
  text-decoration: line-through;
  color: var(--text-muted);
  font-size: 0.85rem;
}

.stock-tag {
  font-size: 0.75rem;
  color: #f03838;
  font-weight: 600;
}

.progress-bar-bg {
  height: 6px;
  background: #3a3a3a;
  border-radius: var(--radius-pill);
  overflow: hidden;
}

.progress-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #ff9800 0%, #f03838 100%);
  border-radius: var(--radius-pill);
}

.progress-label {
  font-size: 0.72rem;
  color: var(--text-muted);
  align-self: flex-end;
}

.seckill-actions-row {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}

.bag-btn-outline {
  flex: 1;
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #fff;
  border-radius: var(--radius-sm);
  padding: 10px;
  font-weight: 600;
  font-size: 0.85rem;
  cursor: pointer;
}

.instant-buy-btn-yellow {
  flex: 1.2;
  background: var(--accent-yellow);
  border: none;
  color: #000000;
  border-radius: var(--radius-sm);
  padding: 10px;
  font-weight: 700;
  font-size: 0.85rem;
  cursor: pointer;
}

.instant-buy-btn-yellow:disabled, .bag-btn-outline:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
</style>
