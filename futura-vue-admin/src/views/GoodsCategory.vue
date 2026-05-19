<template>
  <div class="category-tree-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>Category Hierarchy</h2>
        <span class="subtext">3-Tier Product Classification & Taxonomy</span>
      </div>
      <div class="header-actions">
        <button class="action-btn primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon> New Category
        </button>
      </div>
    </div>

    <!-- Category Tree Cards -->
    <div class="category-grid">
      <div v-for="cat in categories" :key="cat.id" class="category-card fintech-card">
        <div class="cat-header">
          <div class="cat-badge">{{ cat.name ? cat.name.charAt(0).toUpperCase() : 'C' }}</div>
          <div class="cat-title-group">
            <span class="cat-name">{{ cat.name }}</span>
            <span class="cat-level fintech-pill blue">Tier {{ cat.parentId ? '2 (Sub)' : '1 (Root)' }}</span>
          </div>
        </div>

        <div class="cat-meta">
          <span class="text-muted">ID: <strong class="metric-num">#{{ cat.id }}</strong></span>
          <span class="text-muted">Sort: <strong class="metric-num">{{ cat.sort ?? 0 }}</strong></span>
        </div>

        <!-- Sub-categories if present -->
        <div v-if="cat.children && cat.children.length" class="subcategories-list">
          <span class="sub-heading">Subcategories ({{ cat.children.length }}):</span>
          <div class="sub-pills">
            <span
              v-for="sub in cat.children"
              :key="sub.id"
              class="sub-pill clickable"
              @click="editCategory(sub, cat.id)"
              title="Click to edit subcategory"
            >
              {{ sub.name }} <small>✎</small>
            </span>
          </div>
        </div>

        <div class="card-actions">
          <button class="pill-action" @click="editCategory(cat, 0)">Edit</button>
          <button class="pill-action danger" @click="deleteCategory(cat.id)">Delete</button>
        </div>
      </div>

      <div v-if="!categories.length" class="empty-state fintech-card">
        No categories found. Click "New Category" to create one.
      </div>
    </div>

    <!-- Category Add/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="category-modal fintech-card">
        <h3>{{ isEditing ? 'Edit Category' : 'Create New Category' }}</h3>
        <p class="modal-sub">Configure category classification hierarchy and ordering.</p>

        <form @submit.prevent="saveCategory" class="modal-form">
          <div class="form-group">
            <label>Category Name</label>
            <input v-model="form.name" required class="fintech-input" placeholder="e.g. Neural Accelerators" />
          </div>

          <div class="form-row">
            <div class="form-group flex-1">
              <label>Parent Tier</label>
              <select v-model="form.parentId" class="fintech-select">
                <option :value="0">Top-Level Category (Tier 1)</option>
                <option
                  v-for="parent in rootCategories"
                  :key="parent.id"
                  :value="parent.id"
                  :disabled="isEditing && editingId === parent.id"
                >
                  Under {{ parent.name }}
                </option>
              </select>
            </div>

            <div class="form-group flex-1">
              <label>Sort Order</label>
              <input v-model.number="form.sort" type="number" min="0" class="fintech-input" placeholder="1" />
            </div>
          </div>

          <div class="form-group">
            <label>Icon URL / Key</label>
            <input v-model="form.icon" class="fintech-input" placeholder="Optional icon link" />
          </div>

          <div class="modal-actions">
            <button type="button" class="pill-btn secondary" @click="showModal = false">Cancel</button>
            <button type="submit" class="pill-btn primary" :disabled="saving">
              {{ saving ? 'Saving...' : (isEditing ? 'Update Category' : 'Create Category') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { goodsAPI } from '@/api'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const categories = ref([])
const showModal = ref(false)
const isEditing = ref(false)
const editingId = ref(null)
const saving = ref(false)

const form = reactive({
  name: '',
  parentId: 0,
  sort: 1,
  icon: ''
})

const rootCategories = computed(() => {
  return categories.value.filter(c => !c.parentId || c.parentId === 0)
})

const fetchCategories = async () => {
  try {
    const res = await goodsAPI.getCategories()
    if (res && res.data) {
      categories.value = res.data
    }
  } catch (err) {
    console.error('Fetch categories error:', err)
  }
}

const openAddDialog = () => {
  isEditing.value = false
  editingId.value = null
  form.name = ''
  form.parentId = 0
  form.sort = (categories.value.length || 0) + 1
  form.icon = ''
  showModal.value = true
}

const editCategory = (cat, defaultParentId = 0) => {
  isEditing.value = true
  editingId.value = cat.id
  form.name = cat.name || ''
  form.parentId = cat.parentId != null ? cat.parentId : defaultParentId
  form.sort = cat.sort || 1
  form.icon = cat.icon || ''
  showModal.value = true
}

const saveCategory = async () => {
  if (!form.name.trim()) {
    ElMessage.warning('Category name cannot be blank.')
    return
  }
  saving.value = true
  try {
    const payload = {
      id: isEditing.value ? editingId.value : undefined,
      name: form.name.trim(),
      parentId: form.parentId || 0,
      sort: form.sort || 1,
      icon: form.icon || ''
    }

    if (isEditing.value) {
      await goodsAPI.updateCategory(editingId.value, payload)
      ElMessage.success('Category updated successfully!')
    } else {
      await goodsAPI.addCategory(payload)
      ElMessage.success('Category created successfully!')
    }
    showModal.value = false
    fetchCategories()
  } catch (err) {
    ElMessage.error('Failed to save category: ' + (err.message || 'Server error'))
  } finally {
    saving.value = false
  }
}

const deleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('Delete category node and unlink products?', 'Confirm Delete', {
      type: 'warning'
    })
    await goodsAPI.deleteCategory(id)
    ElMessage.success('Category node removed.')
    fetchCategories()
  } catch (err) {
    if (err !== 'cancel') ElMessage.error('Delete failed: ' + (err.message || 'Error'))
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-tree-view {
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

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.category-card {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cat-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.cat-badge {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  background: var(--bg-card);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1rem;
  color: var(--accent-cyan);
  border: 1px solid var(--border-subtle);
}

.cat-title-group {
  display: flex;
  flex-direction: column;
}

.cat-name {
  font-weight: 600;
  font-size: 1rem;
}

.cat-level {
  font-size: 0.7rem;
  margin-top: 2px;
}

.cat-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.82rem;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-subtle);
}

.subcategories-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.sub-heading {
  font-size: 0.72rem;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.sub-pills {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.sub-pill {
  background: var(--bg-input);
  padding: 3px 8px;
  border-radius: var(--radius-sm);
  font-size: 0.75rem;
  border: 1px solid var(--border-subtle);
  color: var(--text-secondary);
}

.sub-pill.clickable {
  cursor: pointer;
  transition: all 0.15s;
}

.sub-pill.clickable:hover {
  background: rgba(0, 117, 255, 0.15);
  border-color: var(--accent-blue);
  color: #fff;
}

.card-actions {
  display: flex;
  gap: 8px;
  margin-top: auto;
}

.pill-action {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-subtle);
  color: var(--text-secondary);
  border-radius: var(--radius-pill);
  padding: 4px 12px;
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
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px;
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

.category-modal {
  width: 100%;
  max-width: 460px;
  background: var(--bg-surface);
  border: 1px solid var(--border-card);
  border-radius: var(--radius-lg);
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.category-modal h3 {
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

.fintech-input, .fintech-select {
  background: var(--bg-input);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-md);
  padding: 8px 12px;
  color: #fff;
  font-size: 0.88rem;
  outline: none;
}

.fintech-input:focus, .fintech-select:focus {
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
