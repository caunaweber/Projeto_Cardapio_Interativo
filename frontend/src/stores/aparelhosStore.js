import { defineStore } from 'pinia'

export const useAparelhosStore = defineStore('aparelhos', {
  state: () => ({
    aparelhos: [
      { id: 1, mesaNum: 1, deviceId: 'ABC123', dataRegistro: new Date().toISOString() },
      { id: 2, mesaNum: 2, deviceId: 'XYZ456', dataRegistro: new Date().toISOString() },
      { id: 3, mesaNum: 3, deviceId: 'QWE789', dataRegistro: new Date().toISOString() },
    ],
  }),
  getters: {
    totalAparelhos: state => state.aparelhos.length,
  },
  actions: {
    adicionarAparelho (aparelho) {
      this.aparelhos.push({
        ...aparelho,
        id: this.aparelhos.length + 1,
        dataRegistro: new Date().toISOString(),
      })
    },
    atualizarAparelho (aparelhoAtualizado) {
      const index = this.aparelhos.findIndex(a => a.id === aparelhoAtualizado.id)
      if (index !== -1) {
        this.aparelhos[index] = { ...aparelhoAtualizado }
      }
    },
    removerAparelho (id) {
      this.aparelhos = this.aparelhos.filter(a => a.id !== id)
    },
  },
})
