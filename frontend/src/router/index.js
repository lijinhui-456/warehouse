import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import Goods from '../views/Goods.vue'
import Stock from '../views/Stock.vue'
import Record from '../views/Record.vue'
import AuditLog from '../views/AuditLog.vue'
import zhuce from '../views/zhuce.vue'
const routes = [
  { path: '/login', name: 'Login', component: Login },
  {path:'/zhuce' , component: zhuce},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: Dashboard },
      { path: 'goods', name: 'Goods', component: Goods },
      { path: 'stock', name: 'Stock', component: Stock },
      { path: 'record', name: 'Record', component: Record },
      { path: 'audit-log', name: 'AuditLog', component: AuditLog, meta: { role: 'ADMIN' } }
    ]
  }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})
// 路由守卫：没登录就跳转到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if ( to.path !== '/login'&&to.path!=='/zhuce' && !token){
    next('/login')
  } else if (to.meta.role) {
    // 检查角色权限
    const role = localStorage.getItem('role')
    if (role !== to.meta.role) {
      next('/dashboard')
    } else {
      next()
    }
  } else {
    next()
  }
})
export default router