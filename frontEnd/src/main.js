import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import '@mdi/font/css/materialdesignicons.min.css';
import './assets/main.css';

createApp(App).use(router).mount('#app');
