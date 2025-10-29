import { defineStore } from 'pinia'
import { getCardapio } from '@/services/cardapioService'

const categoriaMap = {
  PRATO_PRINCIPAL: 'Pratos Principais',
  ENTRADA: 'Entradas',
  SOBREMESA: 'Sobremesas',
  BEBIDA: 'Bebidas',
}

function getImageUrl (filename) {
  if (!filename) {
    return '/no-image.png'
  }
  if (filename.startsWith('data:') || filename.startsWith('http')) {
    return filename
  }
  return `http://localhost:8080/uploads/${filename}`
}

export const useCardapioStore = defineStore('cardapio', {
  state: () => ({
    categorias: [],
    carregando: false,
    error: null,
  }),
  getters: {
    categoriasNomes: state => ['Todas', ...state.categorias.map(c => c.nome)],
  },
  actions: {
    async carregarCardapio () {
      this.carregando = true
      this.error = null

      try {
        const data = await getCardapio()
        const categoriasMap = {}

        for (const prato of data) {
          const nomeCategoria = categoriaMap[prato.categoria] || prato.categoria

          if (!categoriasMap[nomeCategoria]) {
            categoriasMap[nomeCategoria] = { nome: nomeCategoria, itens: [] }
          }

          categoriasMap[nomeCategoria].itens.push({
            id: prato.id,
            nome: prato.nome,
            preco: prato.preco,
            imagem: getImageUrl(prato.imagem),
            categoria: nomeCategoria,
          })
        }

        this.categorias = Object.values(categoriasMap)
      } catch (error) {
        console.error('Erro ao carregar cardápio', error)
        this.error = 'Falha ao carregar o cardápio. Verifique sua conexão ou tente novamente mais tarde.'
      } finally {
        this.carregando = false
      }
    },
  },
})
