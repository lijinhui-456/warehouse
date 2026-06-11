<template>
  <div class="page-container">
    <div class="page-header"><h2>用户管理</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-switch :value="scope.row.status === 1" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const tableData = ref([
  { id: 1, username: 'admin', realName: '系统管理员', email: 'admin@example.com', phone: '13800138000', status: 1, createTime: '2024-01-01 00:00:00' },
  { id: 2, username: 'zhangsan', realName: '张三', email: 'zhang@example.com', phone: '13800138001', status: 1, createTime: '2024-01-10 10:30:00' },
  { id: 3, username: 'lisi', realName: '李四', email: 'li@example.com', phone: '13800138002', status: 0, createTime: '2024-01-12 14:00:00' }
])
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
</style>