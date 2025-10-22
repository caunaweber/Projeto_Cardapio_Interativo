import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
// src/plugins/vuetify.js
import 'vuetify/styles'

const defaultTheme = localStorage.getItem('theme') || 'light'

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme,
    themes: {
      light: {
        colors: {
          primary: '#E53935',
          secondary: '#1976D2',
          background: '#F5F5F5',
          surface: '#FFFFFF',
          onSurface: '#1C1C1C',
          onBackground: '#1C1C1C',
          success: '#43A047',
          error: '#D32F2F',
          warning: '#FBC02D',
          info: '#0288D1',
        },
      },
      dark: {
        colors: {
          primary: '#E53935',
          secondary: '#1976D2',
          background: '#121212',
          surface: '#1E1E1E',
          onSurface: '#E0E0E0',
          onBackground: '#E0E0E0',
          success: '#43A047',
          error: '#D32F2F',
          warning: '#FBC02D',
          info: '#0288D1',
        },
      },
    },
  },
})
