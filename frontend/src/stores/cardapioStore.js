import { defineStore } from 'pinia'

export const useCardapioStore = defineStore('cardapio', {
  state: () => ({
    categorias: [
      { nome: 'Entradas', itens: [
        { id: 1, nome: 'Salada Caesar', preco: 12.5, imagem: 'entrada.png' },
        { id: 2, nome: 'Bruschetta', preco: 9, imagem: 'entrada.png' },
        { id: 8, nome: 'Carpaccio de Carne', preco: 18, imagem: 'entrada.png' },
        { id: 9, nome: 'Tábua de Queijos', preco: 22, imagem: 'entrada.png' },
      ] },
      { nome: 'Pratos Principais', itens: [
        { id: 3, nome: 'Filé Mignon', preco: 35, imagem: 'prato.png' },
        { id: 4, nome: 'Strogonoff de Frango', preco: 28, imagem: 'prato.png' },
        { id: 10, nome: 'Risoto de Cogumelos', preco: 30, imagem: 'prato.png' },
        { id: 11, nome: 'Lasanha à Bolonhesa', preco: 26, imagem: 'prato.png' },
        { id: 12, nome: 'Peixe Grelhado com Legumes', preco: 32, imagem: 'prato.png' },
        { id: 13, nome: 'Frango à Parmegiana', preco: 27, imagem: 'prato.png' },
      ] },
      { nome: 'Bebidas', itens: [
        { id: 5, nome: 'Suco Natural', preco: 7.5, imagem: 'suco.png' },
        { id: 6, nome: 'Coca-Cola', preco: 5, imagem: 'suco.png' },
        { id: 14, nome: 'Água com Gás', preco: 4, imagem: 'suco.png' },
        { id: 15, nome: 'Vinho Tinto Taça', preco: 18, imagem: 'suco.png' },
        { id: 16, nome: 'Cerveja Artesanal', preco: 12, imagem: 'suco.png' },
        { id: 17, nome: 'Chá Gelado', preco: 6.5, imagem: 'suco.png' },
      ] },
      { nome: 'Sobremesas', itens: [
        { id: 18, nome: 'Petit Gâteau', preco: 15, imagem: 'sobremesa.png' },
        { id: 19, nome: 'Cheesecake de Frutas Vermelhas', preco: 14, imagem: 'sobremesa.png' },
        { id: 20, nome: 'Pudim de Leite', preco: 10, imagem: 'sobremesa.png' },
        { id: 21, nome: 'Torta de Limão', preco: 12, imagem: 'sobremesa.png' },
        { id: 22, nome: 'Brownie com Sorvete', preco: 16, imagem: 'sobremesa.png' },
        { id: 23, nome: 'Mousse de Maracujá', preco: 11, imagem: 'sobremesa.png' },
      ] },
    ],
  }),
  getters: {
    categoriasNomes: state => ['Todas', ...state.categorias.map(c => c.nome)],
  },
})
