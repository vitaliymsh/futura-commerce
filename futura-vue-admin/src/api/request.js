import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8086',
  timeout: 15000
})

// Request Interceptor: Attach JWT Token
instance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const token = userStore.token || localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response Interceptor: Token Auto-Refresh & Error Normalization
instance.interceptors.response.use(
  (response) => {
    const userStore = useUserStore()
    const rawHeader = response.headers['authorization'] || response.headers['Authorization']
    const newToken = rawHeader ? rawHeader.replace('Bearer ', '').trim() : null

    if (newToken && newToken !== userStore.token) {
      if (userStore.user) {
        const updatedUser = { ...userStore.user, token: newToken }
        userStore.user = updatedUser
        localStorage.setItem('user', JSON.stringify(updatedUser))
      }
      userStore.token = newToken
      localStorage.setItem('token', newToken)
    }

    return response.data
  },
  (error) => {
    const status = error.response?.status
    const userStore = useUserStore()

    const errorMessages = {
      401: 'Session expired or unauthorized. Please sign in again.',
      403: 'Access denied: insufficient permissions.',
      404: 'Requested resource not found.',
      500: 'Internal server error. Please try again later.'
    }

    let errorMsg = errorMessages[status] || error.response?.data?.message || error.message || 'Network communication error'

    if (status === 401 && !error.config?.url?.includes('/auth/login')) {
      userStore.logout()
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }

    ElMessage.error(errorMsg)
    return Promise.reject(error)
  }
)

export default instance
