import { api } from './api'

export async function createOrReturnDevice (deviceId) {
  return api.post('/aparelhos', { deviceId })
}

export async function verifyDevice (deviceId) {
  const { data } = await api.get(`/aparelhos/verify/${deviceId}`)
  return data
}

export async function validateDevice (deviceId, key) {
  return api.post(`/aparelhos/validate/${deviceId}`, { key })
}

export function generateDeviceId () {
  return 'dev-' + Math.random().toString(36).slice(2, 10)
}
