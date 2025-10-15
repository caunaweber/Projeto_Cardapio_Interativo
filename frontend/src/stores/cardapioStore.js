import { defineStore } from 'pinia'

import { api } from '@/services/api'

const categoriaMap = {
  PRATO_PRINCIPAL: 'Pratos Principais',
  ENTRADA: 'Entradas',
  SOBREMESA: 'Sobremesas',
  BEBIDA: 'Bebidas',
}

export const useCardapioStore = defineStore('cardapio', {
  state: () => ({
    categorias: [],
    carregando: false,
  }),
  getters: {
    categoriasNomes: state => ['Todas', ...state.categorias.map(c => c.nome)],
  },
  actions: {
    async carregarCardapio () {
      this.carregando = true
      try {
        const { data } = await api.get('/cardapio')
        const categoriasMap = {}

        for (const prato of data) {
          const nomeCategoria = categoriaMap[prato.categoria] || prato.categoria

          if (!categoriasMap[nomeCategoria]) {
            categoriasMap[nomeCategoria] = { nome: nomeCategoria, itens: [] }
          }

          categoriasMap[nomeCategoria].itens.push({
            id: prato.id,
            nome: prato.nome,
            preco: prato.preco,
            imagem: prato.imagem,
            categoria: nomeCategoria,
          })
        }

        this.categorias = Object.values(categoriasMap)
      } catch (error) {
        console.error('Erro ao carregar cardápio', error)
      } finally {
        this.carregando = false
      }
    },
  },
})
