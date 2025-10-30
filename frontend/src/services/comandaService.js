import { api } from './api'

export const createComanda = async comandaData => {
  try {
    const { data } = await api.post('/cardapio', comandaData)
    return data
  } catch (error) {
    console.error('Erro ao criar comanda:', error.response?.data || error.message)
    throw new Error(error.response?.data?.message || 'Falha ao enviar pedido')
  }
}
