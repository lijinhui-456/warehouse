<template>
  <div class="page-container">
    <div class="page-header"><h2>入库管理</h2></div>
    <div class="content-card">
      <el-table :data="tableData" border>
        <el-table-column prop="orderNo" label="入库单号" width="180" />
        <el-table-column prop="type" label="入库类型" width="100" />
        <el-table-column prop="warehouseName" label="仓库" />
        <el-table-column prop="supplierName" label="供应商" />
        <el-table-column prop="totalAmount" label="总金额" width="120" />
        <el-table-column prop="itemCount" label="商品数量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="['warning', 'primary', 'success'][scope.row.status]">{{ ['待审核', '已审核', '已完成'][scope.row.status] }}</el-tag>
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
  { orderNo: 'IN20240115001', type: '采购入库', warehouseName: '北京中心仓', supplierName: '苹果中国', totalAmount: 899900, itemCount: 100, status: 2, createTime: '2024-01-15 10:30:00' },
  { orderNo: 'IN20240115002', type: '采购入库', warehouseName: '上海分仓', supplierName: '华为技术', totalAmount: 349950, itemCount: 50, status: 1, createTime: '2024-01-15 11:00:00' }
])
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
</style>