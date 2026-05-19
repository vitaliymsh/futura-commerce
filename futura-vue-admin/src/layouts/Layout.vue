<template>
  <div class="fintech-app-layout">
    <aside class="fintech-sidebar">
      <div class="brand-header">
        <div class="brand-badge-wrap">
          <div class="brand-badge">F</div>
          <span class="pulse-indicator"></span>
        </div>
        <div class="brand-info">
          <span class="brand-title">Futura Admin</span>
          <span class="brand-subtitle">Operations Console</span>
        </div>
      </div>

      <div class="sidebar-glance">
        <div class="glance-item">
          <span class="glance-label">System Mesh</span>
          <span class="fintech-pill emerald">Consul Active</span>
        </div>
      </div>

      <nav class="nav-container">
        <div class="nav-section-label">Core Operations</div>
        <router-link
          v-for="item in primaryRoutes"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          active-class="active"
        >
          <div class="nav-icon-box">
            <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
          </div>
          <span class="nav-label">{{ item.title }}</span>
          <span v-if="item.badge" class="nav-pill-badge">{{ item.badge }}</span>
        </router-link>

        <div class="nav-section-label mt-4">Fulfillment & Growth</div>
        <router-link
          v-for="item in secondaryRoutes"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          active-class="active"
        >
          <div class="nav-icon-box">
            <el-icon class="nav-icon"><component :is="item.icon" /></el-icon>
          </div>
          <span class="nav-label">{{ item.title }}</span>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <div class="user-meta">
          <div class="user-avatar">{{ userInitial }}</div>
          <div class="user-details">
            <span class="username">{{ userStore.username }}</span>
            <span class="user-role">Super Admin</span>
          </div>
        </div>
        <button class="logout-btn" @click="handleLogout" title="Sign Out">
          <el-icon><SwitchButton /></el-icon>
        </button>
      </div>
    </aside>

    <main class="fintech-workspace">
      <header class="workspace-header">
        <div class="header-left-bar">
          <span class="env-pill">Live Cluster</span>
          <span class="route-slash">/</span>
          <span class="current-view">{{ route.name || 'Console' }}</span>
        </div>

        <div class="header-actions">
          <router-link to="/ai-copilot" class="ai-button-pill">
            <span class="pulse-dot"></span>
            Futura Copilot Active
          </router-link>
        </div>
      </header>

      <div class="workspace-body">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'
import { SwitchButton } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const primaryRoutes = computed(() =>
  permissionStore.routes.slice(0, 3)
)

const secondaryRoutes = computed(() =>
  permissionStore.routes.slice(3)
)

const userInitial = computed(() => (userStore.username ? userStore.username.charAt(0).toUpperCase() : 'A'))

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.fintech-app-layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  overflow: hidden;
}

.fintech-sidebar {
  width: 270px;
  background-color: var(--bg-secondary);
  border-right: 1px solid var(--border-subtle);
  display: flex;
  flex-direction: column;
  padding: 24px 16px;
  z-index: 10;
}

.brand-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 8px 20px 8px;
  border-bottom: 1px solid var(--border-subtle);
}

.brand-badge-wrap {
  position: relative;
}

.brand-badge {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: var(--accent-red);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 1.2rem;
  color: #fff;
}

.pulse-indicator {
  position: absolute;
  top: -2px;
  right: -2px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--accent-green);
  border: 2px solid var(--bg-secondary);
}

.brand-info {
  display: flex;
  flex-direction: column;
}

.brand-title {
  font-size: 1.15rem;
  font-weight: 800;
  letter-spacing: -0.02em;
  color: #ffffff;
}

.brand-subtitle {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-secondary);
}

.sidebar-glance {
  margin: 16px 8px 8px;
  padding: 10px 12px;
  background: #333333;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-md);
}

.glance-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.glance-label {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.nav-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-top: 14px;
  flex: 1;
  overflow-y: auto;
}

.nav-section-label {
  font-size: 0.68rem;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.08em;
  font-weight: 700;
  padding: 8px 12px 4px;
}

.mt-4 {
  margin-top: 14px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 0.88rem;
  font-weight: 500;
  transition: all 0.15s ease;
  position: relative;
}

.nav-icon-box {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-sm);
  background: #333333;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s;
}

.nav-item:hover .nav-icon-box {
  background: #3f3f3f;
  color: #fff;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.04);
  color: var(--text-primary);
}

.nav-item.active {
  background-color: #333333;
  color: #ffffff;
  font-weight: 700;
  border-left: 3px solid var(--accent-yellow);
}

.nav-item.active .nav-icon-box {
  background: var(--accent-yellow);
  color: #000000;
}

.nav-icon {
  font-size: 1rem;
}

.nav-pill-badge {
  margin-left: auto;
  background: var(--accent-yellow);
  color: #000;
  font-size: 0.7rem;
  font-weight: 800;
  padding: 2px 6px;
  border-radius: var(--radius-pill);
}

.sidebar-footer {
  padding-top: 16px;
  border-top: 1px solid var(--border-subtle);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: var(--radius-pill);
  background: #333333;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.85rem;
  color: #ffffff;
  border: 1px solid var(--border-subtle);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 0.85rem;
  font-weight: 600;
  color: #ffffff;
}

.user-role {
  font-size: 0.7rem;
  color: var(--text-muted);
}

.logout-btn {
  background: transparent;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  padding: 8px;
  border-radius: var(--radius-sm);
  transition: all 0.15s ease;
}

.logout-btn:hover {
  color: var(--accent-red);
  background: var(--accent-red-bg);
}

.fintech-workspace {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.workspace-header {
  height: 64px;
  border-bottom: 1px solid var(--border-subtle);
  padding: 0 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #212121;
}

.header-left-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.route-slash {
  color: var(--text-muted);
  font-size: 0.85rem;
}

.env-pill {
  background: var(--accent-green-bg);
  color: var(--accent-green);
  padding: 3px 10px;
  border-radius: var(--radius-pill);
  font-size: 0.72rem;
  font-weight: 700;
}

.current-view {
  font-weight: 700;
  font-size: 1rem;
  color: #ffffff;
}

.ai-button-pill {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  border-radius: var(--radius-pill);
  background: #333333;
  border: 1px solid var(--border-subtle);
  color: #ffffff;
  text-decoration: none;
  font-size: 0.82rem;
  font-weight: 600;
  transition: all 0.2s ease;
}

.ai-button-pill:hover {
  background: #3f3f3f;
  border-color: var(--accent-yellow);
}

.pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background-color: var(--accent-yellow);
  box-shadow: 0 0 8px var(--accent-yellow);
}

.workspace-body {
  flex: 1;
  padding: 32px;
  overflow-y: auto;
  background-color: #222222;
}
</style>
