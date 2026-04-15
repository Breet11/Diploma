import { createApp } from 'vue';
import App from './App.vue';
import i18n from './i18n';
import router from './router';
import vuetify from './plugins/vuetify';
import '@mdi/font/css/materialdesignicons.min.css';
import './assets/main.css';

createApp(App).use(router).use(vuetify).use(i18n).mount('#app');
