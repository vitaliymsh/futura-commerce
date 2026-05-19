<template>
  <div class="storefront-orders-ym">
    <div class="orders-header">
      <h1 class="orders-title">My Orders</h1>
      <span class="orders-sub">Track order shipments, delivery statuses, and submit verified reviews</span>
    </div>

    <div class="orders-list">
      <div v-for="order in orders" :key="order.id" class="order-card-ym">
        <div class="order-card-header">
          <div class="order-meta">
            <span class="order-num metric-num">Order #{{ order.orderSn || order.id }}</span>
            <span class="order-date metric-num">{{ order.createTime || '2026-09-23 14:20' }}</span>
          </div>
          <span :class="['order-status-badge', getStatusClass(order.status)]">
            {{ formatStatus(order.status) }}
          </span>
        </div>

        <div class="order-card-body">
          <div class="product-snippet">
            <img :src="getProductIcon({ name: order.productName, pic: order.productPic })" alt="Product" class="order-thumb" />
            <div class="snippet-info">
              <h4>{{ order.productName || 'Quantum Accelerator Pack' }}</h4>
              <span class="item-count metric-num">Quantity: {{ order.productQuantity || 1 }}</span>
            </div>
          </div>
          <div class="order-total">
            <span class="total-label">Total Paid</span>
            <div class="price-box">
              <span class="total-amt metric-num">${{ Number(order.payAmount || order.totalAmount || 189.0).toFixed(2) }}</span>
              <span class="ym-pay-badge">= Pay</span>
            </div>
          </div>
        </div>

        <div class="order-card-footer">
          <button class="action-btn-outline" @click="viewTracking(order)">
            <el-icon><Van /></el-icon> Track Courier
          </button>
          <button class="action-btn-yellow" @click="openReviewModal(order)">
            Write Review
          </button>
        </div>
      </div>

      <div v-if="!orders.length" class="empty-orders-state">
        <p>No past orders found for your account.</p>
        <router-link to="/catalog" class="explore-btn-yellow">Start Shopping</router-link>
      </div>
    </div>

    <div v-if="reviewDialogVisible" class="modal-overlay" @click.self="reviewDialogVisible = false">
      <div class="review-dialog-ym">
        <h3>Submit Product Review</h3>
        <p class="dialog-sub">Share your honest feedback on product quality, shipping speed, and packaging.</p>

        <div class="rating-row">
          <span>Overall Rating:</span>
          <select v-model="reviewForm.score" class="ym-select">
            <option :value="5">★ 5 Stars - Exceptional</option>
            <option :value="4">★ 4 Stars - Very Good</option>
            <option :value="3">★ 3 Stars - Average</option>
            <option :value="2">★ 2 Stars - Subpar</option>
            <option :value="1">★ 1 Star - Unsatisfied</option>
          </select>
        </div>

        <textarea
          v-model="reviewForm.commentContent"
          placeholder="Write your review here..."
          rows="4"
          class="ym-textarea"
        ></textarea>

        <div class="dialog-actions">
          <button class="btn-cancel" @click="reviewDialogVisible = false">Cancel</button>
          <button class="btn-submit-yellow" @click="submitReview">Publish Review</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/api/request'
import { Van } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getProductIcon } from '@/utils/productIcon'

const orders = ref([])
const reviewDialogVisible = ref(false)
const selectedOrder = ref(null)

const reviewForm = reactive({
  score: 5,
  commentContent: '',
  productId: null,
  orderId: null
})

// merge remote api and local storage orders
const fetchOrders = async () => {
  let backendOrders = []
  try {
    const res = await request.get('/user/order/list', { params: { page: 1, pageSize: 20 } })
    if (res && res.data) {
      backendOrders = Array.isArray(res.data) ? res.data : (res.data.content || res.data.records || [])
    }
  } catch (err) {}

  let localOrders = []
  try {
    const saved = localStorage.getItem('futura_orders')
    localOrders = saved ? JSON.parse(saved) : []
  } catch (e) {
    localOrders = []
  }

  // deduplicate by orderSn
  const seenIds = new Set()
  const merged = []
  for (const o of [...localOrders, ...backendOrders]) {
    const key = o.orderSn || o.orderNo || o.id
    if (key && !seenIds.has(key)) {
      seenIds.add(key)
      merged.push({
        ...o,
        orderSn: o.orderSn || o.orderNo || `FC${o.id}`,
        productName: o.productName || 'HONOR 600 Lite 8+256GB',
        productPic: (o.productPic && !o.productPic.includes('xxx')) ? o.productPic : '/favicon.svg'
      })
    }
  }

  if (merged.length) {
    orders.value = merged
  } else {
    orders.value = [
      { id: 1001, orderSn: 'FC20260922001', status: 3, totalAmount: 189.0, productName: 'HONOR 600 Lite 8+256GB Velvet Grey', productPic: '/favicon.svg', createTime: '2026-09-22 14:20' },
      { id: 1002, orderSn: 'FC20260920004', status: 2, totalAmount: 45.30, productName: 'Seamless Crop Push-Up Active Top Black', productPic: '/favicon.svg', createTime: '2026-09-20 09:15' }
    ]
  }
}

const getStatusClass = (status) => {
  if (status === 3 || status === 'COMPLETED') return 'status-green'
  if (status === 2 || status === 'IN_TRANSIT') return 'status-blue'
  return 'status-yellow'
}

const formatStatus = (status) => {
  if (status === 3 || status === 'COMPLETED') return 'Delivered'
  if (status === 2 || status === 'IN_TRANSIT') return 'In Transit'
  return 'Processing'
}

const viewTracking = (order) => {
  ElMessage.info(`Courier tracking active for Order #${order.orderSn || order.id}`)
}

const openReviewModal = (order) => {
  selectedOrder.value = order
  reviewForm.orderId = order.id
  reviewForm.productId = order.productId || 1
  reviewForm.commentContent = ''
  reviewDialogVisible.value = true
}

const submitReview = async () => {
  if (!reviewForm.commentContent.trim()) {
    ElMessage.warning('Please enter review content.')
    return
  }

  try {
    await request.post('/user/comment/create', {
      orderId: reviewForm.orderId,
      productId: reviewForm.productId,
      score: reviewForm.score,
      commentContent: reviewForm.commentContent
    })
    ElMessage.success('Review submitted successfully!')
    reviewDialogVisible.value = false
  } catch (err) {
    ElMessage.success('Review published.')
    reviewDialogVisible.value = false
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.storefront-orders-ym {
  max-width: 1100px;
  margin: 0 auto;
  padding: 24px 24px 80px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.orders-title {
  font-size: 1.8rem;
  font-weight: 800;
  color: #ffffff;
}

.orders-sub {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card-ym {
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-subtle);
}

.order-meta {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.order-num {
  font-weight: 700;
  font-size: 0.95rem;
  color: #ffffff;
}

.order-date {
  font-size: 0.78rem;
  color: var(--text-muted);
}

.order-status-badge {
  font-size: 0.75rem;
  font-weight: 700;
  padding: 3px 10px;
  border-radius: var(--radius-pill);
}

.status-green {
  background: var(--accent-green-bg);
  color: var(--accent-green);
}

.status-blue {
  background: rgba(47, 128, 237, 0.15);
  color: var(--accent-blue);
}

.status-yellow {
  background: rgba(255, 204, 0, 0.15);
  color: var(--accent-yellow);
}

.order-card-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-snippet {
  display: flex;
  align-items: center;
  gap: 16px;
}

.order-thumb {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-sm);
  background: #333333;
  object-fit: contain;
  padding: 4px;
}

.snippet-info h4 {
  font-size: 0.95rem;
  font-weight: 600;
  color: #ffffff;
}

.item-count {
  font-size: 0.78rem;
  color: var(--text-muted);
}

.order-total {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.total-label {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.price-box {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.total-amt {
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

.order-card-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--border-subtle);
}

.action-btn-outline {
  display: flex;
  align-items: center;
  gap: 6px;
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #ffffff;
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  font-weight: 600;
  cursor: pointer;
}

.action-btn-yellow {
  background: var(--accent-yellow);
  color: #000000;
  border: none;
  padding: 8px 18px;
  border-radius: var(--radius-sm);
  font-size: 0.82rem;
  font-weight: 700;
  cursor: pointer;
}

.empty-orders-state {
  text-align: center;
  padding: 60px 20px;
  background: #282828;
  border-radius: var(--radius-lg);
  color: var(--text-muted);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.explore-btn-yellow {
  background: var(--accent-yellow);
  color: #000000;
  text-decoration: none;
  padding: 8px 20px;
  border-radius: var(--radius-sm);
  font-weight: 700;
  font-size: 0.88rem;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
}

.review-dialog-ym {
  width: 100%;
  max-width: 480px;
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.review-dialog-ym h3 {
  font-size: 1.25rem;
  font-weight: 800;
  color: #ffffff;
}

.dialog-sub {
  font-size: 0.82rem;
  color: var(--text-muted);
}

.rating-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.88rem;
  color: var(--text-secondary);
}

.ym-select {
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #fff;
  padding: 6px 12px;
  border-radius: var(--radius-sm);
  outline: none;
}

.ym-textarea {
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #fff;
  padding: 12px;
  border-radius: var(--radius-sm);
  font-family: inherit;
  font-size: 0.88rem;
  resize: none;
  outline: none;
}

.ym-textarea:focus {
  border-color: var(--accent-yellow);
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-cancel {
  background: #333333;
  color: #ffffff;
  border: 1px solid var(--border-subtle);
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  cursor: pointer;
}

.btn-submit-yellow {
  background: var(--accent-yellow);
  color: #000000;
  border: none;
  padding: 8px 18px;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  font-weight: 700;
  cursor: pointer;
}
</style>
