import { createRouter, createWebHistory } from 'vue-router'

import Default from '@/layouts/default.vue'

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
    ],
  },
  {
    path: '/admin',
    component: Default,
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

export default router
