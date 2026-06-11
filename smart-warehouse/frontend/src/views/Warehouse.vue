<template>
  <div class="page-container">
    <div class="page-header">
      <h2>仓库管理</h2>
      <el-button type="primary" @click="showAdd = true"><Plus :size="16" /> 新增仓库</el-button>
    </div>
    <div class="content-card">
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="仓库编码" width="120" />
        <el-table-column prop="name" label="仓库名称" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag>{{ scope.row.type === 'CENTER' ? '中心仓' : '分仓' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="area" label="面积(㎡)" width="100" />
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

    <el-dialog :title="isEdit ? '编辑仓库' : '新增仓库'" v-model="showAdd" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="仓库编码"><el-input v-model="form.code" /></el-form-item>
        <el-form-item label="仓库名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type"><el-option label="中心仓" value="CENTER" /><el-option label="分仓" value="BRANCH" /></el-select>
        </el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contact" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="面积"><el-input-number v-model="form.area" /></el-form-item>
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
import { warehouseApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const showAdd = ref(false)
const isEdit = ref(false)

const form = reactive({ id: 0, code: '', name: '', type: 'CENTER', address: '', contact: '', phone: '', area: 0, status: 1 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await warehouseApi.list({ page: currentPage.value, size: pageSize.value })
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
    await ElMessageBox.confirm('确定删除该仓库吗？', '提示', { type: 'warning' })
    await warehouseApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {}
}

const handleSave = async () => {
  try {
    if (isEdit.value) { await warehouseApi.update(form) } else { await warehouseApi.add(form) }
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
.el-pagination { margin-top: 20px; text-align: right; }
</style>