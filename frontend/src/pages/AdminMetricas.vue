<template>
  <v-container fluid>
    <MenuAppBar title="📊 Gerenciamento - Métricas mensais">
      <template #left>
        <v-btn icon @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>
      </template>
    </MenuAppBar>

    <NavSidebar v-model="drawer" :pagina-atual="'metricas'" />

    <v-container class="py-6" fluid>
      <v-row class="pb-4 " dense>
        <v-col cols="12" md="7">
          <v-card class="pa-4 elevation-2 rounded-xl hover-card" min-height="400px" style="border: 2px solid rgba(var(--v-theme-primary), 0.75);">
            <h3 class="mb-4">📊 Vendas por categoria</h3>
            <BarChart :data="chartData" />
          </v-card>
        </v-col>
        <v-col cols="12" md="5">
          <v-card class="pa-4 elevation-2 rounded-xl hover-card" min-height="400px" style="border: 2px solid rgba(var(--v-theme-primary), 0.75);">
            <h3 class="mb-4">🥧 Participação nas vendas</h3>
            <PieChart :data="chartData" />
          </v-card>
        </v-col>
      </v-row>

      <v-divider />

      <v-row class="mt-4" dense>
        <v-col
          v-for="(categoria, i) in metricasMaisVendidas"
          :key="i"
          class="pb-4"
          cols="12"
          lg="3"
          md="6"
        >
          <MetricCard
            class="hover-card"
            :icon="categoria.icon"
            :icon-color="categoria.color"
            :items="categoria.items"
            :title="categoria.title"
            :total="categoria.items.reduce((sum, item) => sum + item.vendas, 0)"
          />
        </v-col>
        <v-divider />
        <v-col
          v-for="(categoria, i) in metricasMenosVendidas"
          :key="i"
          class="pt-4"
          cols="12"
          lg="3"
          md="6"
        >
          <MetricCard
            class="hover-card"
            :icon="categoria.icon"
            :icon-color="categoria.color"
            :items="categoria.items"
            :title="categoria.title"
            :total="categoria.items.reduce((sum, item) => sum + item.vendas, 0)"
          />
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>
<script setup>
  import { ref } from 'vue'
  import BarChart from '@/components/BarChart.vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'
  import MetricCard from '@/components/MetricCard.vue'
  import NavSidebar from '@/components/NavSidebar.vue'
  import PieChart from '@/components/PieChart.vue'

  const drawer = ref(false)

  const maisVendidos = {
    pratos: [
      { nome: 'Feijoada', vendas: 250 },
      { nome: 'Lasanha', vendas: 320 },
      { nome: 'Strogonoff', vendas: 200 },
      { nome: 'Churrasco', vendas: 180 },
      { nome: 'Moqueca', vendas: 60 },
    ],
    entradas: [
      { nome: 'Bruschetta', vendas: 90 },
      { nome: 'Salada Caesar', vendas: 70 },
      { nome: 'Coxinha', vendas: 60 },
      { nome: 'Bolinho de Bacalhau', vendas: 40 },
      { nome: 'Caldo Verde', vendas: 20 },
    ],
    sobremesas: [
      { nome: 'Petit Gateau', vendas: 110 },
      { nome: 'Mousse de Maracujá', vendas: 95 },
      { nome: 'Tiramisu', vendas: 75 },
      { nome: 'Pudim', vendas: 60 },
      { nome: 'Sorvete', vendas: 45 },
    ],
    bebidas: [
      { nome: 'Coca-Cola', vendas: 200 },
      { nome: 'Suco de Laranja', vendas: 160 },
      { nome: 'Água Mineral', vendas: 140 },
      { nome: 'Cerveja', vendas: 120 },
      { nome: 'Caipirinha', vendas: 80 },
    ],
  }

  const menosVendidos = {
    pratos: [
      { nome: 'Risoto de Camarão', vendas: 15 },
      { nome: 'Quiche', vendas: 10 },
      { nome: 'Sopa de Abóbora', vendas: 8 },
      { nome: 'Cuscuz', vendas: 5 },
      { nome: 'Escondidinho de Carne', vendas: 3 },
    ],
    entradas: [
      { nome: 'Polenta Frita', vendas: 12 },
      { nome: 'Anéis de Cebola', vendas: 9 },
      { nome: 'Salada Grega', vendas: 7 },
      { nome: 'Creme de Abóbora', vendas: 4 },
      { nome: 'Pão de Alho', vendas: 2 },
    ],
    sobremesas: [
      { nome: 'Gelatina', vendas: 6 },
      { nome: 'Manjar Branco', vendas: 4 },
      { nome: 'Rocambole', vendas: 3 },
      { nome: 'Bolo de Fubá', vendas: 2 },
      { nome: 'Curau de Milho', vendas: 1 },
    ],
    bebidas: [
      { nome: 'Chá Gelado', vendas: 5 },
      { nome: 'Suco Detox', vendas: 4 },
      { nome: 'Refrigerante Guaraná', vendas: 3 },
      { nome: 'Café Gelado', vendas: 2 },
      { nome: 'Água de Coco', vendas: 1 },
    ],
  }

  const chartData = {
    labels: ['Pratos', 'Entradas', 'Sobremesas', 'Bebidas'],
    datasets: [
      {
        label: 'Vendas totais',
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC'],
        data: [
          maisVendidos.pratos.reduce((sum, p) => sum + p.vendas, 0),
          maisVendidos.entradas.reduce((sum, e) => sum + e.vendas, 0),
          maisVendidos.sobremesas.reduce((sum, s) => sum + s.vendas, 0),
          maisVendidos.bebidas.reduce((sum, b) => sum + b.vendas, 0),
        ],
      },
    ],
  }

  const metricasMaisVendidas = [
    { title: '🍲 Pratos mais vendidos', items: maisVendidos.pratos, icon: 'mdi-fire', color: 'red' },
    { title: '🥗 Entradas mais vendidas', items: maisVendidos.entradas, icon: 'mdi-fire', color: 'red' },
    { title: '🍰 Sobremesas mais vendidas', items: maisVendidos.sobremesas, icon: 'mdi-fire', color: 'red' },
    { title: '🥤 Bebidas mais vendidas', items: maisVendidos.bebidas, icon: 'mdi-fire', color: 'red' },
  ]

  const metricasMenosVendidas = [
    { title: '🍲 Pratos menos vendidos', items: menosVendidos.pratos, icon: 'mdi-snowflake', color: 'blue' },
    { title: '🥗 Entradas menos vendidas', items: menosVendidos.entradas, icon: 'mdi-snowflake', color: 'blue' },
    { title: '🍰 Sobremesas menos vendidas', items: menosVendidos.sobremesas, icon: 'mdi-snowflake', color: 'blue' },
    { title: '🥤 Bebidas menos vendidas', items: menosVendidos.bebidas, icon: 'mdi-snowflake', color: 'blue' },
  ]
</script>

<style scoped>
.hover-card {
  transition: transform 0.2s, box-shadow 0.2s;
}
.hover-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}
</style>
