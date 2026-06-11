<template>
  <div class="app-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <div class="logo-wrapper" @click="goTo('/dashboard')">
          <div class="logo-icon">
            <Warehouse :size="24" />
          </div>
          <div class="logo-text" v-show="!isCollapsed">
            <span class="logo-title">SmartWMS</span>
            <span class="logo-subtitle">智能仓储管理系统</span>
          </div>
        </div>
        <button class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <ChevronLeft v-if="!isCollapsed" :size="18" />
          <ChevronRight v-else :size="18" />
        </button>
      </div>

      <div class="search-box" v-show="!isCollapsed">
        <Search :size="16" />
        <input type="text" v-model="searchQuery" placeholder="搜索功能..." />
      </div>

      <nav class="sidebar-nav">
        <!-- 工作台 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <LayoutDashboard :size="12" />
            <span>工作台</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/dashboard' }" @click="goTo('/dashboard')">
            <Home :size="18" />
            <span v-show="!isCollapsed">数据概览</span>
          </div>
        </div>

        <!-- 商品管理 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <Package :size="12" />
            <span>商品管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/goods' }" @click="goTo('/goods')">
            <Box :size="18" />
            <span v-show="!isCollapsed">商品列表</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/supplier' }" @click="goTo('/supplier')">
            <Building2 :size="18" />
            <span v-show="!isCollapsed">供应商管理</span>
          </div>
        </div>

        <!-- 仓储管理 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <Warehouse :size="12" />
            <span>仓储管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/warehouse' }" @click="goTo('/warehouse')">
            <MapPin :size="18" />
            <span v-show="!isCollapsed">仓库管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/inventory' }" @click="goTo('/inventory')">
            <Layers :size="18" />
            <span v-show="!isCollapsed">库存查询</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/inventory-alert' }" @click="goTo('/inventory-alert')">
            <AlertTriangle :size="18" />
            <span v-show="!isCollapsed">库存预警</span>
          </div>
        </div>

        <!-- 出入库管理 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <ArrowUpDown :size="12" />
            <span>出入库管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/stock-in' }" @click="goTo('/stock-in')">
            <ArrowDownCircle :size="18" />
            <span v-show="!isCollapsed">入库管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/stock-out' }" @click="goTo('/stock-out')">
            <ArrowUpCircle :size="18" />
            <span v-show="!isCollapsed">出库管理</span>
          </div>
        </div>

        <!-- 订单管理 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <ShoppingCart :size="12" />
            <span>订单管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/order' }" @click="goTo('/order')">
            <ClipboardList :size="18" />
            <span v-show="!isCollapsed">订单列表</span>
          </div>
        </div>

        <!-- 系统管理 -->
        <div class="nav-group" v-if="role === 'ADMIN'">
          <div class="nav-group-title" v-show="!isCollapsed">
            <Settings :size="12" />
            <span>系统管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/user' }" @click="goTo('/user')">
            <Users :size="18" />
            <span v-show="!isCollapsed">用户管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/role' }" @click="goTo('/role')">
            <Shield :size="18" />
            <span v-show="!isCollapsed">角色管理</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/log' }" @click="goTo('/log')">
            <FileText :size="18" />
            <span v-show="!isCollapsed">操作日志</span>
          </div>
        </div>

        <!-- 系统设置 -->
        <div class="nav-group">
          <div class="nav-group-title" v-show="!isCollapsed">
            <Wrench :size="12" />
            <span>系统设置</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/settings' }" @click="goTo('/settings')">
            <Settings2 :size="18" />
            <span v-show="!isCollapsed">系统配置</span>
          </div>
          <div class="nav-item" :class="{ active: activeMenu === '/profile' }" @click="goTo('/profile')">
            <User :size="18" />
            <span v-show="!isCollapsed">个人中心</span>
          </div>
        </div>
      </nav>

      <div class="sidebar-footer">
        <div class="user-info" v-show="!isCollapsed">
          <div class="user-avatar"><User :size="16" /></div>
          <div class="user-detail">
            <span class="user-name">{{ realName || username }}</span>
            <span class="user-role">{{ role === 'ADMIN' ? '管理员' : '普通用户' }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout"><LogOut :size="16" /></button>
        </div>
        <div class="version" v-show="!isCollapsed">v1.0.0</div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <header class="top-header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-popover placement="bottom-end" :width="360" trigger="click" popper-class="notification-popover" @show="loadNotices">
            <template #reference>
              <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notification-badge">
                <el-button circle><Bell :size="18" /></el-button>
              </el-badge>
            </template>

            <div class="notice-panel">
              <div class="notice-panel-header">
                <span>消息通知</span>
                <el-button type="primary" link size="small" @click="goTo('/notice/my')">查看全部</el-button>
              </div>

              <el-empty v-if="noticeList.length === 0 &amp;&amp; !noticeLoading" description="暂无消息" :image-size="80" />

              <div v-else class="notice-list">
                <div
                  v-for="n in noticeList"
                  :key="n.id"
                  class="notice-item"
                  :class="{ unread: !readNoticeIds.has(n.id) }"
                  @click="openNotice(n)"
                >
                  <div class="notice-title">{{ n.title }}</div>
                  <div class="notice-content">{{ n.content }}</div>
                  <div class="notice-meta">
                    <span>{{ n.scope === 'ALL' ? '全站通知' : '私人通知' }}</span>
                    <span>{{ formatTime(n.createTime) }}</span>
                  </div>
                </div>
              </div>

              <div v-if="noticeLoading" class="notice-loading">加载中...</div>
            </div>
          </el-popover>

          <el-dropdown trigger="click">
            <div class="user-dropdown">
              <div class="mini-avatar"><User :size="14" /></div>
              <span>{{ realName || username }}</span>
              <ChevronDown :size="14" />
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goTo('/profile')"><User :size="14" /> 个人中心</el-dropdown-item>
                <el-dropdown-item v-if="role === 'ADMIN'" @click="goTo('/notice/publish')"><Bell :size="14" /> 发布系统通知</el-dropdown-item>
                <el-dropdown-item @click="goTo('/notice/my')"><FileText :size="14" /> 我的消息</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout"><LogOut :size="14" /> 退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <div class="content-wrapper"><router-view /></div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { authApi, noticeApi } from '@/api'
import { Warehouse, ChevronLeft, ChevronRight, Search, LayoutDashboard, Home, Package, Box, Building2, MapPin, Layers, AlertTriangle, ArrowUpDown, ArrowDownCircle, ArrowUpCircle, ShoppingCart, ClipboardList, Settings, Users, Shield, FileText, Wrench, Settings2, User, LogOut, Bell, ChevronDown } from 'lucide-vue-next'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false)
const searchQuery = ref('')
const username = localStorage.getItem('username') || ''
const realName = localStorage.getItem('realName') || ''
const role = localStorage.getItem('role') || 'USER'

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const map = {
    '/dashboard': '数据概览', '/goods': '商品列表', '/inventory': '库存查询', '/inventory-alert': '库存预警',
    '/warehouse': '仓库管理', '/stock-in': '入库管理', '/stock-out': '出库管理', '/order': '订单列表',
    '/supplier': '供应商管理', '/user': '用户管理', '/role': '角色管理', '/log': '操作日志',
    '/settings': '系统配置', '/profile': '个人中心', '/notice/publish': '发布系统通知', '/notice/my': '我的消息'
  }
  return map[route.path] || '首页'
})

const goTo = (path) => router.push(path)

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
    await authApi.logout()
    localStorage.clear()
    ElMessage.success('退出成功')
    router.push('/login')
  } catch (e) {}
}

// ======== 通知铃铛 ========
const noticeList = ref([])
const noticeLoading = ref(false)
const unreadCount = ref(0)
const readNoticeIds = reactive(new Set(JSON.parse(localStorage.getItem('readNoticeIds') || '[]')))

const loadNotices = async () => {
  try {
    noticeLoading.value = true
    const res = await noticeApi.my({ page: 1, size: 10 })
    if (res && res.data && Array.isArray(res.data.list)) {
      noticeList.value = res.data.list
      unreadCount.value = res.data.list.filter(n => !readNoticeIds.has(n.id)).length
    }
  } catch (e) {
    noticeList.value = []
    unreadCount.value = 0
  } finally {
    noticeLoading.value = false
  }
}

const openNotice = (n) => {
  if (!readNoticeIds.has(n.id)) {
    readNoticeIds.add(n.id)
    localStorage.setItem('readNoticeIds', JSON.stringify(Array.from(readNoticeIds)))
    unreadCount.value = noticeList.value.filter(x => !readNoticeIds.has(x.id)).length
  }
  goTo('/notice/my')
}

const formatTime = (t) => {
  if (!t) return ''
  const s = String(t).replace('T', ' ').substring(0, 16)
  return s
}

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
.app-container { display: flex; min-height: 100vh; background: #f5f7fa; }
.sidebar { width: 250px; background: linear-gradient(180deg, #1e3a5f 0%, #152238 100%); color: #fff; display: flex; flex-direction: column; position: fixed; left: 0; top: 0; bottom: 0; z-index: 1000; transition: width 0.3s; box-shadow: 4px 0 20px rgba(0,0,0,0.15); }
.sidebar.collapsed { width: 64px; }
.sidebar-header { padding: 16px; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.1); }
.logo-wrapper { display: flex; align-items: center; gap: 12px; cursor: pointer; flex: 1; }
.logo-icon { width: 36px; height: 36px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.logo-text { display: flex; flex-direction: column; overflow: hidden; }
.logo-title { font-size: 15px; font-weight: 600; color: #fff; }
.logo-subtitle { font-size: 10px; color: rgba(255,255,255,0.5); margin-top: 2px; }
.collapse-btn { width: 28px; height: 28px; border: none; background: rgba(255,255,255,0.1); border-radius: 6px; color: #fff; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.collapse-btn:hover { background: rgba(255,255,255,0.2); }
.search-box { padding: 12px 16px; display: flex; align-items: center; gap: 8px; }
.search-box input { flex: 1; height: 32px; padding: 0 12px; background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.1); border-radius: 6px; color: #fff; font-size: 13px; outline: none; }
.search-box input::placeholder { color: rgba(255,255,255,0.5); }
.sidebar-nav { flex: 1; overflow-y: auto; padding: 8px 0; }
.nav-group { margin-bottom: 4px; }
.nav-group-title { padding: 6px 16px; font-size: 10px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px; color: rgba(255,255,255,0.4); display: flex; align-items: center; gap: 6px; }
.nav-item { padding: 10px 16px; margin: 2px 8px; border-radius: 8px; cursor: pointer; display: flex; align-items: center; gap: 12px; color: rgba(255,255,255,0.7); font-size: 14px; transition: all 0.2s; }
.nav-item:hover { background: rgba(255,255,255,0.1); color: #fff; }
.nav-item.active { background: linear-gradient(90deg, #667eea 0%, #764ba2 100%); color: #fff; font-weight: 500; }
.sidebar-footer { padding: 12px 16px; border-top: 1px solid rgba(255,255,255,0.1); }
.user-info { display: flex; align-items: center; gap: 10px; padding: 8px; background: rgba(255,255,255,0.05); border-radius: 8px; }
.user-avatar { width: 32px; height: 32px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.user-detail { flex: 1; display: flex; flex-direction: column; min-width: 0; }
.user-name { font-size: 12px; font-weight: 500; color: #fff; }
.user-role { font-size: 10px; color: rgba(255,255,255,0.5); }
.logout-btn { width: 28px; height: 28px; border: none; background: transparent; border-radius: 6px; color: rgba(255,255,255,0.6); cursor: pointer; display: flex; align-items: center; justify-content: center; }
.logout-btn:hover { background: rgba(255,255,255,0.1); color: #ff4d4f; }
.version { text-align: center; font-size: 10px; color: rgba(255,255,255,0.3); margin-top: 8px; }
.main-content { flex: 1; margin-left: 250px; display: flex; flex-direction: column; min-height: 100vh; transition: margin-left 0.3s; }
.sidebar.collapsed + .main-content { margin-left: 64px; }
.top-header { height: 60px; background: #fff; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 2px 8px rgba(0,0,0,0.06); position: sticky; top: 0; z-index: 100; }
.header-left { flex: 1; }
.header-right { display: flex; align-items: center; gap: 16px; }
.user-dropdown { display: flex; align-items: center; gap: 8px; padding: 6px 12px; border-radius: 20px; cursor: pointer; }
.user-dropdown:hover { background: #f5f7fa; }
.mini-avatar { width: 28px; height: 28px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; }
.content-wrapper { flex: 1; padding: 20px; overflow-y: auto; }

/* 通知面板 */
.notice-panel { min-width: 320px; }
.notice-panel-header { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; font-weight: 600; border-bottom: 1px solid #ebeef5; color: #303133; }
.notice-list { max-height: 340px; overflow-y: auto; }
.notice-item { padding: 12px 16px; cursor: pointer; border-bottom: 1px solid #f5f5f5; transition: background 0.15s; }
.notice-item:hover { background: #f5f7fa; }
.notice-item.unread { background: #ecf5ff; }
.notice-title { font-size: 13px; font-weight: 500; color: #303133; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.notice-content { font-size: 12px; color: #606266; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.notice-meta { display: flex; justify-content: space-between; font-size: 11px; color: #909399; margin-top: 6px; }
.notice-loading { padding: 20px; text-align: center; color: #909399; font-size: 12px; }
</style>