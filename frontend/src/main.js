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
          primary: '#E53935',
          secondary: '#43A047',
          background: '#FAFAFA',
          surface: '#FFFFFF',
          onSurface: '#212121',
          onBackground: '#212121',
        },
      },
      dark: {
        colors: {
          primary: '#E53935',
          secondary: '#43A047',
          background: '#121212',
          surface: '#1E1E1E',
          onSurface: '#E0E0E0',
          onBackground: '#E0E0E0',
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
