import { api } from './api'

export async function getAllAparelhos () {
  const res = await api.get('/aparelhos')
  return res.data
}

export async function updateAparelho (id, dados) {
  const res = await api.put(`/aparelhos/${id}`, dados)
  return res.data
}

export async function deleteAparelho (id) {
  await api.delete(`/aparelhos/${id}`)
}

export async function invalidateAparelho (id) {
  const res = await api.post(`/aparelhos/invalidate/${id}`)
  return res.data
}
