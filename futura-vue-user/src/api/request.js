import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081',
  timeout: 15000
})

instance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const token = userStore.token || localStorage.getItem('user_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

instance.interceptors.response.use(
  (response) => {
    const userStore = useUserStore()
    const rawHeader = response.headers['authorization'] || response.headers['Authorization']
    const newToken = rawHeader ? rawHeader.replace('Bearer ', '').trim() : null

    if (newToken && newToken !== userStore.token) {
      if (userStore.user) {
        const updatedUser = { ...userStore.user, token: newToken }
        userStore.user = updatedUser
        localStorage.setItem('user_session', JSON.stringify(updatedUser))
      }
      userStore.token = newToken
      localStorage.setItem('user_token', newToken)
    }

    return response.data
  },
  (error) => {
    const status = error.response?.status
    const userStore = useUserStore()

    if (status === 401) {
      userStore.logout()
    }

    const msg = error.response?.data?.message || error.message || 'Network communication issue'
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export default instance
