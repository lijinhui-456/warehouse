import request from '@/utils/request'

// 认证相关
export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data),
  logout: () => request.post('/auth/logout'),
  getCaptcha: () => request.get('/auth/captcha')
}

// 仪表盘
export const dashboardApi = {
  getStats: () => request.get('/dashboard/stats'),
  getCharts: () => request.get('/dashboard/charts')
}

// 商品管理
export const goodsApi = {
  list: (params) => request.get('/goods/list', { params }),
  getById: (id) => request.get(`/goods/${id}`),
  add: (data) => request.post('/goods/add', data),
  update: (data) => request.put('/goods/update', data),
  delete: (id) => request.delete(`/goods/${id}`)
}

// 库存管理
export const inventoryApi = {
  list: (params) => request.get('/inventory/list', { params }),
  alert: (params) => request.get('/inventory/alert', { params })
}

// 仓库管理
export const warehouseApi = {
  list: (params) => request.get('/warehouse/list', { params }),
  all: () => request.get('/warehouse/all'),
  add: (data) => request.post('/warehouse/add', data),
  update: (data) => request.put('/warehouse/update', data),
  delete: (id) => request.delete(`/warehouse/${id}`)
}

// 订单管理
export const orderApi = {
  list: (params) => request.get('/order/list', { params }),
  getById: (id) => request.get(`/order/${id}`),
  updateStatus: (data) => request.put('/order/status', data)
}

// 系统通知
export const noticeApi = {
  publish: (data) => request.post('/notice/publish', data),
  my: (params) => request.get('/notice/my', { params }),
  listAll: (params) => request.get('/notice/admin/list', { params }),
  del: (id) => request.delete('/notice/admin/' + id)
}