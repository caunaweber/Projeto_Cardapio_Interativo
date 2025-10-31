import { defineStore } from 'pinia'
import { atualizarPrato, criarPrato, getPratos, removerPrato } from '@/services/pratosService'

export const usePratosStore = defineStore('pratos', {
  state: () => ({
    pratos: [],
    categorias: [
      { label: 'Entrada', value: 'ENTRADA' },
      { label: 'Prato Principal', value: 'PRATO_PRINCIPAL' },
      { label: 'Bebida', value: 'BEBIDA' },
      { label: 'Sobremesa', value: 'SOBREMESA' },
    ],
    loadingLista: false,
    loadingAcao: false,
    error: null,
  }),

  actions: {
    async carregarPratos () {
      this.loadingLista = true
      this.error = null
      try {
        this.pratos = await getPratos()
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingLista = false
      }
    },

    async adicionarPrato (prato) {
      this.loadingAcao = true
      this.error = null
      try {
        const dadosPayload = {
          nome: prato.nome,
          categoria: prato.categoria,
          preco: prato.preco,
        }
        const novoPrato = await criarPrato(dadosPayload, prato.file)
        this.pratos.push(novoPrato)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async atualizarPrato (prato) {
      this.loadingAcao = true
      this.error = null
      try {
        const dadosPayload = {
          nome: prato.nome,
          categoria: prato.categoria,
          preco: prato.preco,
        }
        const atualizado = await atualizarPrato(prato.id, dadosPayload, prato.file)
        const index = this.pratos.findIndex(p => p.id === atualizado.id)
        if (index !== -1) {
          this.pratos[index] = atualizado
        }
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async removerPrato (id) {
      this.loadingAcao = true
      this.error = null
      try {
        await removerPrato(id)
        this.pratos = this.pratos.filter(p => p.id !== id)
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingAcao = false
      }
    },
  },
})
