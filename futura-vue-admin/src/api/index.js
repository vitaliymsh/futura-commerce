import request from './request'

// Authentication
export const authAPI = {
  login: (username, password) =>
    request.post('/auth/login', { username, password })
}

// Product & SKU Catalog
export const goodsAPI = {
  getList: (params) =>
    request.get('/Pms_promotion/goodList', { params }),

  getPagination: (page, pageSize) =>
    request.get('/Pms_promotion/goodsPagination', { params: { page, pageSize } }),

  search: (params) =>
    request.get('/Pms_promotion/search', { params }),

  save: (data) =>
    request.put('/Pms_promotion/save', data),

  updateStatus: (id, status) =>
    request.put(`/Pms_promotion/status/${id}/${status}`),

  updateProduct: (id, data) =>
    request.put(`/Pms_promotion/updateProduct/${id}`, data),

  delete: (id) =>
    request.delete(`/Pms_promotion/delete/${id}`),

  uploadImage: (formData) =>
    request.post('/Pms_promotion/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }),

  // Categories
  getCategories: () =>
    request.get('/goods/categories'),

  addCategory: (data) =>
    request.post('/goods/categories/add', data),

  updateCategory: (id, data) =>
    request.put(`/goods/categories/update/${id}`, data),

  deleteCategory: (id) =>
    request.delete(`/goods/categories/del/${id}`),

  // SKU Management
  getSkuList: (page, pageSize) =>
    request.get('/Sku/list', { params: { page, pageSize } }),

  searchSku: (data) =>
    request.post('/Sku/search', data),

  saveSku: (id, data) =>
    request.put(`/Sku/save/${id}`, data),

  deleteSku: (skuIds) =>
    request.post('/Sku/del', { ids: skuIds })
}

// Logistics & Carrier Dispatch
export const deliveryAPI = {
  getList: (params) =>
    request.get('/delivery/list', { params }),

  search: (data) =>
    request.post('/delivery/search', data),

  getCarriers: () =>
    request.get('/delivery/company/list'),

  getStatusCounts: () =>
    request.get('/delivery/status'),

  shipOrder: (orderId, data) =>
    request.post(`/delivery/ship/${orderId}`, data),

  updateTracking: (orderId, data) =>
    request.put(`/delivery/tracking/${orderId}`, data),

  cancelDelivery: (orderId, data) =>
    request.post(`/delivery/cancel/${orderId}`, data),

  getTraceList: (pageNum, pageSize) =>
    request.get('/deliveryTrace/list', { params: { pageNum, pageSize } }),

  getLogistics: (orderId) =>
    request.get(`/deliveryTrace/logistics/${orderId}`),

  updateTrace: (data) =>
    request.post('/deliveryTrace/update', data),

  exportLogistics: (data) =>
    request.post('/deliveryTrace/export', data, { responseType: 'blob' })
}

// Order Management & After-Sales
export const orderAPI = {
  getList: (params) =>
    request.get('/order/list', { params }),

  search: (data) =>
    request.post('/order/search', data),

  getDetail: (id) =>
    request.get(`/order/detail/${id}`),

  exportOrders: (data) =>
    request.post('/order/export', data, { responseType: 'blob' }),

  batchExport: (data) =>
    request.post('/order/batch-export', data, { responseType: 'blob' }),

  getAfterSaleList: (page, pageSize) =>
    request.get('/after/list', { params: { page, pageSize } }),

  getReviewList: (page, pageSize) =>
    request.get('/review/list', { params: { page, pageSize } })
}

// Analytics Dashboard
export const dashboardAPI = {
  getStats: (startDate, endDate) =>
    request.get('/dashboard/stats', { params: { startDate, endDate } })
}

// futura copilot
export const aiAPI = {
  chat: (message, memoryId) =>
    request.get('/ai/admin/chat', { params: { message, memoryId } }),

  getHistory: (memoryId) =>
    request.get('/ai/admin/chat/history', { params: { memoryId } })
}

// Marketing & Campaigns
export const activityAPI = {
  getActivities: (params) =>
    request.get('/activity/list', { params }),

  saveActivity: (data) =>
    request.post('/activity/save', data),

  updateStatus: (id, smsStatus) =>
    request.post('/activity/smsStatus', null, { params: { id, smsStatus } })
}
