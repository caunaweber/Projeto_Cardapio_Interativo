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
      <v-row class="pb-4" dense>
        <v-col cols="12" md="7">
          <v-card
            class="pa-4 elevation-2 rounded-xl hover-card"
            min-height="400px"
            style="border: 2px solid rgba(var(--v-theme-primary), 0.75);"
          >
            <h3 class="mb-4">📊 Vendas por categoria</h3>
            <BarChart :data="chartData" />
          </v-card>
        </v-col>

        <v-col cols="12" md="5">
          <v-card
            class="pa-4 elevation-2 rounded-xl hover-card"
            min-height="400px"
            style="border: 2px solid rgba(var(--v-theme-primary), 0.75);"
          >
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
          cols="12"
          lg="3"
          md="6"
          sm="12"
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
          cols="12"
          lg="3"
          md="6"
          sm="12"
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
  import { computed, ref } from 'vue'
  import BarChart from '@/components/BarChart.vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'
  import MetricCard from '@/components/MetricCard.vue'
  import NavSidebar from '@/components/NavSidebar.vue'
  import PieChart from '@/components/PieChart.vue'
  import { useMetricasStore } from '@/stores/metricasStore'

  const drawer = ref(false)
  const metricasStore = useMetricasStore()

  const chartData = computed(() => metricasStore.chartData)
  const metricasMaisVendidas = computed(() => metricasStore.metricasMaisVendidas)
  const metricasMenosVendidas = computed(() => metricasStore.metricasMenosVendidas)
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
