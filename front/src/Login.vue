<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElButton, ElMessage } from 'element-plus'
import axios from 'axios'
import router from './router'
import { Hide, View } from '@element-plus/icons-vue'

onMounted(() => {
  if (showLogin.value) {
    router.push('/')
  }
})

const showLogin = ref(true)
const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 控制密码显示/隐藏
const passwordVisible = ref(false)
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}

//定义表单校验规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /\w/, message: '用户名格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 18, message: '密码长度在6~18之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }, trigger: 'blur'
    }
  ]
})
const formRef = ref()

//是否登录加载中
const isLoading = ref(false)

//当前是否是注册界面
const isRegister = ref(false)

//注册请求
const handleRegister = async () => {
  isLoading.value = true

  try {
    await formRef.value.validate()
    const response = await axios.post('http://localhost:8080/user', {
      username: form.value.username,
      password: form.value.password
    })
    if (response.data === '增加用户成功') {
      ElMessage.success('注册成功')
      isRegister.value = false // 注册成功后返回登录界面
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    ElMessage.error('表单校验失败')
    throw error
  } finally {
    isLoading.value = false
  }
}

//登录请求
const handleLogin = async () => {
  isLoading.value = true;
  localStorage.setItem('isLoggedIn', 'true');

  // try {
  //   await formRef.value.validate();
  //   const response = await axios.post('http://localhost:8080/user/login', {
  //     username: form.value.username,
  //     password: form.value.password
  //   });
  //   if (response.data.includes('true')) {
  //     ElMessage.success('登录成功');
  //     showLogin.value = false;
  //     router.push('/wrapper');
  //   } else {
  //     ElMessage.error(response.data);
  //   }
  // } catch (error) {
  //   console.error('登录请求失败:', error);
  //   ElMessage.error('用户或密码错误');
  //   throw error;
  // } finally {
  //   isLoading.value = false;
  // }
  showLogin.value = false;
  router.push('/wrapper');

};
</script>

<template>
  <div>
    <div class="login-container" v-if="showLogin">
      <div class="login">
        <el-form :model="form" :rules="rules" ref="formRef">
          <h2 class="login-title">{{ isRegister ? '注册' : '登录' }}</h2>
          <el-form-item label="账号 :" prop="username">
            <el-input v-model="form.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item label="密码 :" prop="password">
            <el-input
              v-model="form.password"
              :type="passwordVisible ? 'text' : 'password'"
              placeholder="请输入密码"
            >
              <template #suffix>
                <el-button
                  :icon="passwordVisible ? View : Hide"
                  @click="togglePasswordVisibility"
                  class="hide-button"
                />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item v-if="isRegister" label="确认密码 :" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" />
          </el-form-item>
          <el-form-item class="button-group">
            <el-button type="primary" @click="isRegister ? handleRegister() : handleLogin()" :loading="isLoading">
              {{ isRegister ? '注册' : '登录' }}
            </el-button>
            <el-button type="default" @click="isRegister = !isRegister">
              {{ isRegister ? '返回登录' : '注册' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-title {
  text-align: center;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login {
  width: 300px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-form-item {
  margin-top: 20px;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

.el-button {
  width: 48%;
  margin-top: 10px;
}

/* 保证输入框大小一致 */
.el-input {
  width: 100%;
  height: 40px; /* 确保高度一致 */
  box-sizing: border-box;
}

.hide-button {
  width: auto;
  height: 40px;
  border-radius: 50%;
  color: #606266;
  background: none;
  border: none;
  cursor: pointer;
}
</style>