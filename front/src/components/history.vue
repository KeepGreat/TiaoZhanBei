<template>
  <div class="history-container">
    <el-dialog v-model="showSpecifyDeleteDialog" title="条件删除" width="500px">
      <el-form :model="deleteForm" label-width="100px">
        <el-form-item label="患者姓名">
          <el-input v-model="deleteForm.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="deleteForm.patientGender" placeholder="请选择">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="deleteForm.patientAge" :min="0" />
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="deleteForm.keyword" placeholder="关键词" />
        </el-form-item>
        <el-form-item label="诊断结果">
          <el-input v-model="deleteForm.diagnosis" placeholder="诊断结果" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="deleteForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
            style="width: 350px"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSpecifyDeleteDialog = false">取消</el-button>
        <el-button type="danger" @click="submitSpecifyDelete">确认删除</el-button>
      </template>
    </el-dialog>
    <div class="top-area">
      <div class="title">历史识别记录</div>
      <div class="search-blank">
        <el-input v-model="searchForm.patientName" placeholder="患者姓名" style="width: 160px" clearable />
        <el-select v-model="searchForm.patientGender" placeholder="性别" style="width: 100px" clearable>
          <el-option label="男" value="男" />
          <el-option label="女" value="女" />
        </el-select>
        <el-input-number v-model="searchForm.patientAge" :min="0" placeholder="年龄" style="width: 120px" />
        <el-input v-model="searchForm.keyword" placeholder="关键词" style="width: 160px" clearable />
        <el-input v-model="searchForm.diagnosis" placeholder="诊断结果" style="width: 160px" clearable />
        <el-date-picker
          v-model="searchForm.dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DDTHH:mm:ss"
          style="width: 350px"
          clearable
        />
        <el-button :icon="Search" class="search-button" @click="loadData" />
        <el-button @click="resetSearch" class="reset-button">重置</el-button>
        <el-button class="specify-delete-button" type="danger" @click="SpecifyDeleteButton" >批量删除</el-button>
        <el-button class="batch-delete-button" @click="BatchDeleteButton" :loading="isDeleting">
          {{ isBatchMode ? '取消多选' : '多选' }}
        </el-button>
      </div>
      <div class="history-table">
        <el-table 
          ref="tableRef"
          :data="tableData" 
          style="width: 100%" 
          @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="记录编号" width="120" header-align="center" align="center"></el-table-column>
          <el-table-column prop="patientName" label="患者姓名" width="120" header-align="center" align="center"></el-table-column>
          <el-table-column prop="patientGender" label="性别" width="100" header-align="center" align="center"></el-table-column>
          <el-table-column prop="patientAge" label="年龄" width="100" header-align="center" align="center"></el-table-column>
          <el-table-column label="查看图片"  header-align="center" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" @click="getInfoById(scope.row.id)">查看</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="keyword" label="关键词" width="120" header-align="center" align="center"></el-table-column>
          <el-table-column prop="diagnosis" label="预测结果" width="120" header-align="center" align="center"></el-table-column>

          <el-table-column label="日期" width="200" header-align="center" align="center">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>

          <el-table-column v-if="!isBatchMode" width="55">
            <template #default="scope">
              <el-button @click="handleDelete(scope.row.id)" :icon="Delete" circle size="small"></el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-else
            type="selection"
            width="55"
          />
        </el-table>
      </div>
    </div>

    <div v-if="isBatchMode && multipleSelection.length > 0" style="display: flex; justify-content: flex-end; margin-top: 8px;">
      <el-button type="danger" @click="handleBatchDelete">
        删除所选
      </el-button>
    </div>

    <div class="page-column">
      <div class="goto-box">
        前往
        <input class="goto-input" type="number" v-model.number="gotoPage" min="1" />
        页，共 {{ totalPages }} 页
      </div>
      <div class="page-box" @click="prevPage">
        <el-icon><ArrowLeft /></el-icon>
      </div>
      <div class="page-box page-number">{{ currentPage }}</div>
      <div class="page-box" @click="nextPage">
        <el-icon><ArrowRight /></el-icon>
      </div>
    </div>
    <el-dialog
      v-model="showDetailDialog"
      title="记录详情"
      width="600px"
    >
      <div v-if="detailRecord">
        <p><strong>患者姓名：</strong>{{ detailRecord.patientName }}</p>
        <p><strong>性别：</strong>{{ detailRecord.patientGender }}</p>
        <p><strong>年龄：</strong>{{ detailRecord.patientAge }}</p>
        <p><strong>关键词：</strong>{{ detailRecord.keyword }}</p>
        <p><strong>诊断结果：</strong>{{ detailRecord.diagnosis }}</p>
        <p><strong>创建时间：</strong>{{ formatDate(detailRecord.createdAt) }}</p>
        <div style="margin-top: 12px;">
          <p><strong>伍德灯图像：</strong></p>
          <el-image
            v-if="detailRecord.image1"
            :src="`data:${detailRecord.image1Type};base64,${detailRecord.image1}`"
            :preview-src-list="[`data:${detailRecord.image1Type};base64,${detailRecord.image1}`]"
            style="width: 200px; height: 200px;"
            fit="contain"
          />
        </div>
        <div style="margin-top: 12px;">
          <p><strong>自然光图像：</strong></p>
          <el-image
            v-if="detailRecord.image2"
            :src="`data:${detailRecord.image2Type};base64,${detailRecord.image2}`"
            :preview-src-list="[`data:${detailRecord.image2Type};base64,${detailRecord.image2}`]"
            style="width: 200px; height: 200px;"
            fit="contain"
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref,onMounted,computed,watch,nextTick,onUnmounted } from 'vue';
import { ElTable, ElTableColumn,ElMessage,ElMessageBox } from 'element-plus';
import { Search,ArrowLeft,ArrowRight,Delete, Loading } from '@element-plus/icons-vue';
import axios from 'axios';  

const searchForm = ref({
  patientName: '',
  patientGender: '',
  patientAge: null,
  keyword: '',
  diagnosis: '',
  dateRange: [] // [开始时间, 结束时间]
});

const tableData = ref([]);
//分页功能
const currentPage = ref(1)
const pageSize = ref(7)
const total = ref(0)
const gotoPage = ref(1)
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))
//批量删除
const multipleSelection = ref([])
const isBatchMode = ref(false)
const tableRef = ref(null)
//指定删除
const showSpecifyDeleteDialog = ref(false)
// 详情弹窗
const showDetailDialog = ref(false);
const detailRecord = ref(null);

const deleteForm = ref({
  patientName: '',
  patientGender: '',
  patientAge: null,
  keyword: '',
  diagnosis: '',
  dateRange: []
});

const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

// 分页方法
const prevPage = () => {
  if(currentPage.value > 1) {
    currentPage.value--
    loadData()
  }
}

const nextPage = () => {
  if(currentPage.value < totalPages.value) {
    currentPage.value++
    loadData()
  }
}

watch(gotoPage, (newVal) => {
  if(newVal >= 1 && newVal <= totalPages.value) {
    currentPage.value = newVal
    loadData()
  }
})

const getImageSrc = (row, type) => 
  `data:${row[`image${type}Type`]};base64,${row[`image${type}`]}`

const formatDate = (isoString) => 
  isoString ? isoString.split('T')[0]+" "+isoString.split('T')[1] : ''

const loadData = async () => {
  try {
    const params = {
      ...searchForm.value,
      page: currentPage.value,
      size: pageSize.value,
    };

    if (params.dateRange && params.dateRange.length === 2) {
      params.queryStart = params.dateRange[0];
      params.queryEnd = params.dateRange[1];
    }
    delete params.dateRange;

    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null) {
        delete params[key];
      }
    });

    const res = await axios.get(`http://localhost:8080/info/${currentPage.value}/${pageSize.value}`, {
      params
    });

     tableData.value = res.data.records.map(item => ({
      id: item.id,
      patientName: item.patientName,
      patientGender: item.patientGender,
      patientAge: item.patientAge,
      keyword: item.keyword,
      diagnosis: item.diagnosis,
      createdAt: item.createdAt,
      // 不包含 image1 / image2 这些大字段
    }));
    total.value = res.data.total;
  } catch (e) {
    console.error('加载数据失败', e);
  }
};

const getInfoById = async (id) => {
  try {
    const res = await axios.get(`http://localhost:8080/info/${id}`);
    detailRecord.value = res.data;
    showDetailDialog.value = true;
  } catch (err) {
    console.error('获取记录详情失败', err);
    ElMessage.error('获取记录详情失败');
  }
};

const resetSearch = () => {
  searchForm.value = {
    patientName: '',
    patientGender: '',
    patientAge: null,
    keyword: '',
    diagnosis: '',
    dateRange: []
  };
  loadData();
};

//单独删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      `确定删除记录 ${id} 吗？此操作不可恢复。`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    await axios.delete(`http://localhost:8080/info/${id}`)
    
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除记录失败', error)
    ElMessage.error('删除记录失败')
  }
}

const BatchDeleteButton = () => {
  isBatchMode.value = !isBatchMode.value
  multipleSelection.value = []
  nextTick(() => {
    tableRef.value?.clearSelection()
  })
}

const isDeleting = ref(false)
//批量删除
const handleBatchDelete = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请先选择要删除的记录');
    return;
  }

  try {
    await ElMessageBox.confirm(
      '确认删除所选记录？此操作无法撤销！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );

    isDeleting.value = true;

    for (const item of multipleSelection.value) {
      const res = await axios.delete(`http://localhost:8080/info/${item.id}`);
      if (res.data !== '删除记录成功') {
        throw new Error(`删除 ID=${item.id} 失败`);
      }
    }

    ElMessage.success('批量删除成功');
    loadData();
    isBatchMode.value = false;
    multipleSelection.value = [];

  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败');
      console.error(error);
    }
  }
  isDeleting.value = false;
};

const SpecifyDeleteButton = () => {
  showSpecifyDeleteDialog.value = true
}

const submitSpecifyDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确认根据所选条件删除记录？此操作无法撤销！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const payload = { ...deleteForm.value };

    if (payload.dateRange && payload.dateRange.length === 2) {
      payload.queryStart = payload.dateRange[0];
      payload.queryEnd = payload.dateRange[1];
    }
    delete payload.dateRange;

    for (const key in payload) {
      if (payload[key] === '') {
        payload[key] = null;
      }
    }

    const res = await axios.post('http://localhost:8080/info/delete', payload)

    if (res.data !== '删除记录成功') {
      throw new Error('删除记录失败')
    }

    ElMessage.success('删除成功')
    showSpecifyDeleteDialog.value = false
    loadData()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('删除失败', err)
      ElMessage.error('删除失败')
    }
  }
}

// 图片预览打开时锁定滚动，关闭时解锁
const onPreviewOpen = () => {
  document.body.classList.add('image-viewer-open');
};
const onPreviewClose = () => {
  document.body.classList.remove('image-viewer-open');
};

onMounted(loadData)
</script>

<style scoped>
.history-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 24px;
  box-sizing: border-box;
  height: 100%;
  gap: 16px;
}

.top-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
  min-height: 0; /* 避免溢出 */
  overflow-y: auto;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 0;
}

.search-blank {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-button{
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  margin-right: 0;
}

.reset-button{
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  margin-left: 0;
}

.specify-delete-button {
  margin-left: auto;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.batch-delete-button {
  margin-left: 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.history-table{
  flex: 1;
  overflow-y: auto;
}

.goto-box {
  font-weight: normal;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-right: 12px;
  font-size: 16px;
}

.goto-input {
  width: 40px;
  height: 24px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  text-align: center;
  margin: 0 4px;
}

.page-column {
  font-weight: normal;
  font-family: "SimSun", "宋体", serif;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
  /* 保持在底部 */
}

.page-box {
  font-weight: normal;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #acacac11;
  font-size: 18px;
  cursor: pointer;
}

.page-number {
  font-weight: normal;
  color: #000000;
  cursor: default;
}
</style>

<style>
/* 提升图片预览层级，确保在所有元素上方 */
.el-image-viewer__wrapper {
  z-index: 99999 !important;
  pointer-events: auto !important;
}

/* 遮罩层阻止点击事件穿透到下层表格 */
.el-image-viewer__mask {
  background-color: rgba(0,0,0,0.7) !important;
  pointer-events: auto !important;
}

/* 预览时禁止页面滚动和操作 */
body.image-viewer-open {
  overflow: hidden !important;
  user-select: none;
  pointer-events: none;
}

/* 允许图片预览区域可操作 */
body.image-viewer-open .el-image-viewer__wrapper {
  pointer-events: auto !important;
}
</style>