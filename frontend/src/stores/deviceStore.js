import { defineStore } from 'pinia'
import { createOrReturnDevice, generateDeviceId, validateDevice, verifyDevice } from '@/services/device'

export const useDeviceStore = defineStore('device', {
  state: () => ({
    deviceId: localStorage.getItem('deviceId') || null,
    validated: false,
    mesaNum: null,
    loading: false,
    error: null,
  }),

  actions: {
    async initDevice () {
      this.loading = true
      try {
        if (!this.deviceId) {
          this.deviceId = generateDeviceId()
          localStorage.setItem('deviceId', this.deviceId)
        }

        await createOrReturnDevice(this.deviceId)

        const data = await verifyDevice(this.deviceId)
        this.validated = true
        this.mesaNum = data.mesaNum
      } catch (error) {
        this.validated = false
        console.error('Aparelho não validado:', error)
      } finally {
        this.loading = false
      }
    },

    async validateKey (key) {
      this.loading = true
      try {
        await validateDevice(this.deviceId, key)
        this.validated = true
        this.error = null
      } catch (error) {
        this.error = error.response?.data?.message || 'Falha na validação'
        console.error('Erro ao validar aparelho:', error)
      } finally {
        this.loading = false
      }
    },
  },
})
