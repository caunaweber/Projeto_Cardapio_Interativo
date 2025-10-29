import { api } from './api'

export async function getCardapio () {
  const { data } = await api.get('/cardapio')
  return data
}
