<template>
  <div class="after-sale-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>After-Sales & Resolution Hub</h2>
        <span class="subtext">Refund Arbitrations, Returns Inspection & Replacement Dispatches</span>
      </div>
      <div class="header-actions">
        <button class="action-btn" @click="fetchAfterSales">
          <el-icon><Refresh /></el-icon> Refresh
        </button>
      </div>
    </div>

    <!-- Requests Table -->
    <div class="table-container fintech-card">
      <table class="fintech-table">
        <thead>
          <tr>
            <th>Ticket #</th>
            <th>Order #</th>
            <th>SKU / Item</th>
            <th>Type</th>
            <th>Requested Amount</th>
            <th>Reason</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in tickets" :key="item.id">
            <td class="metric-num text-muted">#{{ item.id }}</td>
            <td class="metric-num">#{{ item.orderId }}</td>
            <td>
              <span class="item-name">{{ item.productName || item.skuName || 'Item #' + (item.skuId || item.id) }}</span>
            </td>
            <td>
              <span class="fintech-pill blue">{{ item.type === 1 ? 'Return & Refund' : 'Direct Refund' }}</span>
            </td>
            <td class="metric-num price-col">${{ item.refundAmount ? Number(item.refundAmount).toFixed(2) : '0.00' }}</td>
            <td class="reason-col">{{ item.reason || 'Package Damaged in Transit' }}</td>
            <td>
              <span :class="['fintech-pill', getStatusPill(item.status)]">
                {{ formatStatus(item.status) }}
              </span>
            </td>
            <td class="actions-cell">
              <button class="pill-action" @click="resolveTicket(item, 'APPROVE')">Approve</button>
              <button class="pill-action danger" @click="resolveTicket(item, 'REJECT')">Reject</button>
            </td>
          </tr>
          <tr v-if="!tickets.length">
            <td colspan="8" class="empty-state">No open after-sales tickets found.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { orderAPI } from '@/api'
import { Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const tickets = ref([])

const fetchAfterSales = async () => {
  try {
    const res = await orderAPI.getAfterSaleList(1, 20)
    if (res && res.data) {
      tickets.value = Array.isArray(res.data) ? res.data : (res.data.records || res.data.content || [])
    }
  } catch (err) {
    console.error('Fetch after-sales error:', err)
  }
}

const getStatusPill = (status) => {
  if (status === 1 || status === 'RESOLVED') return 'emerald'
  if (status === 2 || status === 'REJECTED') return 'rose'
  return 'amber'
}

const formatStatus = (status) => {
  if (status === 1 || status === 'RESOLVED') return 'Resolved'
  if (status === 2 || status === 'REJECTED') return 'Rejected'
  return 'Under Review'
}

const resolveTicket = (item, action) => {
  ElMessage.success(`Ticket #${item.id} arbitration marked: ${action}`)
}

onMounted(() => {
  fetchAfterSales()
})
</script>

<style scoped>
.after-sale-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.view-header {
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h2 {
  font-size: 1.25rem;
  font-weight: 700;
}

.subtext {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: var(--radius-pill);
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  color: #fff;
  font-size: 0.85rem;
  cursor: pointer;
}

.table-container {
  overflow: hidden;
}

.fintech-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.fintech-table th {
  padding: 14px 20px;
  font-size: 0.75rem;
  text-transform: uppercase;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border-subtle);
}

.fintech-table td {
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-subtle);
  font-size: 0.88rem;
}

.item-name {
  font-weight: 500;
}

.price-col {
  color: var(--accent-emerald);
  font-weight: 600;
}

.reason-col {
  font-size: 0.82rem;
  color: var(--text-secondary);
}

.actions-cell {
  display: flex;
  gap: 8px;
}

.pill-action {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-subtle);
  color: var(--text-secondary);
  border-radius: var(--radius-pill);
  padding: 4px 10px;
  font-size: 0.75rem;
  cursor: pointer;
}

.pill-action:hover {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.2);
}

.pill-action.danger:hover {
  background: rgba(255, 69, 58, 0.15);
  color: var(--accent-rose);
  border-color: rgba(255, 69, 58, 0.3);
}

.empty-state {
  text-align: center;
  padding: 40px !important;
  color: var(--text-muted);
}
</style>
