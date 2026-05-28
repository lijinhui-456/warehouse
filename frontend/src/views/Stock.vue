<template>
  <div class="stock-page">
    <h3>入库 / 出库</h3>

    <!-- 操作卡片 -->
    <div class="操作区">
      <!-- 入库 -->
      <el-card class="操作卡片">
        <template #header>
          <span>📥 入库</span>
        </template>
        <el-form :model="inForm" label-width="70px">
          <el-form-item label="商品">
            <el-select v-model="inForm.goodsId" placeholder="请选择商品" style="width: 100%">
              <el-option v-for="g in goodsList" :key="g.id" :label="g.name" :value="g.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="数量">
            <el-input-number v-model="inForm.num" :min="1" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="inForm.remark" type="textarea" placeholder="可选" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleIn" :loading="inLoading">确认入库</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 出库 -->
      <el-card class="操作卡片">
        <template #header>
          <span>📤 出库</span>
        </template>
        <el-form :model="outForm" label-width="70px">
          <el-form-item label="商品">
            <el-select v-model="outForm.goodsId" placeholder="请选择商品" style="width: 100%">
              <el-option v-for="g in goodsList" :key="g.id" :label="g.name" :value="g.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="数量">
            <el-input-number v-model="outForm.num" :min="1" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="outForm.remark" type="textarea" placeholder="可选" />
          </el-form-item>
          <el-form-item>
            <el-button type="warning" @click="handleOut" :loading="outLoading">确认出库</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 当前库存 -->
      <el-card class="库存卡片">
        <template #header>
          <span>📦 当前库存</span>
        </template>
        <div class="库存列表">
          <div v-for="item in stockList" :key="item.id" class="库存项">
            <span class="商品">{{ item.goodsName }}</span>
            <span class="数量">{{ item.num }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { goodsApi, stockApi } from '../api'

const goodsList = ref([])
const stockList = ref([])

const inForm = reactive({ goodsId: null, num: 1, remark: '' })
const outForm = reactive({ goodsId: null, num: 1, remark: '' })
const inLoading = ref(false)
const outLoading = ref(false)

// 加载商品列表
const loadGoods = async () => {
  const res = await goodsApi.list()
  goodsList.value = res.data
}

// 加载库存
const loadStock = async () => {
  const res = await stockApi.list()
  stockList.value = res.data
}

// 入库
const handleIn = async () => {
  if (!inForm.goodsId) {
    ElMessage.warning('请选择商品')
    return
  }
  inLoading.value = true
  try {
    await stockApi.in({ goodsId: inForm.goodsId, num: inForm.num, remark: inForm.remark })
    ElMessage.success('入库成功')
    inForm.num = 1
    inForm.remark = ''
    loadStock()
  } catch (e) {
    // 错误已处理
  } finally {
    inLoading.value = false
  }
}

// 出库
const handleOut = async () => {
  if (!outForm.goodsId) {
    ElMessage.warning('请选择商品')
    return
  }
  outLoading.value = true
  try {
    await stockApi.out({ goodsId: outForm.goodsId, num: outForm.num, remark: outForm.remark })
    ElMessage.success('出库成功')
    outForm.num = 1
    outForm.remark = ''
    loadStock()
  } catch (e) {
    // 错误已处理
  } finally {
    outLoading.value = false
  }
}

onMounted(() => {
  loadGoods()
  loadStock()
})
</script>

<style scoped>
.stock-page {
  padding: 20px;
}

.操作区 {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.操作卡片 {
  flex: 1;
  max-width: 350px;
}

.库存卡片 {
  flex: 1;
  max-width: 350px;
}

.库存列表 {
  max-height: 300px;
  overflow-y: auto;
}

.库存项 {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.库存项:last-child {
  border-bottom: none;
}

.商品 {
  color: #666;
}

.数量 {
  font-weight: bold;
  color: #409EFF;
}
</style>
