<template>
  <div class="page-container">
    <div class="page-header">
      <h2>商品管理</h2>
      <el-button type="primary" @click="showAdd = true"><Plus :size="16" /> 新增商品</el-button>
    </div>
    <div class="content-card">
      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索商品名称/编码" clearable @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="商品编码" width="120" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="brand" label="品牌" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="costPrice" label="成本价" width="100" />
        <el-table-column prop="salePrice" label="销售价" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :total="total" :page-size="pageSize" :current-page="currentPage" @current-change="handlePageChange" layout="prev, pager, next, jumper, ->, total" />
    </div>

    <el-dialog :title="isEdit ? '编辑商品' : '新增商品'" v-model="showAdd" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品编码"><el-input v-model="form.code" /></el-form-item>
        <el-form-item label="商品名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="form.categoryName" /></el-form-item>
        <el-form-item label="品牌"><el-input v-model="form.brand" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.unit" /></el-form-item>
        <el-form-item label="成本价"><el-input-number v-model="form.costPrice" :precision="2" /></el-form-item>
        <el-form-item label="销售价"><el-input-number v-model="form.salePrice" :precision="2" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAdd = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from 'lucide-vue-next'
import { goodsApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const keyword = ref('')
const showAdd = ref(false)
const isEdit = ref(false)

const form = reactive({ id: 0, code: '', name: '', categoryName: '', brand: '', unit: '', costPrice: 0, salePrice: 0, status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await goodsApi.list({ page: currentPage.value, size: pageSize.value, keyword: keyword.value })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {} finally { loading.value = false }
}

const handlePageChange = (page) => { currentPage.value = page; loadData() }

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  showAdd.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该商品吗？', '提示', { type: 'warning' })
    await goodsApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {}
}

const handleSave = async () => {
  try {
    if (isEdit.value) { await goodsApi.update(form) } else { await goodsApi.add(form) }
    ElMessage.success('保存成功')
    showAdd.value = false
    loadData()
  } catch (e) {}
}

onMounted(() => loadData())
</script>

<style scoped>
.page-container { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 22px; font-weight: 600; color: #303133; margin: 0; }
.content-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.search-bar { display: flex; gap: 12px; margin-bottom: 20px; }
.search-bar .el-input { width: 300px; }
.el-pagination { margin-top: 20px; text-align: right; }
</style>