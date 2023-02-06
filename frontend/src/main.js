import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import Toaster from '@meforma/vue-toaster'
import PrimeVue from 'primevue/config'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'


//import '@/assets/css/main.css'

createApp(App).use(store).use(router).use(Toaster).use(PrimeVue).mount('#app')
