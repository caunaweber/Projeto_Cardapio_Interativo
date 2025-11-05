import { defineStore } from 'pinia'
import { atualizarStatusComanda, createComanda, deleteComanda, deleteComandasEntregues, getComandas, updateComanda } from '@/services/comandaService'

export const useComandaStore = defineStore('comanda', {
  state: () => ({
    comandas: [],
    loadingAcao: false,
    loadingLista: false,
    error: null,
  }),

  actions: {
    async criarComanda (pedidos, mesaNum) {
      this.loadingAcao = true
      this.error = null
      try {
        const itensDto = pedidos.map(item => ({
          pratoId: item.id,
          qtd: item.qtd,
        }))

        const comandaData = { mesaNum, itens: itensDto }
        const comandaNova = await createComanda(comandaData)
        this.adicionarComandaFormatada(comandaNova)
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro inesperado.'
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async carregarComandas () {
      this.loadingLista = true
      this.error = null
      try {
        const data = await getComandas()
        // eslint-disable-next-line unicorn/no-array-callback-reference
        this.comandas = data.map(formatarComanda)
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro inesperado.'
        throw error
      } finally {
        this.loadingLista = false
      }
    },

    async atualizarStatusComanda (id, status) {
      this.loadingAcao = true
      this.error = null
      try {
        const atualizada = await atualizarStatusComanda(id, status)
        const idx = this.comandas.findIndex(c => c.id === id)
        if (idx !== -1) {
          this.comandas[idx] = formatarComanda(atualizada)
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro inesperado.'
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async atualizarComanda (id, dto) {
      this.loadingAcao = true
      this.error = null
      try {
        const atualizada = await updateComanda(id, dto)
        const idx = this.comandas.findIndex(c => c.id === id)
        if (idx !== -1) {
          this.comandas.splice(idx, 1, formatarComanda(atualizada))
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro inesperado.'
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    adicionarComandaFormatada (c) {
      this.comandas.unshift(formatarComanda(c))
    },

    async deletarComanda (id) {
      this.loadingAcao = true
      this.error = null
      try {
        await deleteComanda(id)
        this.comandas = this.comandas.filter(c => c.id !== id)
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro inesperado.'
        throw error
      } finally {
        this.loadingAcao = false
      }
    },

    async deletarComandasEntregues () {
      this.loadingAcao = true
      this.error = null
      try {
        await deleteComandasEntregues()
        this.comandas = this.comandas.filter(c => c.status !== 'Entregue')
      } catch (error) {
        this.error = error.response?.data?.message || 'Erro ao excluir comandas entregues.'
        throw error
      } finally {
        this.loadingAcao = false
      }
    },
  },
})

function formatarComanda (c) {
  return {
    id: c.id,
    numero: c.comandaNum,
    mesa: c.mesaNum,
    status: formatarStatus(c.status),
    tempo: formatarTempo(c.dataCriacao),
    acao: getAcao(c.status),
    itens: (c.itens || []).map(item => ({
      id: item.pratoId,
      nome: item.pratoNome,
      qtd: item.qtd,
    })),
  }
}

function formatarStatus (status) {
  switch (status) {
    case 'A_ENTREGAR': { return 'A Entregar'
    }
    case 'ENTREGUE': { return 'Entregue'
    }
    default: { return status
    }
  }
}

function getAcao (status) {
  switch (status) {
    case 'A_ENTREGAR': { return { texto: 'Finalizar', cor: 'success' }
    }
    case 'ENTREGUE': { return { texto: 'Editar', cor: 'info' }
    }
    default: { return { texto: 'N/A', cor: 'grey' }
    }
  }
}

function formatarTempo (dataCriacao) {
  const agora = new Date()
  const inicio = new Date(dataCriacao)
  const diffMs = agora - inicio
  const diffMin = Math.round(diffMs / 60_000)
  return `há ${diffMin} min`
}
