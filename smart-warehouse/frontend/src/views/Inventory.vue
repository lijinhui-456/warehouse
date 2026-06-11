<template>
  <div class="page-container">
    <div class="page-header"><h2>库存查询</h2></div>
    <div class="content-card">
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索商品名称/编码" clearable @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsCode" label="商品编码" width="120" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="quantity" label="库存数量" width="120" />
        <el-table-column prop="lockedQuantity" label="锁定数量" width="120" />
        <el-table-column prop="availableQuantity" label="可用数量" width="120" />
        <el-table-column prop="minQuantity" label="最小库存" width="100" />
        <el-table-column prop="location" label="库位" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.quantity < scope.row.minQuantity ? 'danger' : 'success'">
              {{ scope.row.quantity < scope.row.minQuantity ? '库存不足' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :total="total" :page-size="pageSize" :current-page="currentPage" @current-change="handlePageChange" layout="prev, pager, next, jumper, ->, total" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { inventoryApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const keyword = ref('')

const loadData = async () => {
  loading.value = true
  try {
    const res = await inventoryApi.list({ page: currentPage.value, size: pageSize.value, keyword: keyword.value })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {} finally { loading.value = false }
}

const handlePageChange = (page) => { currentPage.value = page; loadData() }
onMounted(() => loadData())
</script>

<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.search-bar { display: flex; gap: 12px; margin-bottom: 20px; }
.search-bar .el-input { width: 300px; }
.el-pagination { margin-top: 20px; text-align: right; }
</style>