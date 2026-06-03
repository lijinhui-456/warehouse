<template>
  <div class="audit-page">
    <h3>📋 操作日志</h3>

    <div style="display: flex; gap: 10px; margin-top: 20px">
      <el-select v-model="filterModule" placeholder="操作模块" style="width: 140px" clearable @change="loadLogs">
        <el-option label="全部" :value="null" />
        <el-option label="商品管理" value="商品管理" />
        <el-option label="分类管理" value="分类管理" />
        <el-option label="库存管理" value="库存管理" />
        <el-option label="用户管理" value="用户管理" />
      </el-select>
      <el-input v-model="filterUsername" placeholder="操作人" style="width: 150px" clearable @clear="loadLogs" />
      <el-button type="primary" @click="loadLogs">查询</el-button>
    </div>

    <el-table :data="logList" border stripe style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="操作人" width="100" />
      <el-table-column label="操作" width="70">
        <template #default="{ row }">
          <el-tag v-if="row.operation === '新增'" type="success" size="small">{{ row.operation }}</el-tag>
          <el-tag v-else-if="row.operation === '修改'" type="primary" size="small">{{ row.operation }}</el-tag>
          <el-tag v-else-if="row.operation === '删除'" type="danger" size="small">{{ row.operation }}</el-tag>
          <el-tag v-else size="small">{{ row.operation }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="module" label="模块" width="100" />
      <el-table-column prop="detail" label="操作详情" min-width="300" show-overflow-tooltip />
      <el-table-column prop="ip" label="IP地址" width="140" />
      <el-table-column prop="createdAt" label="操作时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="current"
      v-model:page-size="size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 20px; justify-content: flex-end"
      @current-change="loadLogs"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const logList = ref([])
const filterModule = ref(null)
const filterUsername = ref(null)
const current = ref(1)
const size = ref(10)
const total = ref(0)

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

const loadLogs = async () => {
  const res = await api.get('/audit-log/page', {
    params: { current: current.value, size: size.value, module: filterModule.value, username: filterUsername.value }
  })
  logList.value = res.data.records
  total.value = res.data.total
}

onMounted(loadLogs)
</script>

<style scoped>
.audit-page { padding: 20px; }
h3 { margin: 0; }
</style>