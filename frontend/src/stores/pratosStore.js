import { defineStore } from 'pinia'
import { getPratos, criarPrato, atualizarPrato, removerPrato } from '@/services/pratos'

export const usePratosStore = defineStore('pratos', {
  state: () => ({
    pratos: [],
    categorias: [
      { label: 'Entrada', value: 'ENTRADA' },
      { label: 'Prato Principal', value: 'PRATO_PRINCIPAL' },
      { label: 'Bebida', value: 'BEBIDA' },
      { label: 'Sobremesa', value: 'SOBREMESA' }
    ]
  }),

  actions: {
    async carregarPratos() {
      this.pratos = await getPratos()
    },

    async adicionarPrato(prato) {
      const novoPrato = await criarPrato(prato, prato.file)
      this.pratos.push(novoPrato)
    },

    async atualizarPrato(prato) {
      const atualizado = await atualizarPrato(prato.id, prato)
      const index = this.pratos.findIndex(p => p.id === atualizado.id)
      if (index !== -1) this.pratos[index] = atualizado
    },

    async removerPrato(id) {
      await removerPrato(id)
      this.pratos = this.pratos.filter(p => p.id !== id)
    },
  },
})
