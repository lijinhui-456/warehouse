import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../views/Layout.vue'
import Goods from '../views/Goods.vue'
import Stock from '../views/Stock.vue'
import Record from '../views/Record.vue'
import zhuce from '../views/zhuce.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login },
  {path:'/zhuce' , component: zhuce},
  {
    path: '/',
    component: Layout,
    redirect: '/goods',
    children: [
      { path: 'goods', name: 'Goods', component: Goods },
      { path: 'stock', name: 'Stock', component: Stock },
      { path: 'record', name: 'Record', component: Record }
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
  } else {
    next()
  }
})

export default router
