<template>
  <div class="user-login-view-ym">
    <div class="login-card-ym">
      <div class="brand-badge-red">F</div>
      <h2>Customer Sign In</h2>
      <p class="subtitle">Access member prices, personalized recommendations, and order tracking</p>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Username / Member Phone</label>
          <input v-model="form.username" type="text" required placeholder="customer1" class="ym-input" />
        </div>

        <div class="form-group">
          <label>Password</label>
          <input v-model="form.password" type="password" required placeholder="••••••••" class="ym-input" />
        </div>

        <button type="submit" class="submit-btn-yellow" :disabled="loading">
          <span v-if="!loading">Sign In to Futura</span>
          <span v-else>Connecting...</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    await userStore.login(form)
    ElMessage.success('Welcome back to Futura.')
    router.push('/')
  } catch (err) {
    ElMessage.error(err.message || 'Login failed')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.user-login-view-ym {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.login-card-ym {
  width: 100%;
  max-width: 400px;
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.brand-badge-red {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--accent-red);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 1.4rem;
  color: #fff;
  margin-bottom: 16px;
}

.login-card-ym h2 {
  font-size: 1.4rem;
  font-weight: 800;
  color: #ffffff;
}

.subtitle {
  font-size: 0.82rem;
  color: var(--text-secondary);
  margin-top: 6px;
  margin-bottom: 24px;
}

.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  text-align: left;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 0.75rem;
  color: var(--text-secondary);
  font-weight: 600;
}

.ym-input {
  background: #333333;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
  padding: 12px 14px;
  color: #fff;
  outline: none;
  font-size: 0.92rem;
}

.ym-input:focus {
  border-color: var(--accent-yellow);
}

.submit-btn-yellow {
  margin-top: 8px;
  background-color: var(--accent-yellow);
  color: #000000;
  border: none;
  border-radius: var(--radius-sm);
  padding: 13px;
  font-weight: 700;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background-color 0.15s;
}

.submit-btn-yellow:hover {
  background-color: var(--accent-yellow-hover);
}
</style>
