import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器：自动带上token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：统一处理错误
api.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default api

// 各个模块的接口
export const userApi = {
  login: (data) => api.post('/user/login', data),
  getInfo: () => api.get('/user/info'),
}

export const categoryApi = {
  list: () => api.get('/category/list'),
  page: (params) => api.get('/category/page', { params }),
  add: (data) => api.post('/category', data),
  update: (id, data) => api.put(`/category/${id}`, data),
  delete: (id) => api.delete(`/category/${id}`)
}

export const goodsApi = {
  list: () => api.get('/goods/list'),
  page: (params) => api.get('/goods/page', { params }),
  add: (data) => api.post('/goods', data),
  update: (id, data) => api.put(`/goods/${id}`, data),
  delete: (id) => api.delete(`/goods/${id}`)
}

export const stockApi = {
  list: () => api.get('/stock/list'),
  in: (data) => api.post('/stock/in', data),
  out: (data) => api.post('/stock/out', data),
  recordPage: (params) => api.get('/stock/record/page', { params })
}
