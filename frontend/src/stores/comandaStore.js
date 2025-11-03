import { defineStore } from 'pinia'
import { createComanda, getComandas } from '@/services/comandaService'

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

        const comandaData = {
          mesaNum,
          itens: itensDto,
        }

        const comandaNova = await createComanda(comandaData)
        this.adicionarComandaFormatada(comandaNova)
      } catch (error) {
        this.error = error
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

        this.comandas = data.map(c => ({
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
        }))
      } catch (error) {
        this.error = error
        throw error
      } finally {
        this.loadingLista = false
      }
    },

    adicionarComandaFormatada (c) {
      const comandaFormatada = {
        id: c.id,
        numero: c.comandaNum,
        mesa: c.mesaNum,
        status: formatarStatus(c.status),
        tempo: formatarTempo(c.dataCriacao),
        acao: getAcao(c.status),
        itens: (c.itens || []).map(item => ({
          id: item.pratoId,
          nome: item.nomePrato,
          qtd: item.qtd,
        })),
      }
      this.comandas.unshift(comandaFormatada)
    },
  },
})

function formatarStatus (status) {
  switch (status) {
    case 'A_ENTREGAR': {
      return 'A Entregar'
    }
    case 'ENTREGUE': {
      return 'Entregue'
    }
    default: {
      return status
    }
  }
}

function getAcao (status) {
  switch (status) {
    case 'A_ENTREGAR': {
      return { texto: 'Marcar como Pronto', cor: 'success' }
    }
    case 'ENTREGUE': {
      return { texto: 'Editar', cor: 'info' }
    }
    default: {
      return { texto: 'N/A', cor: 'grey' }
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
