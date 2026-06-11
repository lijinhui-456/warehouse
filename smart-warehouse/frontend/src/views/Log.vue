<template>
  <div class="page-container">
    <div class="page-header"><h2>操作日志</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border>
        <el-table-column prop="username" label="用户" width="120" />
        <el-table-column prop="operation" label="操作描述" />
        <el-table-column prop="method" label="请求方法" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="time" label="耗时(ms)" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '成功' : '失败' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" />
      </el-table>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const tableData = ref([
  { username: 'admin', operation: '用户登录', method: 'POST /auth/login', ip: '127.0.0.1', time: 156, status: 1, createTime: '2024-01-15 10:30:00' },
  { username: 'admin', operation: '查询商品列表', method: 'GET /goods/list', ip: '127.0.0.1', time: 89, status: 1, createTime: '2024-01-15 10:31:00' },
  { username: 'zhangsan', operation: '新增入库单', method: 'POST /stock-in/add', ip: '127.0.0.1', time: 234, status: 1, createTime: '2024-01-15 11:00:00' }
])
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
</style>