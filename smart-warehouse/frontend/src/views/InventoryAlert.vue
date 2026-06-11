<template>
  <div class="page-container">
    <div class="page-header"><h2>库存预警</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="goodsCode" label="商品编码" width="120" />
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="quantity" label="当前库存" width="120">
          <template #default="scope">
            <span :style="{ color: scope.row.quantity < scope.row.minQuantity ? '#f56c6c' : '#303133' }">{{ scope.row.quantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minQuantity" label="最小库存" width="100" />
        <el-table-column label="预警等级" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.quantity < scope.row.minQuantity * 0.5 ? 'danger' : 'warning'">
              {{ scope.row.quantity < scope.row.minQuantity * 0.5 ? '严重' : '警告' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="库位" width="100" />
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

const loadData = async () => {
  loading.value = true
  try {
    const res = await inventoryApi.alert({ page: currentPage.value, size: pageSize.value })
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
.el-pagination { margin-top: 20px; text-align: right; }
</style>