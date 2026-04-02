<template>
  <header class="header">
    <div class="header__inner">
      <router-link class="header__logo" to="/">Diploma Rent Cars</router-link>
      <nav class="header__nav">
        <router-link to="/cars" class="header__link" active-class="header__link--active">
          Список автомобилей
        </router-link>
        <router-link v-if="admin" to="/admin" class="header__link" active-class="header__link--active">
          Админ панель
        </router-link>
        <router-link
          :to="accountRoute"
          class="header__icon-link"
          :title="authorized ? 'Личный кабинет' : 'Войти в аккаунт'"
          aria-label="Профиль"
        >
          <i class="mdi mdi-account-circle-outline"></i>
        </router-link>
        <button v-if="authorized" type="button" class="header__link header__link--button" @click="logout">
          Выход
        </button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { clearAuth, isAdmin, isAuthenticated } from '../../utils/auth';

const router = useRouter();

const authorized = computed(() => isAuthenticated());
const admin = computed(() => isAdmin());
const accountRoute = computed(() => (authorized.value ? { name: 'profile' } : { name: 'login' }));

function logout() {
  clearAuth();
  router.push({ name: 'cars' });
}
</script>
