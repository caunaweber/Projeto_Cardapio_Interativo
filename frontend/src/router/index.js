import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

import Default from '@/layouts/default.vue'

import AdminLogin from '@/pages/AdminLogin.vue'
import AdminMesas from '@/pages/AdminMesas.vue'
import AdminMetricas from '@/pages/AdminMetricas.vue'
import AdminPratos from '@/pages/AdminPratos.vue'
import Cardapio from '@/pages/Cardapio.vue'
import Comandas from '@/pages/Comandas.vue'

const routes = [
  {
    path: '/',
    component: Default,
    children: [
      { path: '/cardapio',
        name: 'Cardapio',
        component: Cardapio,
      },
      { path: '/comandas',
        name: 'Comandas',
        component: Comandas,
      },
      {
        path: '/login',
        name: 'Login',
        component: AdminLogin,
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
        path: 'mesas',
        name: 'AdminMesas',
        component: AdminMesas,
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

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return next({ name: 'Login' })
  }

  if (to.meta.requiresAdmin && !auth.isAdmin) {
    return next({ name: 'Login' })
  }

  next()
})

export default router
