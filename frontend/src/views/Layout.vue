<template>
  <el-container class="layout-container">
    <!-- 左侧导航 -->
    <el-aside width="200px">
      <div class="logo">仓库管理</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/goods">
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/stock">
          <span>入库出库</span>
        </el-menu-item>
        <el-menu-item index="/record">
          <span>库存记录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容 -->
    <el-container>
      <!-- 顶部 -->
      <el-header>
        <span class="username">{{ realName }} ({{ username }})</span>
        <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
      </el-header>

      <!-- 内容区 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import {userApi} from "../api/index.js";
import {ElMessage} from "element-plus";
import {onMounted, onUnmounted} from "vue";

const router = useRouter()

const username = localStorage.getItem('username') || ''
const realName = localStorage.getItem('realName') || ''

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('realName')
  router.push('/login')
}
let hearttime=null
onMounted(async () => {
  try{
    await userApi.getInfo()
  }catch(err){
    ElMessage.error(err)
  }
  hearttime=setInterval(()=>{
     userApi.getInfo().catch(()=>{

     })
  },5*60*1000)
})
onUnmounted(()=>{
  clearInterval(hearttime)
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.el-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
}

.username {
  margin-right: 15px;
  color: #666;
}

.el-main {
  background: #f5f7fa;
}

.el-menu {
  border: none;
}
</style>
