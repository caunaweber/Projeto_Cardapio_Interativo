import { api } from './api'

export async function getPratos () {
  const res = await api.get('/pratos')
  return res.data
}

export async function criarPrato (dados, imagemFile) {
  const formData = new FormData()

  const blob = new Blob([JSON.stringify(dados)], { type: 'application/json' })
  formData.append('dados', blob)

  if (imagemFile) {
    formData.append('imagem', imagemFile)
  }

  const res = await api.post('/pratos', formData, {
    headers: {
      'Content-Type': undefined,
    },
  })
  return res.data
}

export async function atualizarPrato (id, dados, imagemFile) {
  const formData = new FormData()

  const blob = new Blob([JSON.stringify(dados)], { type: 'application/json' })
  formData.append('dados', blob)
  if (imagemFile) {
    formData.append('imagem', imagemFile)
  }
  const res = await api.put(`/pratos/${id}`, formData, {
    headers: {
      'Content-Type': undefined,
    },
  })
  return res.data
}

export async function removerPrato (id) {
  await api.delete(`/pratos/${id}`)
}

export async function getMetricas () {
  const res = await api.get('/pratos/metricas')
  return res.data
}
