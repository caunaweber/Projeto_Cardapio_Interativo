import { defineStore } from 'pinia'

export const useComandaStore = defineStore('comanda', {
  state: () => ({
    comandas: [
      {
        mesa: 1,
        status: 'A Entregar',
        numero: 101,
        itens: [
          { qtd: 1, nome: 'Filé Mignon' },
          { qtd: 2, nome: 'Batata Frita' },
          { qtd: 1, nome: 'Coca-Cola' },
        ],
        tempo: '12:25',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 2,
        status: 'Entregue',
        numero: 102,
        itens: [
          { qtd: 1, nome: 'Prato Executivo' },
          { qtd: 1, nome: 'Suco Natural' },
        ],
        tempo: '08:40',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 3,
        status: 'A Entregar',
        numero: 103,
        itens: [
          { qtd: 2, nome: 'Strogonoff de Frango' },
          { qtd: 2, nome: 'Água com Gás' },
        ],
        tempo: '05:15',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 4,
        status: 'Entregue',
        numero: 104,
        itens: [
          { qtd: 1, nome: 'Risoto de Cogumelos' },
          { qtd: 1, nome: 'Vinho Tinto Taça' },
        ],
        tempo: '15:35',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 5,
        status: 'A Entregar',
        numero: 105,
        itens: [
          { qtd: 3, nome: 'Cerveja Artesanal' },
          { qtd: 1, nome: 'Tábua de Queijos' },
        ],
        tempo: '18:10',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 6,
        status: 'Entregue',
        numero: 106,
        itens: [
          { qtd: 1, nome: 'Frango à Parmegiana' },
          { qtd: 1, nome: 'Chá Gelado' },
          { qtd: 1, nome: 'Petit Gâteau' },
        ],
        tempo: '09:50',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 7,
        status: 'A Entregar',
        numero: 107,
        itens: [
          { qtd: 1, nome: 'Lasanha à Bolonhesa' },
          { qtd: 2, nome: 'Coca-Cola' },
        ],
        tempo: '07:20',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 8,
        status: 'Entregue',
        numero: 108,
        itens: [
          { qtd: 1, nome: 'Peixe Grelhado com Legumes' },
          { qtd: 1, nome: 'Suco Natural' },
          { qtd: 1, nome: 'Mousse de Maracujá' },
        ],
        tempo: '20:05',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 9,
        status: 'A Entregar',
        numero: 109,
        itens: [
          { qtd: 2, nome: 'Carpaccio de Carne' },
          { qtd: 1, nome: 'Água com Gás' },
        ],
        tempo: '04:45',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 10,
        status: 'Entregue',
        numero: 110,
        itens: [
          { qtd: 1, nome: 'Filé Mignon' },
          { qtd: 1, nome: 'Vinho Tinto Taça' },
        ],
        tempo: '11:30',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 11,
        status: 'A Entregar',
        numero: 111,
        itens: [
          { qtd: 1, nome: 'Tábua de Queijos' },
          { qtd: 1, nome: 'Cerveja Artesanal' },
        ],
        tempo: '06:55',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 12,
        status: 'Entregue',
        numero: 112,
        itens: [
          { qtd: 1, nome: 'Lasanha à Bolonhesa' },
          { qtd: 1, nome: 'Coca-Cola' },
          { qtd: 1, nome: 'Brownie com Sorvete' },
        ],
        tempo: '22:40',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 13,
        status: 'A Entregar',
        numero: 113,
        itens: [
          { qtd: 1, nome: 'Risoto de Cogumelos' },
          { qtd: 2, nome: 'Água com Gás' },
        ],
        tempo: '10:20',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
      {
        mesa: 14,
        status: 'Entregue',
        numero: 114,
        itens: [
          { qtd: 1, nome: 'Frango à Parmegiana' },
          { qtd: 1, nome: 'Suco Natural' },
        ],
        tempo: '13:55',
        acao: { texto: 'Editar', cor: 'primary' },
      },
      {
        mesa: 15,
        status: 'A Entregar',
        numero: 115,
        itens: [
          { qtd: 1, nome: 'Peixe Grelhado com Legumes' },
          { qtd: 1, nome: 'Chá Gelado' },
          { qtd: 1, nome: 'Pudim de Leite' },
        ],
        tempo: '16:05',
        acao: { texto: 'Finalizar', cor: 'success' },
      },
    ],
  }),
  actions: {
    criarComanda (itens) {
      const numero = this.comandas.length + 101
      const nova = {
        mesa: this.comandas.length + 1,
        numero,
        status: 'A Entregar',
        itens,
        tempo: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        acao: { texto: 'Finalizar', cor: 'success' },
      }
      console.log('Nova comanda criada:', nova)
      this.comandas.push(nova)
    },
    atualizarStatus (numero, novoStatus) {
      const comanda = this.comandas.find(c => c.numero === numero)
      if (comanda) {
        comanda.status = novoStatus
        comanda.acao
          = novoStatus === 'A Entregar'
            ? { texto: 'Finalizar', cor: 'success' }
            : { texto: 'Editar', cor: 'primary' }
      }
    },
    editarComanda (numero, novosItens) {
      const comanda = this.comandas.find(c => c.numero === numero)
      if (comanda) {
        comanda.itens = novosItens
        comanda.status = 'A Entregar'
        comanda.acao = { texto: 'Finalizar', cor: 'success' }
      }
    },
  },
})
