import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import request from '@/api/request'

export const useCartStore = defineStore('cart', () => {
  const initialItems = (() => {
    try {
      const saved = localStorage.getItem('futura_cart')
      return saved ? JSON.parse(saved) : []
    } catch (e) {
      return []
    }
  })()

  const items = ref(initialItems)

  const saveCart = () => {
    try {
      localStorage.setItem('futura_cart', JSON.stringify(items.value))
    } catch (e) {
      console.error('Failed to save cart to localStorage:', e)
    }
  }

  watch(items, saveCart, { deep: true })

  const fetchCart = async () => {
    try {
      const res = await request.get('/user/cart/list')
      if (res && res.data && Array.isArray(res.data) && res.data.length > 0) {
        items.value = res.data
        saveCart()
      }
    } catch (err) {
      console.error('Fetch cart error:', err)
    }
  }

  const addItem = (product, quantity = 1, sku = null) => {
    const qty = Math.max(1, Number(quantity) || 1)
    const itemId = sku?.id || product.id || Date.now()
    const existingIndex = items.value.findIndex(
      (i) => (sku?.id && i.skuId === sku.id) || (!sku?.id && i.productId === product.id) || i.id === itemId
    )

    if (existingIndex > -1) {
      items.value[existingIndex].quantity = (items.value[existingIndex].quantity || 1) + qty
    } else {
      const picUrl = product.pic && !product.pic.includes('xxx') ? product.pic : '/favicon.svg'
      items.value.push({
        id: itemId,
        productId: product.id || product.productId,
        skuId: sku?.id || null,
        productName: product.name || product.productName || 'Product',
        skuCode: sku?.spData || sku?.skuCode || 'Standard Spec',
        price: Number(sku?.price || product.price || 0),
        pic: picUrl,
        quantity: qty,
        selected: 1
      })
    }
    saveCart()
  }

  const removeItem = (id) => {
    items.value = items.value.filter((i) => i.id !== id)
    saveCart()
  }

  const updateQuantity = (itemOrId, delta) => {
    const targetId = typeof itemOrId === 'object' ? itemOrId.id : itemOrId
    const item = items.value.find((i) => i.id === targetId)
    if (item) {
      const current = item.quantity || 1
      if (current + delta >= 1) {
        item.quantity = current + delta
        saveCart()
      }
    }
  }

  const toggleSelect = (itemOrId) => {
    const targetId = typeof itemOrId === 'object' ? itemOrId.id : itemOrId
    const item = items.value.find((i) => i.id === targetId)
    if (item) {
      item.selected = item.selected === 1 ? 0 : 1
      saveCart()
    }
  }

  const clearCart = () => {
    items.value = []
    saveCart()
  }

  const count = computed(() => items.value.reduce((acc, item) => acc + (item.quantity || 1), 0))
  const totalPrice = computed(() =>
    items.value
      .filter((i) => i.selected === 1)
      .reduce((acc, item) => acc + (item.price || 0) * (item.quantity || 1), 0)
  )

  return {
    items,
    count,
    totalPrice,
    fetchCart,
    addItem,
    removeItem,
    updateQuantity,
    toggleSelect,
    clearCart
  }
})
