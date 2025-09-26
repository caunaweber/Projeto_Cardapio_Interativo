import { defineStore } from 'pinia'

export const usePratosStore = defineStore('pratos', {
  state: () => ({
    pratos: [
      { id: 1, nome: 'Prato 1', categoria: 'Prato Principal', preco: 25, imagem: 'image.png', vendas: 120 },
      { id: 2, nome: 'Suco de Laranja', categoria: 'Bebida', preco: 12, imagem: 'suco.png', vendas: 80 },
      { id: 3, nome: 'Petit Gateau', categoria: 'Sobremesa', preco: 20, imagem: '', vendas: 60 },
    ],
    categorias: ['Entrada', 'Prato Principal', 'Bebida', 'Sobremesa'],
  }),
  getters: {
    totalPratos: (state) => state.pratos.length,
  },
  actions: {
    adicionarPrato(prato) {
      this.pratos.push({
        ...prato,
        id: this.pratos.length + 1,
        vendas: 0,
      })
    },
    atualizarPrato(pratoAtualizado) {
      const index = this.pratos.findIndex(p => p.id === pratoAtualizado.id)
      if (index !== -1) {
        Object.assign(this.pratos[index], pratoAtualizado)
      }
    },
    removerPrato(id) {
      this.pratos = this.pratos.filter(p => p.id !== id)
    },
  },
})
