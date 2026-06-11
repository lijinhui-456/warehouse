<template>
  <div class="page-container">
    <div class="profile-header">
      <div class="avatar"><User :size="48" /></div>
      <h2>{{ realName || username }}</h2>
      <p>{{ role === 'ADMIN' ? '超级管理员' : '普通用户' }}</p>
    </div>
    <div class="profile-content">
      <div class="info-card">
        <h3>基本信息</h3>
        <div class="info-item"><span class="label">用户名</span><span class="value">{{ username }}</span></div>
        <div class="info-item"><span class="label">真实姓名</span><span class="value">{{ realName }}</span></div>
        <div class="info-item"><span class="label">角色</span><span class="value"><el-tag :type="role === 'ADMIN' ? 'danger' : 'info'">{{ role === 'ADMIN' ? '管理员' : '普通用户' }}</el-tag></span></div>
      </div>
      <div class="info-card">
        <h3>修改密码</h3>
        <el-form label-width="100px">
          <el-form-item label="原密码"><el-input type="password" v-model="pwdForm.oldPwd" /></el-form-item>
          <el-form-item label="新密码"><el-input type="password" v-model="pwdForm.newPwd" /></el-form-item>
          <el-form-item label="确认密码"><el-input type="password" v-model="pwdForm.confirmPwd" /></el-form-item>
          <el-form-item><el-button type="primary" @click="changePwd">修改密码</el-button></el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { User } from 'lucide-vue-next'

const username = localStorage.getItem('username') || ''
const realName = localStorage.getItem('realName') || ''
const role = localStorage.getItem('role') || 'USER'

const pwdForm = reactive({ oldPwd: '', newPwd: '', confirmPwd: '' })

const changePwd = () => {
  if (!pwdForm.oldPwd || !pwdForm.newPwd || !pwdForm.confirmPwd) {
    ElMessage.warning('请填写完整')
    return
  }
  if (pwdForm.newPwd !== pwdForm.confirmPwd) {
    ElMessage.warning('两次密码不一致')
    return
  }
  ElMessage.success('密码修改成功')
}
</script>
<style scoped>
.page-container { padding: 0; }
.profile-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 12px; padding: 30px; text-align: center; color: #fff; margin-bottom: 20px; }
.avatar { width: 80px; height: 80px; background: rgba(255,255,255,0.2); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 16px; }
.profile-header h2 { font-size: 24px; margin: 0 0 8px; }
.profile-header p { font-size: 14px; opacity: 0.8; margin: 0; }
.profile-content { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.info-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.info-card h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0 0 16px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
.info-item { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #f5f5f5; }
.label { color: #909399; }
.value { color: #303133; font-weight: 500; }
</style>