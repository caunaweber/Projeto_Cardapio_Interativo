import { defineStore } from 'pinia'
import { getMetricas } from '@/services/pratosService'

export const useMetricasStore = defineStore('metricas', {
  state: () => ({
    dadosBrutos: [],
    loading: false,
    error: null,
  }),

  getters: {
    dadosPorCategoria: state => {
      const grupos = {
        PRATO_PRINCIPAL: [],
        ENTRADA: [],
        SOBREMESA: [],
        BEBIDA: [],
      }

      for (const item of state.dadosBrutos) {
        if (Array.isArray(grupos[item.categoria])) {
          grupos[item.categoria].push(item)
        }
      }
      return grupos
    },

    getTop5: () => (lista, ordem = 'desc') => {
      const list = lista || []

      return [...list]
        .sort((a, b) => ordem === 'desc' ? b.vendas - a.vendas : a.vendas - b.vendas)
        .slice(0, 5)
    },

    maisVendidos: state => {
      const grupos = state.dadosPorCategoria
      const top5 = state.getTop5
      return {
        pratos: top5(grupos.PRATO_PRINCIPAL, 'desc'),
        entradas: top5(grupos.ENTRADA, 'desc'),
        sobremesas: top5(grupos.SOBREMESA, 'desc'),
        bebidas: top5(grupos.BEBIDA, 'desc'),
      }
    },

    menosVendidos: state => {
      const grupos = state.dadosPorCategoria
      const bottom5 = state.getTop5
      return {
        pratos: bottom5(grupos.PRATO_PRINCIPAL, 'asc'),
        entradas: bottom5(grupos.ENTRADA, 'asc'),
        sobremesas: bottom5(grupos.SOBREMESA, 'asc'),
        bebidas: bottom5(grupos.BEBIDA, 'asc'),
      }
    },

    chartData: state => {
      const grupos = state.dadosPorCategoria
      const totalVendas = lista => lista.reduce((sum, item) => sum + item.vendas, 0)

      return {
        labels: ['Pratos', 'Entradas', 'Sobremesas', 'Bebidas'],
        datasets: [
          {
            label: 'Vendas totais',
            backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC'],
            data: [
              totalVendas(grupos.PRATO_PRINCIPAL),
              totalVendas(grupos.ENTRADA),
              totalVendas(grupos.SOBREMESA),
              totalVendas(grupos.BEBIDA),
            ],
          },
        ],
      }
    },

    metricasMaisVendidas: state => [
      { title: '🍲 Pratos mais vendidos', items: state.maisVendidos.pratos, icon: 'mdi-fire', color: 'red' },
      { title: '🥗 Entradas mais vendidas', items: state.maisVendidos.entradas, icon: 'mdi-fire', color: 'red' },
      { title: '🍰 Sobremesas mais vendidas', items: state.maisVendidos.sobremesas, icon: 'mdi-fire', color: 'red' },
      { title: '🥤 Bebidas mais vendidas', items: state.maisVendidos.bebidas, icon: 'mdi-fire', color: 'red' },
    ],

    metricasMenosVendidas: state => [
      { title: '🍲 Pratos menos vendidos', items: state.menosVendidos.pratos, icon: 'mdi-snowflake', color: 'blue' },
      { title: '🥗 Entradas menos vendidas', items: state.menosVendidos.entradas, icon: 'mdi-snowflake', color: 'blue' },
      { title: '🍰 Sobremesas menos vendidas', items: state.menosVendidos.sobremesas, icon: 'mdi-snowflake', color: 'blue' },
      { title: '🥤 Bebidas menos vendidas', items: state.menosVendidos.bebidas, icon: 'mdi-snowflake', color: 'blue' },
    ],
  },

  actions: {
    async carregarMetricas () {
      this.loading = true
      this.error = null
      try {
        const dados = await getMetricas()

        this.dadosBrutos = dados.map(item => ({
          ...item,
          vendas: item.vendas === undefined ? (item.vendasCounter || 0) : item.vendas,
        }))
      } catch (error) {
        console.error('Erro ao carregar métricas:', error)
        this.error = 'Falha ao carregar métricas.'
      } finally {
        this.loading = false
      }
    },
  },
})
