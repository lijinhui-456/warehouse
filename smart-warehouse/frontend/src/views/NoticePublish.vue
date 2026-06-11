<template>
  <div class="main-card">
    <h2>发布系统通知</h2>
    <el-form :model="form" label-width="80px" style="max-width:720px">
      <el-form-item label="标题">
        <el-input v-model="form.title" maxlength="100" placeholder="请输入通知标题"/>
      </el-form-item>
      <el-form-item label="接收范围">
        <el-radio-group v-model="form.scope">
          <el-radio value="ALL">全站通知（所有人可见）</el-radio>
          <el-radio value="USER">指定用户</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="接收用户" v-if="form.scope==='USER'">
        <el-input v-model="form.toUserId" placeholder="用户ID（数字）"/>
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="form.content" type="textarea" :rows="6" maxlength="2000"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit" :loading="loading">发布</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { noticeApi } from '@/api'

const form = reactive({ title: '', content: '', scope: 'ALL', toUserId: '' })
const loading = ref(false)

const onSubmit = async () => {
  if (!form.title || !form.content) return ElMessage.error('标题和内容必填')
  try {
    loading.value = true
    await noticeApi.publish({
      title: form.title,
      content: form.content,
      scope: form.scope,
      toUserId: form.scope === 'USER' ? Number(form.toUserId) : undefined
    })
    ElMessage.success('已发布')
    form.title = ''
    form.content = ''
    form.toUserId = ''
  } catch (e) {
    ElMessage.error(e?.message || '发布失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.main-card { padding: 20px; background: #fff; border-radius: 8px; }
.main-card h2 { margin: 0 0 16px; }
</style>
