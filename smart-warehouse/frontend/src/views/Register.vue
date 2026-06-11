<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo"><UserPlus :size="32" /></div>
        <h1>用户注册</h1>
        <p>Smart Warehouse Management System</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="3-20个字符，注册后不可更改" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="6-32个字符" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" prefix-icon="Lock" size="large" show-password @keyup.enter="handleRegister" />
        </el-form-item>
        <el-form-item label="验证码" prop="captchaCode" :rules="captchaRules">
          <div class="captcha-row">
            <el-input v-model="form.captchaCode" placeholder="请输入右侧验证码" prefix-icon="Key" size="large" style="flex: 1;" @keyup.enter="handleRegister" />
            <img
              v-if="form.captchaImage"
              :src="form.captchaImage"
              class="captcha-img"
              title="点击刷新验证码"
              @click="refreshCaptcha"
            />
            <div v-else class="captcha-img captcha-loading" @click="refreshCaptcha">Loading...</div>
          </div>
          <div class="captcha-tip">看不清？点击图片刷新</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleRegister" style="width: 100%;">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <p>已有账号？<a @click="goLogin">立即登录</a></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UserPlus, User, Lock, Key } from 'lucide-vue-next'
import { authApi } from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  realName: '',
  password: '',
  confirmPassword: '',
  captchaId: '',
  captchaCode: '',
  captchaImage: '',
  captchaRequired: true
})

const validateConfirm = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度需在3-20个字符之间', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 32, message: '长度需在6-32个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirm, trigger: 'blur' }
  ]
}

// 验证码规则动态化：当 captchaRequired=true 时才强制校验
const captchaRules = computed(() => {
  if (form.captchaRequired) {
    return [{ required: true, message: '请输入验证码', trigger: 'blur' }]
  }
  return []
})

const refreshCaptcha = async () => {
  try {
    const res = await authApi.getCaptcha()
    if (res && res.data && res.data.captchaImage) {
      form.captchaId = res.data.captchaId
      form.captchaImage = res.data.captchaImage
    } else {
      // 后端没有返回图片（可能是降级/Redis未启动），关闭验证码输入框的必填校验
      form.captchaId = ''
      form.captchaImage = ''
      form.captchaRequired = false
      ElMessage.warning('验证码服务暂不可用，已跳过验证码')
    }
  } catch (e) {
    // 验证码接口异常时：清空并关闭验证码校验，让注册仍可进行
    form.captchaId = ''
    form.captchaImage = ''
    form.captchaRequired = false
    console.warn('refreshCaptcha failed:', e)
  }
}

const handleRegister = async () => {
  // 表单校验
  let valid = false
  try {
    await formRef.value.validate()
    valid = true
  } catch (err) {
    // 字段没通过校验
    ElMessage.error('请检查表单：用户名(3-20位)、密码(6-32位)、验证码是否填对')
    return
  }

  if (!valid) return

  try {
    loading.value = true
    await authApi.register({
      username: form.username,
      password: form.password,
      realName: form.realName,
      captchaId: form.captchaId || '',
      captchaCode: form.captchaCode || ''
    })
    ElMessage.success('注册成功，即将跳转到登录页')
    setTimeout(() => {
      router.push('/login')
    }, 1000)
  } catch (e) {
    // 接口层面的错误（用户名重复、验证码错误等），request.js 拦截器已经 ElMessage 提示
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

const goLogin = () => {
  router.push('/login')
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 440px;
  padding: 40px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-header {
  text-align: center;
  margin-bottom: 24px;
}
.logo {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin: 0 auto 16px;
}
.login-header h1 {
  font-size: 22px;
  color: #303133;
  margin: 0 0 8px;
}
.login-header p {
  font-size: 13px;
  color: #909399;
  margin: 0;
}
.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
}
.captcha-img {
  width: 120px;
  height: 40px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  object-fit: cover;
  background: #f5f7fa;
  flex-shrink: 0;
}
.captcha-img:hover {
  opacity: 0.85;
}
.captcha-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #909399;
}
.captcha-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 6px;
}
.login-footer {
  text-align: center;
  margin-top: 16px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
.login-footer p {
  font-size: 13px;
  color: #606266;
  margin: 0;
}
.login-footer a {
  color: #409eff;
  cursor: pointer;
  text-decoration: none;
}
.login-footer a:hover {
  text-decoration: underline;
}
</style>
