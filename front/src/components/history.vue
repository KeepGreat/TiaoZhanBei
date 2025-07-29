<template>
  <div class="history-container">
    <div class="top-area">
      <div class="title">历史识别记录</div>
      <div class="search-blank">
        <el-input v-model="input" style="width: 240px" placeholder="请输入姓名" />
        <el-button :icon="Search" circle />
        <el-button>重置</el-button>
      </div>
      <div class="history-table">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="id" label="记录编号" width="100"></el-table-column>
          <el-table-column prop="name" label="姓名"></el-table-column>
          <el-table-column prop="age" label="年龄"></el-table-column>
          <el-table-column prop="gender" label="性别"></el-table-column>
          <el-table-column prop="wood-lamp" label="伍德灯图像"></el-table-column>
          <el-table-column prop="natural-light" label="自然光图像"></el-table-column>
          <el-table-column prop="prediction" label="预测结果"></el-table-column>
          <el-table-column prop="date" label="日期"></el-table-column>
        </el-table>
      </div>
    </div>

    <div class="page-column">
      <div class="goto-box">
        前往
        <input class="goto-input" type="number" v-model="gotoPage" min="1" />
        页，共 {{ totalPage }} 页
      </div>
      <div class="page-box"><el-icon><ArrowLeft /></el-icon></div>
      <div class="page-box page-number">1</div>
      <div class="page-box"><el-icon><ArrowRight /></el-icon></div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import { ElTable, ElTableColumn } from 'element-plus';
import { Search,ArrowLeft,ArrowRight } from '@element-plus/icons-vue';

const input = ref('');
const tableData = ref([
  {
    id: 1,
    name: '张三',
    age: 28,
    gender: '男',
    // wood-lamp: '', // 图像数据暂不添加
    // natural-light: '', // 图像数据暂不添加
    prediction: '正常',
    date: '2025-07-25'
  },
  {
    id: 2,
    name: '李四',
    age: 34,
    gender: '女',
    prediction: '异常',
    date: '2025-07-24'
  },
  {
    id: 3,
    name: '王五',
    age: 22,
    gender: '男',
    prediction: '正常',
    date: '2025-07-23'
  }
]);
const gotoPage = ref(1);
const totalPage = ref(10); // 假设总页数为10，后续可用API结果替换
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
  gap: 12px;
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