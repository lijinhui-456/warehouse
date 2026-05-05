<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>仓库管理系统 - 注册</h2>
      <el-form :model="form">
        <el-form-item label-width="0">
          <el-input v-model="form.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item label-width="0">
          <el-input v-model="form.password" type="password" placeholder="密码" />
          <el-input v-model="form.password1" type="password" style="margin-top: 15px" placeholder="确认密码" />
        </el-form-item>
        <el-form-item label-width="0">
          <el-button type="primary" style="width: 100%;" @click="handleRegister">注册</el-button>
          <el-button type="primary" style="width: 100%;margin-left: 0;margin-top: 15px" @click="denglu">返回登录页面</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from "axios";


const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  password1: '',
  realName:'普通员工'
})

const denglu=async () => {
  await router.push('/login')
}
const handleRegister = async () => {
  if (!form.username || !form.password) {
    return ElMessage.warning('请输入用户名和密码')
  }else if(!(form.password ===form.password1)) {
    ElMessage.warning("密码不一致")
    return
  }
  try {
     const res=await axios.post('http://localhost:8080/api/user/zhuce',{username:form.username,password:form.password,realName:form.realName})
     if(res.data.code===200){
       ElMessage.success(res.data.message)
        router.push('/login')
     }else if (res.data.code===500)
    {
       ElMessage.error(res.data.message)
     }else{
       ElMessage.error("注册失败")
     }
  } catch (err) {
    ElMessage.error("注册失败")
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.login-card {
  width: 350px;
  padding: 20px;
  border-radius: 8px;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
}
</style>