import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('./Login.vue')
  },
  {
    path: '/wrapper',
    component: () => import('./components/wrapper.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router