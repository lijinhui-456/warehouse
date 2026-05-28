<template>
  <div class="goods-page">
    <h3>商品管理</h3>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input v-model="searchName" placeholder="搜索商品名称" style="width: 200px" clearable />
      <el-select v-model="searchCategoryId" placeholder="选择分类" style="width: 150px" clearable>
        <el-option v-for="c in categoryList" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
      <el-button type="primary" @click="loadGoods">搜索</el-button>
      <el-button type="success" @click="handleAdd">新增商品</el-button>
    </div>

    <!-- 商品表格 -->
    <el-table :data="goodsList" border style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column label="分类">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="price" label="单价" width="100">
        <template #default="{ row }">
          ¥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="current"
      v-model:page-size="size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 20px; justify-content: flex-end"
      @current-change="loadGoods"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '新增商品'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option v-for="c in categoryList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { goodsApi, categoryApi } from '../api'

// 搜索条件
const searchName = ref('')
const searchCategoryId = ref(null)

// 表格数据
const goodsList = ref([])
const categoryList = ref([])
const current = ref(1)
const size = ref(10)
const total = ref(0)

// 弹窗
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({
  id: null,
  name: '',
  categoryId: null,
  unit: '件',
  price: 0,
  remark: ''
})

// 加载分类列表
const loadCategories = async () => {
  const res = await categoryApi.list()
  categoryList.value = res.data
}

// 加载商品列表
const loadGoods = async () => {
  const res = await goodsApi.page({
    current: current.value,
    size: size.value,
    name: searchName.value,
    categoryId: searchCategoryId.value
  })
  goodsList.value = res.data.records
  total.value = res.data.total
}

// 获取分类名称
const getCategoryName = (id) => {
  const cat = categoryList.value.find(c => c.id === id)
  return cat ? cat.name : '-'
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, name: '', categoryId: null, unit: '件', price: 0, remark: '' })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

// 保存
const handleSave = async () => {
  if (!form.name || !form.categoryId) {
    ElMessage.warning('请填写必填项')
    return
  }
  try {
    if (isEdit.value) {
      await goodsApi.update(form.id, form)
      ElMessage.success('修改成功')
    } else {
      await goodsApi.add(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadGoods()
  } catch (e) {
    // 错误已处理
  }
}

// 删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？', '提示', { type:'warning'})
    await goodsApi.delete(id)
    ElMessage.success('删除成功')
    loadGoods()
  } catch (e) {
    // 取消或错误
  }
}

onMounted(() => {
  loadCategories()
  loadGoods()
})
</script>

<style scoped>
.goods-page {
  padding: 20px;
}

.search-bar {
  display: flex;
  gap: 10px;
}
</style>
