import { createApp } from 'vue'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { registerPlugins } from '@/plugins'
import App from './App.vue'
import 'vuetify/styles'
import 'unfonts.css'

const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#E53935', // vermelho forte para destaques
          secondary: '#1976D2', // azul para ações neutras
          background: '#F5F5F5', // fundo da página
          surface: '#FFFFFF', // fundo dos cards
          onSurface: '#1C1C1C', // texto nos cards
          onBackground: '#1C1C1C', // texto sobre o fundo
          success: '#43A047', // botões de confirmação
          error: '#D32F2F', // remoção ou alertas
          warning: '#FBC02D', // alertas leves
          info: '#0288D1', // mensagens informativas
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

const app = createApp(App)

registerPlugins(app)

app.use(vuetify)

app.mount('#app')
export default app
export { vuetify }
