import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/api/request'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('user_token') || '')

  const initUser = () => {
    const stored = localStorage.getItem('user_session')
    if (stored) {
      try {
        user.value = JSON.parse(stored)
      } catch (e) {
        localStorage.removeItem('user_session')
      }
    }
  }

  const login = async (credentials) => {
    try {
      const res = await request.post('/user/login', {
        username: credentials.username,
        password: credentials.password
      })

      if (res && res.code === 200) {
        const tokenVal = res.data?.token || res.message || ''
        const userObj = {
          id: res.data?.id || 1,
          username: credentials.username,
          nickname: res.data?.nickname || credentials.username,
          token: tokenVal
        }
        user.value = userObj
        token.value = tokenVal
        localStorage.setItem('user_session', JSON.stringify(userObj))
        localStorage.setItem('user_token', tokenVal)
        return res
      } else {
        throw new Error(res.message || 'Login failed')
      }
    } catch (err) {
      console.error('Customer login error:', err)
      throw err
    }
  }

  const logout = () => {
    user.value = null
    token.value = ''
    localStorage.removeItem('user_session')
    localStorage.removeItem('user_token')
  }

  const isLoggedIn = computed(() => !!token.value)

  return {
    user,
    token,
    isLoggedIn,
    login,
    logout,
    initUser
  }
})
