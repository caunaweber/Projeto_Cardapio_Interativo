import { defineStore } from 'pinia'

export const useCarrinhoStore = defineStore('carrinho', {
  state: () => ({
    pedidos: [],
  }),
  getters: {
    totalPreco: state => state.pedidos.reduce((sum, p) => sum + p.qtd * p.preco, 0),
    totalItens: state => state.pedidos.reduce((sum, p) => sum + p.qtd, 0),
  },
  actions: {
    adicionar (id, categorias) {
      const produto = categorias.flatMap(c => c.itens).find(p => p.id === id)
      const item = this.pedidos.find(p => p.id === id)
      if (item) {
        item.qtd++
      } else {
        this.pedidos.push({ ...produto, qtd: 1 })
      }
    },
    remover (id) {
      const index = this.pedidos.findIndex(p => p.id === id)
      if (index !== -1) {
        if (this.pedidos[index].qtd > 1) {
          this.pedidos[index].qtd--
        } else {
          this.pedidos.splice(index, 1)
        }
      }
    },
    getQtdProduto (id) {
      const item = this.pedidos.find(p => p.id === id)
      return item ? item.qtd : 0
    },
    confirmarPedido () {
      this.pedidos = []
    },
  },
})
