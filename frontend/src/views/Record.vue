<template>
  <div class="record-page">
    <h3>库存记录</h3>

    <!-- 筛选 -->
    <div class="filter-bar">
      <el-select v-model="filterType" placeholder="类型" style="width: 120px" clearable>
        <el-option label="入库" :value="1" />
        <el-option label="出库" :value="2" />
      </el-select>
      <el-button type="primary" @click="loadRecords">查询</el-button>
    </div>

    <!-- 记录表格 -->
    <el-table :data="recordList" border style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="类型" width="80">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'success' : 'warning'">
            {{ row.type === 1 ? '入库' : '出库' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品">
        <template #default="{ row }">
          {{ getGoodsName(row.goodsId) }}
        </template>
      </el-table-column>
      <el-table-column prop="num" label="数量" width="80">
        <template #default="{ row }">
          <span :style="{ color: row.type === 1 ? '#67C23A' : '#E6A23C' }">
            {{ row.type === 1 ? '+' : '-' }}{{ row.num }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="createdAt" label="时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="current"
      v-model:page-size="size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 20px; justify-content: flex-end"
      @current-change="loadRecords"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { goodsApi, stockApi } from '../api'

const recordList = ref([])
const goodsList = ref([])
const filterType = ref(null)
const current = ref(1)
const size = ref(10)
const total = ref(0)

// 加载商品列表
const loadGoods = async () => {
  const res = await goodsApi.list()
  goodsList.value = res.data
}

// 加载记录
const loadRecords = async () => {
  const res = await stockApi.recordPage({
    current: current.value,
    size: size.value,
    type: filterType.value
  })
  recordList.value = res.data.records
  total.value = res.data.total
}

// 获取商品名称
const getGoodsName = (id) => {
  const g = goodsList.value.find(item => item.id === id)
  return g ? g.name : '-'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  loadGoods()
  loadRecords()
})
</script>

<style scoped>
.record-page {
  padding: 20px;
}

.filter-bar {
  display: flex;
  gap: 10px;
}
</style>
