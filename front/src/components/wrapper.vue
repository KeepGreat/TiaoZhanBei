<template>
  <div>
    <header>
      <div class="wrapper">
        <div class="list" :style="{ width: isCollapse ? '63.33px' : '20%' }">
          <div class="title" @click="isCollapse = !isCollapse">导航</div>
          <div class="hamburger" @click="isCollapse = !isCollapse">
            <el-icon><Expand /></el-icon>
          </div>
          <el-col>
            <el-menu
              :collapse="isCollapse"
              active-text-color="#ffd04b"
              background-color="#545c64"
              class="el-menu-vertical-demo"
              default-active="2"
              text-color="#fff"
              router
            >
              <el-menu-item index="">
                <el-icon><location /></el-icon>
                <span>一</span>
              </el-menu-item>
              <el-menu-item index="/wrapper/history">
                <el-icon><location /></el-icon>
                <span>历史记录</span>
              </el-menu-item>
              <el-menu-item index="">
                <el-icon><location /></el-icon>
                <span>三</span>
              </el-menu-item>
              <el-menu-item index="">
                <el-icon><location /></el-icon>
                <span>四</span>
              </el-menu-item>
            </el-menu>
          </el-col>
        </div>
        <div class="container" :style="{ width: isCollapse ? 'calc(100% - 63.33px)' : '80%' }">
          <div class="welcome">
            <div class="welcome-title">欢迎使用白癜风预测系统</div>
            <div class="admin" style="align-items: center; display: flex;">
              <el-dropdown :trigger="['click']">
                <span class="el-dropdown-link">
                  <el-avatar :size="15" :src="circleUrl" style="margin-right: 8px;" /> <span>admin</span>
                  <el-icon class="el-icon--right">
                    <arrow-down />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item>登出</el-dropdown-item>
                    <el-dropdown-item>修改密码</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <div class="show">
            <RouterView />
          </div>
        </div>
      </div>
    </header>
  </div>
</template>
  
  <script setup>
  import { ref, reactive, toRefs, onMounted, onBeforeUnmount } from 'vue'
  import { ArrowDown, Expand } from '@element-plus/icons-vue'
  import { RouterLink, RouterView } from 'vue-router'
  
  const state = reactive({
    circleUrl:
      'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
    sizeList: ['small', '', 'large'],
  })
  
  const { circleUrl, sizeList } = toRefs(state)
  const isCollapse = ref(false) 
  //检测视口变化
  const checkMobile = () => {
    isCollapse.value = window.innerWidth <= 768;
  };
  
  onMounted(() => {
    checkMobile();
    window.addEventListener('resize', checkMobile);
  });
  
  onBeforeUnmount(() => {
    window.removeEventListener('resize', checkMobile);
  });
  </script>
  
  <style scoped>
.wrapper,
header,
.container {
  height: 100vh;
  box-sizing: border-box;
  overflow: hidden;
}

  .el-icon--right {
  margin-left: 0%;
  width: var(--el-menu-icon-width);
}
.wrapper {
  display: flex;
  width: 100%;
  height: 100vh;
}
.list {
  width: 20%;
  height: 100%;
  background-color: #565c63;
  flex-shrink: 0;
  justify-content: center;
  color: white;
  border-top-left-radius: 10px;
  transition: width 0.3s ease-in-out;
}
.container {
  width: 80%;
  height: 100%;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease-in-out;
}
.title {
  height: 6%;
  align-items: center;
  justify-content: center;
  background: rgb(15, 1, 1);
  border-top-left-radius: 10px;
  width: 100%;
  display: flex;
}
.welcome {
  font-size: 24px;
  font-weight: bold;
  width: 100%;
  display: flex;
  background-color: #ffffff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 20px;
  flex-shrink: 0;
}
.welcome-title {
  width: 80%;
  text-align: center;
}
.show {
  flex: 1 1 auto;
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}
header {
  height: 100vh;
}
.el-dropdown-link:focus {
  outline: none !important;
}
.el-dropdown-link {
  align-items: center;
  display: flex;
}
/* 基本的 RouterLink 样式 */
.el-menu-item a {
  color: inherit; /* 继承父元素的文本颜色 */
  text-decoration: none; /* 去掉下划线 */
  display: block;
  width: 100%;
  height: 100%;
}

@media (max-width: 1200px) {
  .container {
    width: 75%;
  }
  .welcome-title {
    font-size: 22px;
  }
}

@media (max-width: 992px) {
  .container {
    width: 70%;
  }
  .title {
    font-size: 14px;
  }
}

@media (max-width: 768px) {
  .el-sub-menu__hide-arrow .el-sub-menu__icon-arrow {
    display: none !important;
    width: 0% !important;
  }

  /* 隐藏admin文字 */
  .el-dropdown-link > span:not(.el-icon) {
    display: none !important;
  }

  /* 调整下拉图标位置 */
  .el-icon--right {
    margin-left: 4px !important;
  }
  .container {
    width: calc(100% - 60px) !important;
  }
  .el-menu-vertical-demo {
    width: 100%;
  }
  .welcome-title {
    font-size: 20px;
  }

  /* 新增图标模式样式 */
  .list {
    width: 64px !important;
  }
  .container {
    width: calc(100% - 64px) !important;
  }
  .el-menu-item span,
  .el-menu-item,
  .el-sub-menu__title {
    justify-content: center;
  }
  .el-sub-menu__title > .el-icon {
    margin-right: 0 !important;
  }
  :deep(.el-avatar) > span {
    display: none !important;
  }
  .el-sub-menu__title {
    text-align: center !important;
  }
}

@media (max-width: 576px) {
  .welcome-title {
    font-size: 18px;
  }
  .title {
    font-size: 12px;
  }
  .el-menu-item span {
    font-size: 12px;
  }
}
  </style>