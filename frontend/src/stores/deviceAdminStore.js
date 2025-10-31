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
    loadingLista: false, // Para carregar a lista
    loadingAcao: false, // Para atualizar, remover ou invalidar
    error: null,
  }),

  actions: {
    async carregarAparelhos () {
      this.loadingLista = true
      this.error = null
      try {
        this.aparelhos = await getAllAparelhos()
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingLista = false
      }
    },

    async atualizarAparelho (aparelho) {
      this.loadingAcao = true
      this.error = null
      try {
        const atualizado = await updateAparelho(aparelho.id, aparelho)
        const index = this.aparelhos.findIndex(a => a.id === atualizado.id)
        if (index !== -1) {
          this.aparelhos[index] = atualizado
        }
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async removerAparelho (id) {
      this.loadingAcao = true
      this.error = null
      try {
        await deleteAparelho(id)
        this.aparelhos = this.aparelhos.filter(a => a.id !== id)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async invalidarAparelho (id) {
      this.loadingAcao = true
      this.error = null
      try {
        const atualizado = await invalidateAparelho(id)
        const index = this.aparelhos.findIndex(a => a.id === atualizado.id)
        if (index !== -1) {
          this.aparelhos[index] = atualizado
        }
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },
  },
})
