import { defineStore } from 'pinia'
import {
  deleteAparelho,
  getAllAparelhos,
  invalidateAparelho,
  updateAparelho,
} from '@/services/deviceAdminService'

export const useDeviceAdminStore = defineStore('deviceAdmin', {
  state: () => ({
    aparelhos: [],
  }),

  actions: {
    async carregarAparelhos () {
      this.aparelhos = await getAllAparelhos()
    },

    async atualizarAparelho (aparelho) {
      const atualizado = await updateAparelho(aparelho.id, aparelho)

      const index = this.aparelhos.findIndex(a => a.id === atualizado.id)
      if (index !== -1) {
        this.aparelhos[index] = atualizado
      }
    },

    async removerAparelho (id) {
      await deleteAparelho(id)
      this.aparelhos = this.aparelhos.filter(a => a.id !== id)
    },

    async invalidarAparelho (id) {
      const atualizado = await invalidateAparelho(id)

      const index = this.aparelhos.findIndex(a => a.id === atualizado.id)
      if (index !== -1) {
        this.aparelhos[index] = atualizado
      }
    },
  },
})
