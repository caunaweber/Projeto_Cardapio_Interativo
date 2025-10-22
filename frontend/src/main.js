import { createApp } from 'vue'
import { registerPlugins } from '@/plugins'
import App from './App.vue'
import 'vuetify/styles'
import 'unfonts.css'
import '@mdi/font/css/materialdesignicons.css'

const app = createApp(App)
registerPlugins(app)
app.mount('#app')
