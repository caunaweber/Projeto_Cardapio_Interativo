import { defineStore } from 'pinia'
import { createComanda } from '@/services/comandaService'

export const useComandaStore = defineStore('comanda', {
  state: () => ({
    comandas: [],
    loading: false,
    error: null,
  }),

  actions: {
    async criarComanda (pedidos, mesaNum) {
      this.loading = true
      this.error = null

      try {
        const itensDto = pedidos.map(item => ({
          pratoId: item.id,
          qtd: item.qtd,
        }))

        const comandaData = {
          mesaNum,
          itens: itensDto,
        }

        const comandaNova = await createComanda(comandaData)
        this.comandas.push(comandaNova)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
