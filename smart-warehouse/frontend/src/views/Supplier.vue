<template>
  <div class="page-container">
    <div class="page-header"><h2>供应商管理</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="供应商编码" width="120" />
        <el-table-column prop="name" label="供应商名称" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '合作中' : '已停用' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const tableData = ref([
  { id: 1, code: 'SUP001', name: '苹果中国有限公司', contact: '王经理', phone: '400-666-8888', email: 'apple@example.com', address: '北京市朝阳区', status: 1 },
  { id: 2, code: 'SUP002', name: '华为技术有限公司', contact: '李经理', phone: '400-888-6666', email: 'huawei@example.com', address: '深圳市南山区', status: 1 },
  { id: 3, code: 'SUP003', name: '小米科技有限公司', contact: '张经理', phone: '400-100-5678', email: 'xiaomi@example.com', address: '北京市海淀区', status: 1 }
])
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
</style>