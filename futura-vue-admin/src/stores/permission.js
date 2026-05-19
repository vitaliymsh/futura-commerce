import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePermissionStore = defineStore('permission', () => {
  const routes = ref([
    { path: '/dashboard', title: 'Analytics', icon: 'TrendCharts' },
    { path: '/goods/list', title: 'Catalog', icon: 'Goods' },
    { path: '/goods/category', title: 'Categories', icon: 'Menu' },
    { path: '/delivery', title: 'Logistics', icon: 'Van' },
    { path: '/activity', title: 'Marketing', icon: 'Present' },
    { path: '/after-sale', title: 'After-Sales', icon: 'Service' },
    { path: '/ai-copilot', title: 'Futura Copilot', icon: 'Cpu' }
  ])

  return {
    routes
  }
})
