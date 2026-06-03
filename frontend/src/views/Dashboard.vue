<template>
  <div class="dashboard-page">
    <h3>📊 数据仪表盘</h3>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center; padding: 10px">
            <div style="font-size: 36px; font-weight: bold; color: #409EFF">{{ stats.goodsCount }}</div>
            <div style="font-size: 14px; color: #909399; margin-top: 5px">商品总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center; padding: 10px">
            <div style="font-size: 36px; font-weight: bold; color: #67C23A">{{ stats.categoryCount }}</div>
            <div style="font-size: 14px; color: #909399; margin-top: 5px">分类数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center; padding: 10px">
            <div style="font-size: 36px; font-weight: bold; color: #E6A23C">{{ stats.stockTotal }}</div>
            <div style="font-size: 14px; color: #909399; margin-top: 5px">库存总量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center; padding: 10px">
            <div style="font-size: 36px; font-weight: bold; color: #F56C6C">{{ stats.todayOut }}</div>
            <div style="font-size: 14px; color: #909399; margin-top: 5px">今日出库
              <span style="color: #67C23A; margin-left: 10px">入库 {{ stats.todayIn }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span style="font-weight: bold">📈 近7天出入库趋势</span></template>
          <div ref="trendChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span style="font-weight: bold">📦 分类库存占比</span></template>
          <div ref="pieChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span style="font-weight: bold">📋 最近操作记录</span></template>
          <el-table :data="stats.recentRecords" size="small">
            <el-table-column label="类型" width="60">
              <template #default="{ row }">
                <el-tag :type="row.type === 1 ? 'success' : 'warning'" size="small">
                  {{ row.type === 1 ? '入库' : '出库' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="goodsName" label="商品" />
            <el-table-column prop="num" label="数量" width="60" />
            <el-table-column label="时间" width="160">
              <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span style="font-weight: bold">⚡ 快捷操作</span></template>
          <div style="display: flex; flex-wrap: wrap; gap: 15px; padding: 10px 0">
            <el-button type="primary" size="large" @click="$router.push('/goods')" style="flex: 1">商品管理</el-button>
            <el-button type="success" size="large" @click="$router.push('/stock')" style="flex: 1">入库出库</el-button>
            <el-button type="warning" size="large" @click="$router.push('/record')" style="flex: 1">库存记录</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import api from '../api'
import * as echarts from 'echarts'

const trendChart = ref(null)
const pieChart = ref(null)

const stats = reactive({
  goodsCount: 0, categoryCount: 0, stockTotal: 0,
  todayIn: 0, todayOut: 0, recentRecords: [], categoryDistribution: {}
})

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

const loadStats = async () => {
  const res = await api.get('/dashboard/stats')
  Object.assign(stats, res.data)
}

const loadTrend = async () => {
  const res = await api.get('/dashboard/trend')
  nextTick(() => {
    if (!trendChart.value) return
    const chart = echarts.init(trendChart.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['入库', '出库'] ,top:"top" ,right:"10px"},
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: res.data.map(d => d.date.slice(5)) },
      yAxis: { type: 'value' },
      series: [
        { name: '入库', type: 'bar', data: res.data.map(d => d.inCount), itemStyle: { color: '#67C23A' } },
        { name: '出库', type: 'bar', data: res.data.map(d => d.outCount), itemStyle: { color: '#E6A23C' } }
      ]
    })
  })
}

const loadPie = () => {
  nextTick(() => {
    const dist = stats.categoryDistribution
    const keys = Object.keys(dist)
    if (!keys.length || !pieChart.value) return
    const chart = echarts.init(pieChart.value)
    chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}' },
      series: [{
        type: 'pie', radius: ['40%', '70%'],
        data: keys.map(k => ({ name: k, value: dist[k] })),
        label: { show: true, formatter: '{b}\n{d}%' }
      }]
    })
  })
}

onMounted(async () => {
  await loadStats()
  loadTrend()
  loadPie()
})
</script>

<style scoped>
.dashboard-page { padding: 20px; }
h3 { margin: 0 0 10px 0; }
</style>