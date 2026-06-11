<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo"><Warehouse :size="32" /></div>
        <h1>智能仓储物流管理系统</h1>
        <p>Smart Warehouse Management System</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleLogin" style="width: 100%;">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p>演示账号：admin / 123456</p>
        <p style="margin-top: 8px;">还没有账号？<a @click="goRegister" style="color:#409eff;cursor:pointer;">立即注册</a></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Warehouse } from 'lucide-vue-next'
import { authApi } from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({ username: 'admin', password: '123456' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    const res = await authApi.login({ username: form.username, password: form.password })
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userId', res.data.userId)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('realName', res.data.realName)
    localStorage.setItem('role', res.data.role)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const goRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container { min-height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.login-card { width: 420px; padding: 40px; background: #fff; border-radius: 16px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }
.login-header { text-align: center; margin-bottom: 30px; }
.logo { width: 60px; height: 60px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 12px; display: flex; align-items: center; justify-content: center; color: #fff; margin: 0 auto 16px; }
.login-header h1 { font-size: 22px; color: #303133; margin: 0 0 8px; }
.login-header p { font-size: 13px; color: #909399; margin: 0; }
.login-footer { text-align: center; margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee; }
.login-footer p { font-size: 12px; color: #909399; margin: 0; }
</style>