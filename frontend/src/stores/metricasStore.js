import { defineStore } from 'pinia'

export const useMetricasStore = defineStore('metricas', {
  state: () => ({
    maisVendidos: {
      pratos: [],
      entradas: [],
      sobremesas: [],
      bebidas: [],
    },
    menosVendidos: {
      pratos: [],
      entradas: [],
      sobremesas: [],
      bebidas: [],
    },
  }),

  getters: {
    chartData: state => ({
      labels: ['Pratos', 'Entradas', 'Sobremesas', 'Bebidas'],
      datasets: [
        {
          label: 'Vendas totais',
          backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC'],
          data: [
            state.maisVendidos.pratos.reduce((sum, p) => sum + p.vendas, 0),
            state.maisVendidos.entradas.reduce((sum, e) => sum + e.vendas, 0),
            state.maisVendidos.sobremesas.reduce((sum, s) => sum + s.vendas, 0),
            state.maisVendidos.bebidas.reduce((sum, b) => sum + b.vendas, 0),
          ],
        },
      ],
    }),

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
    carregarMock () {
      this.maisVendidos = {
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

      this.menosVendidos = {
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
    },
  },
})
