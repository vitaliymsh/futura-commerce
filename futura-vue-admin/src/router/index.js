import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Layout from '@/layouts/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Analytics Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'goods/list',
        name: 'Product Catalog',
        component: () => import('@/views/GoodsList.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'goods/category',
        name: 'Category Tree',
        component: () => import('@/views/GoodsCategory.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'delivery',
        name: 'Logistics Dispatch',
        component: () => import('@/views/Delivery.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'activity',
        name: 'Promotional Campaigns',
        component: () => import('@/views/Activity.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'after-sale',
        name: 'After-Sales Resolution',
        component: () => import('@/views/AfterSale.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'ai-copilot',
        name: 'Futura Copilot',
        component: () => import('@/views/AIService.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (!userStore.user) {
    userStore.initUser()
  }

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
