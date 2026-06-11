<template>
  <div class="main-card">
    <h2>我的消息</h2>
    <el-empty v-if="list.length===0" description="暂无消息"/>
    <el-timeline v-else>
      <el-timeline-item
        v-for="n in list"
        :key="n.id"
        :timestamp="n.createTime"
        placement="top"
        color="#409eff"
      >
        <el-card shadow="never" style="margin-bottom:8px">
          <div style="font-weight:600;margin-bottom:4px">{{ n.title }}</div>
          <div style="color:#606266;white-space:pre-line">{{ n.content }}</div>
          <div style="color:#909399;font-size:12px;margin-top:6px">
            {{ n.scope === 'ALL' ? '全站通知' : '私人通知' }} · 发布人：{{ n.publisherName }}
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :page-size="10"
      :current-page="page"
      @current-change="onPage"
      style="margin-top:16px"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { noticeApi } from '@/api'

const list = ref([])
const total = ref(0)
const page = ref(1)

const load = async () => {
  try {
    const res = await noticeApi.my({ page: page.value, size: 10 })
    list.value = res.data.list || []
    total.value = res.data.total || 0
  } catch (e) {
    ElMessage.error(e?.message || '加载失败')
  }
}
const onPage = (p) => { page.value = p; load() }
onMounted(load)
</script>

<style scoped>
.main-card { padding: 20px; background: #fff; border-radius: 8px; }
.main-card h2 { margin: 0 0 16px; }
</style>
