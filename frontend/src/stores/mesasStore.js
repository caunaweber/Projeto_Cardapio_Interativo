import { defineStore } from 'pinia'

export const useMesasStore = defineStore('mesas', {
  state: () => ({
    mesas: [
      { id: 1, numero: 1, IDvinc: '#1' },
      { id: 2, numero: 2, IDvinc: '#2' },
      { id: 3, numero: 3, IDvinc: '#3' },
    ],
  }),
  getters: {
    totalMesas: (state) => state.mesas.length,
  },
  actions: {
    adicionarMesa(mesa) {
      this.mesas.push({
        ...mesa,
        id: this.mesas.length + 1,
      })
    },
    atualizarMesa(mesaAtualizada) {
      const index = this.mesas.findIndex(m => m.id === mesaAtualizada.id)
      if (index !== -1) {
        this.mesas[index] = { ...mesaAtualizada }
      }
    },
    removerMesa(id) {
      this.mesas = this.mesas.filter(m => m.id !== id)
    },
  },
})
