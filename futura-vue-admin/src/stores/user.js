import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/api/request'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || '')

  const initUser = () => {
    const stored = localStorage.getItem('user')
    if (stored) {
      try {
        user.value = JSON.parse(stored)
      } catch (e) {
        localStorage.removeItem('user')
      }
    }
  }

  const login = async (credentials) => {
    try {
      const res = await request.post('/auth/login', {
        username: credentials.username,
        password: credentials.password
      })

      if (res && res.code === 200) {
        const tokenValue = (typeof res.data === 'string' ? res.data : res.data?.token) || ''
        const userObj = {
          username: credentials.username,
          token: tokenValue,
          role: 'ADMIN'
        }
        user.value = userObj
        token.value = tokenValue
        localStorage.setItem('user', JSON.stringify(userObj))
        localStorage.setItem('token', tokenValue)
        return res
      } else {
        throw new Error(res.message || 'Authentication failed')
      }
    } catch (err) {
      console.error('Sign-in error:', err)
      throw err
    }
  }

  const logout = () => {
    user.value = null
    token.value = ''
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => user.value?.username || 'Executive Admin')

  return {
    user,
    token,
    isLoggedIn,
    username,
    login,
    logout,
    initUser
  }
})
