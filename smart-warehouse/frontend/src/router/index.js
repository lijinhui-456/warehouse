import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/',
    component: () => import('@/components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue') },
      { path: 'goods', name: 'Goods', component: () => import('@/views/Goods.vue') },
      { path: 'inventory', name: 'Inventory', component: () => import('@/views/Inventory.vue') },
      { path: 'inventory-alert', name: 'InventoryAlert', component: () => import('@/views/InventoryAlert.vue') },
      { path: 'warehouse', name: 'Warehouse', component: () => import('@/views/Warehouse.vue') },
      { path: 'stock-in', name: 'StockIn', component: () => import('@/views/StockIn.vue') },
      { path: 'stock-out', name: 'StockOut', component: () => import('@/views/StockOut.vue') },
      { path: 'order', name: 'Order', component: () => import('@/views/Order.vue') },
      { path: 'supplier', name: 'Supplier', component: () => import('@/views/Supplier.vue') },
      { path: 'user', name: 'User', component: () => import('@/views/User.vue'), meta: { role: 'ADMIN' } },
      { path: 'role', name: 'Role', component: () => import('@/views/Role.vue'), meta: { role: 'ADMIN' } },
      { path: 'log', name: 'Log', component: () => import('@/views/Log.vue'), meta: { role: 'ADMIN' } },
      { path: 'profile', name: 'Profile', component: () => import('@/views/Profile.vue') },
      { path: 'settings', name: 'Settings', component: () => import('@/views/Settings.vue') },
      { path: 'notice/publish', name: 'NoticePublish', component: () => import('@/views/NoticePublish.vue'), meta: { role: 'ADMIN' } },
      { path: 'notice/my', name: 'NoticeMy', component: () => import('@/views/NoticeMy.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  if (!token) {
    next('/login')
    return
  }

  if (to.meta.role) {
    const role = localStorage.getItem('role')
    if (role !== to.meta.role) {
      ElMessage.warning('暂无权限访问')
      next('/dashboard')
      return
    }
  }

  next()
})

export default router