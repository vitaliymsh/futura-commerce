<template>
  <div class="login-wrapper">
    <div class="login-glass-card">
      <div class="login-brand">
        <div class="brand-badge-red">F</div>
        <h1>Futura Admin</h1>
      </div>

      <form class="login-form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>Operator ID / Username</label>
          <input
            v-model="form.username"
            type="text"
            required
            placeholder="admin"
            class="ym-input"
          />
        </div>

        <div class="form-group">
          <label>Passcode / Security Token</label>
          <input
            v-model="form.password"
            type="password"
            required
            placeholder="••••••••"
            class="ym-input"
          />
        </div>

        <button type="submit" class="submit-btn-yellow" :disabled="loading">
          <span v-if="!loading">Authenticate & Enter</span>
          <span v-else>Verifying Credentials...</span>
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

const handleSubmit = async () => {
  loading.value = true
  try {
    await userStore.login(form)
    ElMessage.success('Welcome back, Operator.')
    router.push('/dashboard')
  } catch (err) {
    ElMessage.error(err.message || 'Authentication rejected')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-wrapper {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle at 50% 20%, #2b2b2b 0%, #1e1e1e 70%);
}

.login-glass-card {
  width: 100%;
  max-width: 420px;
  background: #282828;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 40px;
  box-shadow: var(--shadow-card);
}

.login-brand {
  text-align: center;
  margin-bottom: 32px;
}

.brand-badge-red {
  width: 48px;
  height: 48px;
  margin: 0 auto 16px;
  border-radius: 50%;
  background: var(--accent-red);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 900;
  font-size: 1.4rem;
  color: #fff;
}

.login-brand h1 {
  font-size: 1.35rem;
  font-weight: 800;
  color: #ffffff;
  letter-spacing: -0.02em;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: var(--text-secondary);
}

.ym-input {
  background: #333333;
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
  padding: 12px 14px;
  color: #fff;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.2s;
}

.ym-input:focus {
  border-color: var(--accent-yellow);
}

.submit-btn-yellow {
  margin-top: 10px;
  background-color: var(--accent-yellow);
  border: none;
  border-radius: var(--radius-sm);
  padding: 13px;
  color: #000000;
  font-weight: 700;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background-color 0.15s;
}

.submit-btn-yellow:hover {
  background-color: var(--accent-yellow-hover);
}

.submit-btn-yellow:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
