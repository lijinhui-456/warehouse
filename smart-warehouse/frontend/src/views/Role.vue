<template>
  <div class="page-container">
    <div class="page-header"><h2>角色管理</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const tableData = ref([
  { id: 1, name: '超级管理员', code: 'ADMIN', description: '系统超级管理员', status: 1 },
  { id: 2, name: '仓库管理员', code: 'WAREHOUSE', description: '仓库管理权限', status: 1 },
  { id: 3, name: '普通用户', code: 'USER', description: '普通用户权限', status: 1 }
])
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
</style>