import { api } from './api'

export const createComanda = async comandaData => {
  const { data } = await api.post('/cardapio', comandaData)
  return data
}
