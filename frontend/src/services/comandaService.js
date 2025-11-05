import { api } from './api'

export const createComanda = async comandaData => {
  const { data } = await api.post('/cardapio', comandaData)
  return data
}

export const getComandas = async () => {
  const { data } = await api.get('/comandas')
  return data
}

export const updateComanda = async (id, comandaData) => {
  const { data } = await api.put(`/comandas/${id}`, comandaData)
  return data
}

export const deleteComanda = async id => {
  await api.delete(`/comandas/${id}`)
}

export async function atualizarStatusComanda (id, status) {
  const response = await api.patch(`/comandas/${id}/status`, { status })
  return response.data
}

export async function deleteComandasEntregues () {
  await api.delete('/comandas/entregues')
}
