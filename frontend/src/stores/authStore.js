import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
  }),
  getters: {
    isAuthenticated: state => !!state.user,
    isAdmin: state => state.user?.role === 'admin',
  },
  actions: {
    login (email, senha) {
      if (email === 'admin' && senha === '123') {
        this.user = { email, role: 'admin' }
        return true
      }
      return false
    },
    logout () {
      this.user = null
    },
  },
})
