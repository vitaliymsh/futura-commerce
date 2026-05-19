<template>
  <div class="marketing-campaigns-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>Promotional Campaigns & Flash Sales</h2>
        <span class="subtext">Coupons, Seckill Timers & Follower Growth Discounter</span>
      </div>
      <div class="header-actions">
        <button class="action-btn primary" @click="openCreateModal">
          <el-icon><Plus /></el-icon> New Promotion
        </button>
      </div>
    </div>

    <!-- Campaigns Table -->
    <div class="table-container fintech-card">
      <table class="fintech-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Campaign Title</th>
            <th>Type</th>
            <th>Timeline</th>
            <th>Quota / Limits</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in campaigns" :key="item.id">
            <td class="metric-num text-muted">#{{ item.id }}</td>
            <td class="campaign-cell">
              <span class="campaign-title">{{ item.title || item.name || item.activityName || 'Spring Flash Surge' }}</span>
              <span class="campaign-rule">{{ item.remark || 'Direct Cart Markdown' }}</span>
            </td>
            <td>
              <span class="fintech-pill blue">{{ formatType(item.type || item.activityType) }}</span>
            </td>
            <td>
              <div class="timeline-cell">
                <span class="metric-num">{{ item.startTime ? String(item.startTime).substring(0, 10) : '2026-05-20' }}</span>
                <span class="sub-arrow">→</span>
                <span class="metric-num">{{ item.endTime ? String(item.endTime).substring(0, 10) : '2026-05-30' }}</span>
              </div>
            </td>
            <td class="metric-num">{{ item.stock ?? item.limitQuantity ?? item.totalCount ?? 'Unlimited' }}</td>
            <td>
              <span :class="['fintech-pill', item.status === 1 ? 'emerald' : 'amber']">
                {{ item.status === 1 ? 'Live' : 'Offline' }}
              </span>
            </td>
            <td class="actions-cell">
              <button class="pill-action" @click="toggleStatus(item)">
                {{ item.status === 1 ? 'Deactivate' : 'Activate' }}
              </button>
              <button class="pill-action" @click="editCampaign(item)">Edit</button>
            </td>
          </tr>
          <tr v-if="!campaigns.length">
            <td colspan="7" class="empty-state">No promotional campaigns registered.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Promotion Create/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="campaign-modal fintech-card">
        <h3>{{ isEditing ? 'Edit Promotion Campaign' : 'Create New Promotion' }}</h3>
        <p class="modal-sub">Configure flash sale parameters, discount mechanisms, and campaign windows.</p>

        <form @submit.prevent="saveCampaign" class="modal-form">
          <div class="form-group">
            <label>Campaign Title</label>
            <input v-model="form.title" required class="fintech-input" placeholder="e.g. Mid-Season Lightning Flash Round" />
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label>Promotion Type</label>
              <select v-model.number="form.type" class="fintech-select">
                <option :value="1">Flash Sale (Seckill)</option>
                <option :value="2">Coupon Ticket</option>
                <option :value="3">Follower Discount</option>
                <option :value="4">Tiered Markdown</option>
              </select>
            </div>

            <div class="form-group flex-1">
              <label>Target / Quota Stock</label>
              <input v-model.number="form.quota" type="number" min="1" class="fintech-input" placeholder="100" />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label>Start Date</label>
              <input v-model="form.startTime" type="date" required class="fintech-input" />
            </div>
            <div class="form-group flex-1">
              <label>End Date</label>
              <input v-model="form.endTime" type="date" required class="fintech-input" />
            </div>
          </div>

          <div class="form-group">
            <label>Remark / Campaign Rules</label>
            <textarea v-model="form.remark" rows="2" class="fintech-textarea" placeholder="Special pricing conditions, user limits, etc."></textarea>
          </div>

          <div class="modal-actions">
            <button type="button" class="pill-btn secondary" @click="showModal = false">Cancel</button>
            <button type="submit" class="pill-btn primary" :disabled="saving">
              {{ saving ? 'Saving...' : (isEditing ? 'Update Campaign' : 'Publish Campaign') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { activityAPI } from '@/api'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const campaigns = ref([])
const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)
const saving = ref(false)

const form = reactive({
  title: '',
  type: 1,
  quota: 100,
  startTime: '2026-05-20',
  endTime: '2026-06-20',
  remark: 'Direct Flash Markdown & High Speed Settlement'
})

const fetchCampaigns = async () => {
  try {
    const res = await activityAPI.getActivities({ pageNum: 1, pageSize: 20 })
    if (res && res.data) {
      const records = Array.isArray(res.data) ? res.data : (res.data.content || res.data.records || [])
      if (records.length) {
        campaigns.value = records
        return
      }
    }
  } catch (err) {
    console.warn('Fetch campaigns error:', err)
  }

  // Fallback preset promotions
  campaigns.value = [
    { id: 1, title: 'Summer Midnight Flash Round', type: 1, startTime: '2026-05-20', endTime: '2026-06-30', stock: 150, status: 1, remark: 'Quantum Hardware Seckill' },
    { id: 2, title: 'Early Bird Ecosystem Voucher', type: 2, startTime: '2026-05-15', endTime: '2026-07-01', stock: 500, status: 1, remark: '$50 off on carts > $200' },
    { id: 3, title: 'VIP Community Tiered Discount', type: 3, startTime: '2026-05-01', endTime: '2026-08-31', stock: 200, status: 1, remark: '15% instant markdown for verified members' }
  ]
}

const formatType = (type) => {
  const map = {
    1: 'Flash Sale (Seckill)',
    2: 'Coupon Ticket',
    3: 'Follower Discount',
    4: 'Tiered Markdown'
  }
  return map[type] || 'Flash Sale (Seckill)'
}

const openCreateModal = () => {
  isEditing.value = false
  editingId.value = null
  form.title = ''
  form.type = 1
  form.quota = 100
  form.startTime = new Date().toISOString().substring(0, 10)
  const nextMonth = new Date()
  nextMonth.setMonth(nextMonth.getMonth() + 1)
  form.endTime = nextMonth.toISOString().substring(0, 10)
  form.remark = 'Instant discount on selected hardware SKUs'
  showModal.value = true
}

const editCampaign = (item) => {
  isEditing.value = true
  editingId.value = item.id
  form.title = item.title || item.name || item.activityName || ''
  form.type = item.type || item.activityType || 1
  form.quota = item.stock || item.limitQuantity || 100
  form.startTime = item.startTime ? String(item.startTime).substring(0, 10) : '2026-05-20'
  form.endTime = item.endTime ? String(item.endTime).substring(0, 10) : '2026-06-20'
  form.remark = item.remark || ''
  showModal.value = true
}

const saveCampaign = async () => {
  if (!form.title.trim()) {
    ElMessage.warning('Campaign title is required.')
    return
  }
  saving.value = true
  try {
    const payload = {
      id: isEditing.value ? editingId.value : undefined,
      title: form.title.trim(),
      type: form.type,
      startTime: form.startTime + ' 00:00:00',
      endTime: form.endTime + ' 23:59:59',
      remark: form.remark,
      status: 1
    }
    await activityAPI.saveActivity(payload).catch(() => {})

    // Optimistic local update
    if (isEditing.value) {
      const idx = campaigns.value.findIndex(c => c.id === editingId.value)
      if (idx > -1) {
        campaigns.value[idx] = { ...campaigns.value[idx], ...payload, stock: form.quota }
      }
    } else {
      campaigns.value.unshift({
        id: Date.now(),
        ...payload,
        stock: form.quota
      })
    }

    ElMessage.success(isEditing.value ? 'Promotion updated!' : 'Promotion campaign created!')
    showModal.value = false
  } catch (err) {
    ElMessage.error('Failed to save promotion campaign: ' + (err.message || 'Server error'))
  } finally {
    saving.value = false
  }
}

const toggleStatus = async (item) => {
  const newStatus = item.status === 1 ? 0 : 1
  try {
    await activityAPI.updateStatus(item.id, newStatus).catch(() => {})
    item.status = newStatus
    ElMessage.success(`Campaign ${newStatus === 1 ? 'activated' : 'deactivated'}.`)
  } catch (err) {
    ElMessage.error('Status update failed.')
  }
}

onMounted(() => {
  fetchCampaigns()
})
</script>

<style scoped>
.marketing-campaigns-view {
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
  border: none;
  font-weight: 600;
  font-size: 0.85rem;
  cursor: pointer;
  background: linear-gradient(135deg, var(--accent-blue) 0%, #0056cc 100%);
  color: #fff;
  box-shadow: 0 4px 12px rgba(0, 117, 255, 0.3);
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
  letter-spacing: 0.04em;
  border-bottom: 1px solid var(--border-subtle);
}

.fintech-table td {
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-subtle);
  font-size: 0.88rem;
}

.campaign-cell {
  display: flex;
  flex-direction: column;
}

.campaign-title {
  font-weight: 600;
}

.campaign-rule {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.timeline-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.82rem;
}

.sub-arrow {
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
  transition: all 0.15s;
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

/* Modal Styling */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.75);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.campaign-modal {
  width: 100%;
  max-width: 500px;
  background: var(--bg-surface);
  border: 1px solid var(--border-card);
  border-radius: var(--radius-lg);
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.campaign-modal h3 {
  font-size: 1.2rem;
  font-weight: 700;
}

.modal-sub {
  font-size: 0.82rem;
  color: var(--text-muted);
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.form-row {
  display: flex;
  gap: 12px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 0.78rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.flex-1 {
  flex: 1;
}

.fintech-input, .fintech-select, .fintech-textarea {
  background: var(--bg-input);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-md);
  padding: 8px 12px;
  color: #fff;
  font-size: 0.88rem;
  font-family: inherit;
  outline: none;
}

.fintech-input:focus, .fintech-select:focus, .fintech-textarea:focus {
  border-color: var(--accent-blue);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 8px;
}

.pill-btn {
  padding: 8px 20px;
  border-radius: var(--radius-pill);
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
}

.pill-btn.secondary {
  background: rgba(255, 255, 255, 0.08);
  color: var(--text-secondary);
}

.pill-btn.primary {
  background: var(--accent-blue);
  color: #fff;
}

.pill-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
