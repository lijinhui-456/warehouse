<template>
  <div class="page-container">
    <div class="page-header"><h2>系统配置</h2></div>
    <div class="settings-grid">
      <div class="setting-card">
        <h3>基础设置</h3>
        <el-form label-width="120px">
          <el-form-item label="系统名称"><el-input v-model="settings.systemName" /></el-form-item>
          <el-form-item label="系统描述"><el-input type="textarea" v-model="settings.description" /></el-form-item>
          <el-form-item label="启用验证码"><el-switch v-model="settings.enableCaptcha" /></el-form-item>
        </el-form>
      </div>
      <div class="setting-card">
        <h3>安全设置</h3>
        <el-form label-width="120px">
          <el-form-item label="密码最小长度"><el-input-number v-model="settings.passwordMinLength" :min="6" :max="32" /></el-form-item>
          <el-form-item label="登录失败锁定"><el-input-number v-model="settings.loginFailLock" :min="3" :max="10" /></el-form-item>
          <el-form-item label="会话超时(分钟)"><el-input-number v-model="settings.sessionTimeout" :min="10" :max="120" /></el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const settings = reactive({
  systemName: 'SmartWMS',
  description: '智能仓储物流管理系统',
  enableCaptcha: true,
  passwordMinLength: 6,
  loginFailLock: 5,
  sessionTimeout: 30
})
</script>
<style scoped>
.page-container { padding: 0; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.settings-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.setting-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.setting-card h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0 0 16px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
</style>