<template>
  <div class="dashboard-page">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon blue"><Package :size="24" /></div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.goodsCount || 0 }}</span>
          <span class="stat-label">商品总数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon green"><Layers :size="24" /></div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.inventoryQuantity || 0 }}</span>
          <span class="stat-label">库存总量</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon orange"><AlertTriangle :size="24" /></div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.alertCount || 0 }}</span>
          <span class="stat-label">库存预警</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon purple"><ShoppingCart :size="24" /></div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.orderCount || 0 }}</span>
          <span class="stat-label">订单总数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon cyan"><Warehouse :size="24" /></div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.warehouseCount || 0 }}</span>
          <span class="stat-label">仓库数量</span>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card">
        <h3>库存分类分布</h3>
        <div ref="pieChart" class="chart-container"></div>
      </div>
      <div class="chart-card">
        <h3>订单趋势</h3>
        <div ref="barChart" class="chart-container"></div>
      </div>
    </div>

    <div class="recent-section">
      <div class="recent-card">
        <h3>最近订单</h3>
        <el-table :data="recentOrders" size="small">
          <el-table-column prop="orderNo" label="订单号" />
          <el-table-column prop="customerName" label="客户" />
          <el-table-column prop="totalAmount" label="金额">
            <template #default="scope">¥{{ scope.row.totalAmount }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { Package, Layers, AlertTriangle, ShoppingCart, Warehouse } from 'lucide-vue-next'
import { dashboardApi, orderApi } from '@/api'

const stats = ref({})
const charts = ref({})
const pieChart = ref(null)
const barChart = ref(null)
const recentOrders = ref([])

const getStatusText = (status) => ({ 0: '待支付', 1: '已支付', 2: '已发货', 3: '已完成', 4: '已取消' }[status] || '未知')
const getStatusType = (status) => ({ 0: 'warning', 1: 'primary', 2: 'info', 3: 'success', 4: 'danger' }[status] || 'default')

onMounted(async () => {
  try {
    const [statsRes, chartsRes, ordersRes] = await Promise.all([
      dashboardApi.getStats(),
      dashboardApi.getCharts(),
      orderApi.list({ page: 1, size: 5 })
    ])
    stats.value = statsRes.data
    charts.value = chartsRes.data
    recentOrders.value = ordersRes.data.records || []
    initCharts()
  } catch (e) {
    console.error(e)
  }
})

const initCharts = () => {
  const pieChartInstance = echarts.init(pieChart.value)
  pieChartInstance.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie', radius: '60%',
      data: charts.value.inventoryDistribution || [],
      label: { show: true, formatter: '{b}: {d}%' }
    }]
  })

  const barChartInstance = echarts.init(barChart.value)
  barChartInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: (charts.value.orderTrend || []).map(d => d.month) },
    yAxis: { type: 'value' },
    series: [{
      type: 'bar',
      data: (charts.value.orderTrend || []).map(d => d.count),
      itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#667eea' }, { offset: 1, color: '#764ba2' }]), borderRadius: [6, 6, 0, 0] }
    }]
  })

  window.addEventListener('resize', () => {
    pieChartInstance.resize()
    barChartInstance.resize()
  })
}
</script>

<style scoped>
.dashboard-page { padding: 0; }
.stats-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; margin-bottom: 24px; }
.stat-card { background: #fff; border-radius: 12px; padding: 20px; display: flex; align-items: center; gap: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.stat-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; color: #fff; }
.stat-icon.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-icon.orange { background: linear-gradient(135deg, #fc4a1a 0%, #f7b733 100%); }
.stat-icon.purple { background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%); color: #606266; }
.stat-icon.cyan { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
.stat-content { flex: 1; }
.stat-value { font-size: 24px; font-weight: 600; color: #303133; display: block; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; display: block; }
.charts-row { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 20px; }
.chart-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.chart-card h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0 0 16px 0; }
.chart-container { height: 280px; }
.recent-section { margin-bottom: 20px; }
.recent-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.recent-card h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0 0 16px 0; }
@media (max-width: 1400px) { .stats-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 1000px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } .charts-row { grid-template-columns: 1fr; } }
@media (max-width: 768px) { .stats-grid { grid-template-columns: 1fr; } }
</style>