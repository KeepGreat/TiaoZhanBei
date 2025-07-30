import { createRouter, createWebHistory } from 'vue-router'
import Login from './Login.vue'
import Wrapper from './components/wrapper.vue'
import History from './components/history.vue'
import ImageRecognition from './components/ImageRecognition.vue';

const routes = [
  { path: '/', component: Login },
  {
    path: '/wrapper',
    component: Wrapper,
    children: [
      { path: 'history', component: History },
      { path: 'image-recognition', component: ImageRecognition }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  
  if (to.path === '/') {
    // 已登录用户禁止返回登录页
    isLoggedIn ? next('/wrapper') : next(); 
  } else {
    // 保护所有非登录页
    isLoggedIn ? next() : next('/');
  }
});

export default router