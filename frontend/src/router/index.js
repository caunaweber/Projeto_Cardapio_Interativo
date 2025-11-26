import { createRouter, createWebHistory } from 'vue-router'
import Default from '@/layouts/default.vue'

import AdminAparelhos from '@/pages/AdminAparelhos.vue'
import AdminLogin from '@/pages/AdminLogin.vue'
import AdminMetricas from '@/pages/AdminMetricas.vue'
import AdminPratos from '@/pages/AdminPratos.vue'
import Cardapio from '@/pages/Cardapio.vue'
import Comandas from '@/pages/Comandas.vue'
import { useAuthStore } from '@/stores/authStore'

const routes = [
  {
    path: '/',
    component: Default,
    children: [
      { path: '/cardapio',
        name: 'Cardapio',
        component: Cardapio,
      },
      {
        path: '/comandas',
        name: 'Comandas',
        component: Comandas,
        meta: { requiresAuth: true, requiresCozinha: true },
      },
      {
        path: '/login',
        name: 'Login',
        component: AdminLogin,
      },
      {
        path: '/comandas/login',
        name: 'LoginCozinha',
        component: () => import('@/pages/CozinhaLogin.vue'),
      },
    ],
  },
  {
    path: '/admin',
    component: Default,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'pratos',
        name: 'AdminPratos',
        component: AdminPratos,
      },
      {
        path: 'aparelhos',
        name: 'AdminAparelhos',
        component: AdminAparelhos,
      },
      {
        path: 'metricas',
        name: 'AdminMetricas',
        component: AdminMetricas,
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.meta.requiresCozinha && !auth.isCozinha) {
    return next({ name: 'LoginCozinha' })
  }

  if (to.meta.requiresAdmin && !auth.isAdmin) {
    return next({ name: 'Login' })
  }

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    if (to.meta.requiresCozinha) {
      return next({ name: 'LoginCozinha' })
    }

    return next({ name: 'Login' })
  }

  next()
})

export default router
