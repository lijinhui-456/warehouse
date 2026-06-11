<template>
  <div class="page-container">
    <div class="page-header"><h2>订单管理</h2></div>
    <div class="content-card">
      <div class="search-bar">
        <el-select v-model="status" placeholder="订单状态" clearable @change="loadData">
          <el-option label="全部" :value="null" />
          <el-option label="待支付" :value="0" />
          <el-option label="已支付" :value="1" />
          <el-option label="已发货" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已取消" :value="4" />
        </el-select>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerName" label="客户" width="120" />
        <el-table-column prop="customerPhone" label="电话" width="130" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="itemCount" label="商品数量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="success" v-if="scope.row.status === 0" @click="handlePay(scope.row)">支付</el-button>
            <el-button size="small" type="primary" v-if="scope.row.status === 1" @click="handleShip(scope.row)">发货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :total="total" :page-size="pageSize" :current-page="currentPage" @current-change="handlePageChange" layout="prev, pager, next, jumper, ->, total" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { orderApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const status = ref(null)

const getStatusText = (s) => ({ 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }[s] || '未知')
const getStatusType = (s) => ({ 0: 'warning', 1: 'primary', 2: 'info', 3: 'success', 4: 'danger' }[s] || 'default')

const loadData = async () => {
  loading.value = true
  try {
    const res = await orderApi.list({ page: currentPage.value, size: pageSize.value, status: status.value })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {} finally { loading.value = false }
}

const handlePageChange = (page) => { currentPage.value = page; loadData() }

const handleView = (row) => { ElMessage.info('查看订单: ' + row.orderNo) }

const handlePay = async (row) => {
  try {
    await orderApi.updateStatus({ id: row.id, status: 1 })
    ElMessage.success('支付成功')
    loadData()
  } catch (e) {}
}

const handleShip = async (row) => {
  try {
    await orderApi.updateStatus({ id: row.id, status: 2 })
    ElMessage.success('发货成功')
    loadData()
  } catch (e) {}
}

onMounted(() => loadData())
</script>

<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.search-bar { margin-bottom: 20px; }
.search-bar .el-select { width: 150px; }
.el-pagination { margin-top: 20px; text-align: right; }
</style>