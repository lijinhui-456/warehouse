import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,  // 前端端口
    proxy: {
      // 把/api请求代理到后端
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
