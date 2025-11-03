import { api } from './api'

export const createComanda = async comandaData => {
  const { data } = await api.post('/cardapio', comandaData)
  return data
}

export const getComandas = async () => {
  const { data } = await api.get('/comandas')
  return data
}
