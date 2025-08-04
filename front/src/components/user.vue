<template>
<div class="user-container">
  <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
    <!-- 用户名部分 -->
    <el-form-item label="用户名" prop="username">
    <div style="display: flex; align-items: center; gap: 8px;">
        <el-input
        v-model="form.username"
        :disabled="!editableUsername"
        placeholder="请输入用户名"
        style="width: 200px;" 
        />
        <el-button type="text" @click="toggleEditUsername">
        {{ editableUsername ? '取消' : '修改' }}
        </el-button>
    </div>
    </el-form-item>
    <!-- 密码修改部分 -->
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input v-model="form.oldPassword" type="password" />
    </el-form-item>

    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="form.newPassword" type="password" />
    </el-form-item>

    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="form.confirmPassword" type="password" />
    </el-form-item>

    <!-- 提交按钮 -->
    <el-form-item>
      <el-button type="primary" @click="handleUpdate">保存修改</el-button>
    </el-form-item>
  </el-form>
</div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const form = reactive({
  username: localStorage.getItem('username') || '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /\w/, message: '用户名格式不正确', trigger: 'blur' }
  ],
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 18, message: '密码长度应在6~18位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

const formRef = ref()
const editableUsername = ref(false)

const toggleEditUsername = () => {
  editableUsername.value = !editableUsername.value
}

const handleUpdate = async () => {
  await formRef.value.validate()
  const res = await axios.put('http://localhost:8080/user/update', {
    username: form.username,
    oldPassword: form.oldPassword,
    newPassword: form.newPassword
  })
  if (res.data === true) {
    ElMessage.success('修改成功')
    editableUsername.value = false
  } else {
    ElMessage.error('修改失败')
  }
}
</script>

<style scoped>
.user-container {
  padding-top: 40px;
}
</style>