<template>
  <div class="goods-catalog-view">
    <!-- Header Controls -->
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>Product Catalog</h2>
        <span class="subtext">Inventory, SKU Matrix & Promotion Allocation</span>
      </div>
      <div class="header-actions">
        <button class="action-btn primary" @click="openCreateModal">
          <el-icon><Plus /></el-icon> New Product
        </button>
      </div>
    </div>

    <!-- Filters Bar -->
    <div class="filters-bar fintech-card">
      <div class="filter-group">
        <input
          v-model="filters.name"
          placeholder="Search by product title..."
          class="fintech-input search"
          @keyup.enter="handleSearch"
        />
        <select v-model="filters.publishStatus" class="fintech-select" @change="handleSearch">
          <option :value="null">All Statuses</option>
          <option :value="1">Published</option>
          <option :value="0">Offline</option>
        </select>
      </div>
      <button class="filter-btn" @click="handleSearch">Filter</button>
    </div>

    <!-- Products Table -->
    <div class="table-container fintech-card">
      <table class="fintech-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Product</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in goodsList" :key="item.id">
            <td class="metric-num text-muted">#{{ item.id }}</td>
            <td class="product-cell">
              <img :src="getProductIcon(item)" class="product-thumb" alt="thumb" />
              <div class="product-info">
                <span class="product-title">{{ item.name }}</span>
                <span class="product-sub">{{ item.description || item.subTitle || 'Standard Catalog Item' }}</span>
              </div>
            </td>
            <td>
              <span class="fintech-pill blue">{{ item.categoryName || getCategoryName(item.categoryId) }}</span>
            </td>
            <td class="metric-num price-col">${{ formatPrice(item.price) }}</td>
            <td class="metric-num">{{ item.stock ?? item.totalStock ?? 0 }}</td>
            <td>
              <span :class="['fintech-pill', item.publishStatus === 1 ? 'emerald' : 'rose']">
                {{ item.publishStatus === 1 ? 'Active' : 'Offline' }}
              </span>
            </td>
            <td class="actions-cell">
              <button class="pill-action" @click="openEditModal(item)">Edit</button>
              <button class="pill-action" @click="toggleStatus(item)">
                {{ item.publishStatus === 1 ? 'Delist' : 'Publish' }}
              </button>
              <button class="pill-action danger" @click="deleteProduct(item.id)">Delete</button>
            </td>
          </tr>
          <tr v-if="!goodsList.length">
            <td colspan="7" class="empty-state">No products found matching criteria.</td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination Footer -->
      <div class="pagination-footer">
        <span class="page-info">Total Records: <strong class="metric-num">{{ total }}</strong></span>
        <div class="page-controls">
          <button :disabled="page <= 1" @click="changePage(page - 1)">Prev</button>
          <span class="current-page metric-num">{{ page }}</span>
          <button :disabled="goodsList.length < pageSize" @click="changePage(page + 1)">Next</button>
        </div>
      </div>
    </div>

    <!-- Product Create/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="product-modal fintech-card">
        <h3>{{ isEditing ? 'Edit Product' : 'Create New Product' }}</h3>
        <p class="modal-sub">Configure inventory specifications, category linkage and base pricing.</p>

        <form @submit.prevent="saveProduct" class="modal-form">
          <div class="form-row">
            <div class="form-group flex-1">
              <label>Product Title</label>
              <input v-model="form.name" required class="fintech-input" placeholder="e.g. Apple iPhone 16 Pro Max" />
            </div>
            <div class="form-group flex-1">
              <label>Category</label>
              <select v-model="form.categoryId" required class="fintech-select">
                <option v-for="cat in categoryOptions" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label>Base Price ($)</label>
              <input v-model.number="form.price" type="number" step="0.01" min="0.01" required class="fintech-input" placeholder="999.00" />
            </div>
            <div class="form-group flex-1">
              <label>Initial Stock Inventory</label>
              <input v-model.number="form.stock" type="number" min="0" required class="fintech-input" placeholder="50" />
            </div>
          </div>

          <div class="form-group">
            <label>Image URL</label>
            <input v-model="form.pic" class="fintech-input" placeholder="https://... or leave empty for default badge" />
          </div>

          <div class="form-group">
            <label>Description / Subtitle</label>
            <textarea v-model="form.description" rows="2" class="fintech-textarea" placeholder="Brief hardware specification and highlights"></textarea>
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label>Publish Status</label>
              <select v-model="form.publishStatus" class="fintech-select">
                <option :value="1">Published (Active)</option>
                <option :value="0">Offline (Draft)</option>
              </select>
            </div>
          </div>

          <div class="modal-actions">
            <button type="button" class="pill-btn secondary" @click="showModal = false">Cancel</button>
            <button type="submit" class="pill-btn primary" :disabled="saving">
              {{ saving ? 'Saving...' : (isEditing ? 'Update Product' : 'Create Product') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { goodsAPI } from '@/api'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductIcon } from '@/utils/productIcon'

const goodsList = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const categoryOptions = ref([
  { id: 7, name: 'Mobile Phones & Telecom' },
  { id: 8, name: 'Computers & Office' },
  { id: 9, name: 'Digital Accessories' },
  { id: 10, name: 'Smart Devices' },
  { id: 11, name: 'Men\'s Apparel' },
  { id: 12, name: 'Women\'s Apparel' },
  { id: 15, name: 'Snacks & Groceries' },
  { id: 19, name: 'Furniture & Living' }
])

const filters = reactive({
  name: '',
  publishStatus: null
})

const showModal = ref(false)
const isEditing = ref(false)
const saving = ref(false)
const editingId = ref(null)

const form = reactive({
  name: '',
  categoryId: 7,
  price: 199.00,
  stock: 50,
  pic: '',
  description: '',
  publishStatus: 1
})

const formatPrice = (p) => {
  const num = Number(p) || 0
  return num.toFixed(2)
}

const getCategoryName = (catId) => {
  const found = categoryOptions.value.find(c => c.id === catId)
  return found ? found.name : 'General'
}

const fetchCategories = async () => {
  try {
    const res = await goodsAPI.getCategories()
    if (res && res.data && Array.isArray(res.data) && res.data.length > 0) {
      const flat = []
      for (const node of res.data) {
        flat.push({ id: node.id, name: node.name })
        if (node.children) {
          for (const sub of node.children) {
            flat.push({ id: sub.id, name: `${node.name} > ${sub.name}` })
          }
        }
      }
      if (flat.length) categoryOptions.value = flat
    }
  } catch (err) {
    console.warn('Failed to load categories tree:', err)
  }
}

const fetchGoods = async () => {
  try {
    const res = await goodsAPI.search({
      page: page.value,
      pageSize: pageSize.value,
      name: filters.name || undefined,
      publishStatus: filters.publishStatus !== null ? filters.publishStatus : undefined
    })
    if (res && res.data) {
      if (Array.isArray(res.data)) {
        goodsList.value = res.data
        total.value = res.data.length
      } else if (res.data.records) {
        goodsList.value = res.data.records
        total.value = res.data.total || res.data.records.length
      } else if (res.data.content) {
        goodsList.value = res.data.content
        total.value = res.data.totalElements || res.data.content.length
      }
    }
  } catch (err) {
    console.error('Fetch products error:', err)
  }
}

const handleSearch = () => {
  page.value = 1
  fetchGoods()
}

const changePage = (newPage) => {
  page.value = newPage
  fetchGoods()
}

const toggleStatus = async (item) => {
  const newStatus = item.publishStatus === 1 ? 0 : 1
  try {
    await goodsAPI.updateStatus(item.id, newStatus)
    item.publishStatus = newStatus
    ElMessage.success(`Product ${newStatus === 1 ? 'activated' : 'delisted'} successfully.`)
  } catch (err) {
    ElMessage.error('Status update failed')
  }
}

const deleteProduct = async (id) => {
  try {
    await ElMessageBox.confirm('Delist and remove this product permanently?', 'Confirm Action', {
      confirmButtonText: 'Confirm Delete',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await goodsAPI.delete(id)
    ElMessage.success('Product removed.')
    fetchGoods()
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('Deletion failed')
    }
  }
}

const openCreateModal = () => {
  isEditing.value = false
  editingId.value = null
  form.name = ''
  form.categoryId = categoryOptions.value[0]?.id || 7
  form.price = 199.00
  form.stock = 50
  form.pic = ''
  form.description = ''
  form.publishStatus = 1
  showModal.value = true
}

const openEditModal = (item) => {
  isEditing.value = true
  editingId.value = item.id
  form.name = item.name || ''
  form.categoryId = item.categoryId || 7
  form.price = Number(item.price) || 199.00
  form.stock = item.stock != null ? item.stock : 50
  form.pic = item.pic || ''
  form.description = item.description || item.subTitle || ''
  form.publishStatus = item.publishStatus != null ? item.publishStatus : 1
  showModal.value = true
}

const saveProduct = async () => {
  saving.value = true
  try {
    const payload = {
      id: isEditing.value ? editingId.value : undefined,
      name: form.name,
      categoryId: form.categoryId,
      price: form.price,
      stock: form.stock,
      pic: form.pic,
      description: form.description,
      publishStatus: form.publishStatus
    }
    await goodsAPI.save(payload)
    ElMessage.success(isEditing.value ? 'Product updated successfully!' : 'Product created successfully!')
    showModal.value = false
    fetchGoods()
  } catch (err) {
    ElMessage.error('Failed to save product: ' + (err.message || 'Server error'))
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  fetchCategories()
  fetchGoods()
})
</script>

<style scoped>
.goods-catalog-view {
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

.filters-bar {
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filter-group {
  display: flex;
  gap: 12px;
}

.fintech-input.search {
  width: 280px;
}

.filter-btn {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  color: var(--text-primary);
  padding: 8px 16px;
  border-radius: var(--radius-pill);
  cursor: pointer;
  font-size: 0.85rem;
  font-weight: 500;
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
  letter-spacing: 0.04em;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border-subtle);
  background: rgba(255, 255, 255, 0.01);
}

.fintech-table td {
  padding: 14px 20px;
  border-bottom: 1px solid var(--border-subtle);
  font-size: 0.88rem;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-thumb {
  width: 42px;
  height: 42px;
  border-radius: var(--radius-sm);
  background: var(--bg-input);
  object-fit: cover;
  border: 1px solid var(--border-subtle);
}

.product-info {
  display: flex;
  flex-direction: column;
}

.product-title {
  font-weight: 600;
}

.product-sub {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.price-col {
  font-weight: 600;
  color: var(--accent-cyan);
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

.pagination-footer {
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.page-info {
  font-size: 0.82rem;
  color: var(--text-muted);
}

.page-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.page-controls button {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  color: #fff;
  border-radius: var(--radius-sm);
  padding: 4px 10px;
  font-size: 0.8rem;
  cursor: pointer;
}

.page-controls button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.current-page {
  font-weight: 600;
  font-size: 0.88rem;
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

.product-modal {
  width: 100%;
  max-width: 540px;
  background: var(--bg-surface);
  border: 1px solid var(--border-card);
  border-radius: var(--radius-lg);
  padding: 30px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-modal h3 {
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
  gap: 14px;
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
  margin-top: 10px;
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
