<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>仓库管理系统</h2>
      <el-form :model="form" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item label-width="0">
          <el-button type="primary" style="width: 100%;margin-bottom: 10px" :loading="loading" native-type="submit">
            登录
          </el-button>
          <el-button label-width="0" type="primary" style="width: 100%;margin-left: 0" :loading="loading" @click="zhuce()">
            注册
            </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { userApi } from '../api'
const router = useRouter()
const loading = ref(false)
const form = reactive({
  username: '',
  password: ''
})
const zhuce = async () => {
  await router.push('/zhuce')
}
const handleLogin = async () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {

    const res = await userApi.login({
      username: form.username,
      password: form.password
    })


    // 保存token和用户信息
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('realName', res.data.realName)

    ElMessage.success('登录成功')
    router.push('/')
  } catch (e) {
    // 错误已经在拦截器处理了
  } finally {
    loading.value = false
  }
}

</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: white;
}

.login-card {
  width: 350px;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

</style>
