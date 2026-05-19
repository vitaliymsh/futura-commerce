<template>
  <div class="logistics-dispatch-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>Logistics & Carrier Dispatch</h2>
        <span class="subtext">Fulfillment Routing, Tracking Numbers & Waybills</span>
      </div>
      <div class="header-actions">
        <button class="action-btn" @click="fetchShipments">
          <el-icon><Refresh /></el-icon> Refresh State
        </button>
      </div>
    </div>

    <!-- Carrier Summary Strip -->
    <div class="status-strip">
      <div class="status-stat-card fintech-card">
        <span class="stat-label">Pending Dispatch</span>
        <span class="stat-value metric-num amber">{{ statusCounts.pending || 0 }}</span>
      </div>
      <div class="status-stat-card fintech-card">
        <span class="stat-label">In Transit</span>
        <span class="stat-value metric-num blue">{{ statusCounts.inTransit || 0 }}</span>
      </div>
      <div class="status-stat-card fintech-card">
        <span class="stat-label">Delivered & Settled</span>
        <span class="stat-value metric-num emerald">{{ statusCounts.delivered || 0 }}</span>
      </div>
    </div>

    <!-- Shipments Table -->
    <div class="table-container fintech-card">
      <table class="fintech-table">
        <thead>
          <tr>
            <th>Order #</th>
            <th>Carrier</th>
            <th>Tracking No</th>
            <th>Recipient</th>
            <th>Status</th>
            <th>Updated</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in shipments" :key="item.id || item.orderId">
            <td class="metric-num text-muted">#{{ item.orderId || item.id }}</td>
            <td>
              <span class="carrier-name">{{ item.deliveryCompany || item.carrier || 'DHL Express' }}</span>
            </td>
            <td class="metric-num tracking-col">{{ item.trackingNo || 'UNASSIGNED' }}</td>
            <td>
              <div class="recipient-cell">
                <span>{{ item.receiverName || 'Customer' }}</span>
                <span class="phone-muted metric-num">{{ item.receiverPhone || '—' }}</span>
              </div>
            </td>
            <td>
              <span :class="['fintech-pill', getStatusPillClass(item.status)]">
                {{ formatStatus(item.status) }}
              </span>
            </td>
            <td class="metric-num text-muted">{{ item.updateTime || 'Just now' }}</td>
            <td class="actions-cell">
              <button class="pill-action" @click="openTrackingModal(item)">Track</button>
              <button class="pill-action" @click="openDispatchModal(item)">Dispatch</button>
            </td>
          </tr>
          <tr v-if="!shipments.length">
            <td colspan="7" class="empty-state">No shipments currently active.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { deliveryAPI } from '@/api'
import { Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const shipments = ref([])
const statusCounts = reactive({
  pending: 0,
  inTransit: 0,
  delivered: 0
})

const fetchShipments = async () => {
  try {
    const res = await deliveryAPI.getList({ page: 1, pageSize: 20 })
    if (res && res.data) {
      if (Array.isArray(res.data)) {
        shipments.value = res.data
      } else if (res.data.records) {
        shipments.value = res.data.records
      } else if (res.data.content) {
        shipments.value = res.data.content
      }
    }

    const countsRes = await deliveryAPI.getStatusCounts()
    if (countsRes && countsRes.data) {
      Object.assign(statusCounts, countsRes.data)
    }
  } catch (err) {
    console.error('Fetch shipments error:', err)
  }
}

const getStatusPillClass = (status) => {
  if (status === 2 || status === 'DELIVERED') return 'emerald'
  if (status === 1 || status === 'IN_TRANSIT') return 'blue'
  return 'amber'
}

const formatStatus = (status) => {
  if (status === 2 || status === 'DELIVERED') return 'Delivered'
  if (status === 1 || status === 'IN_TRANSIT') return 'In Transit'
  return 'Awaiting Pickup'
}

const openTrackingModal = (item) => {
  ElMessage.info(`Fetching live telemetry for order #${item.orderId || item.id}`)
}

const openDispatchModal = (item) => {
  ElMessage.info(`Carrier dispatch modal for order #${item.orderId || item.id}`)
}

onMounted(() => {
  fetchShipments()
})
</script>

<style scoped>
.logistics-dispatch-view {
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

.status-strip {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.status-stat-card {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-label {
  font-size: 0.78rem;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: 700;
}

.stat-value.amber { color: var(--accent-amber); }
.stat-value.blue { color: var(--accent-cyan); }
.stat-value.emerald { color: var(--accent-emerald); }

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
  letter-spacing: 0.04em;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border-subtle);
}

.fintech-table td {
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-subtle);
  font-size: 0.88rem;
}

.carrier-name {
  font-weight: 600;
}

.tracking-col {
  color: var(--accent-cyan);
  font-weight: 500;
}

.recipient-cell {
  display: flex;
  flex-direction: column;
}

.phone-muted {
  font-size: 0.75rem;
  color: var(--text-muted);
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
  transition: all 0.15s ease;
}

.pill-action:hover {
  color: #fff;
  border-color: rgba(255, 255, 255, 0.2);
}

.empty-state {
  text-align: center;
  padding: 40px !important;
  color: var(--text-muted);
}
</style>
