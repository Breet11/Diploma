<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { saveAuth } from '../utils/auth';

const router = useRouter();
const errorMessage = ref('');

const form = reactive({
  login: '',
  password: ''
});

async function submit() {
  errorMessage.value = '';
  try {
    const auth = await api.login(form);
    saveAuth(auth);
    router.push({ name: 'profile' });
  } catch (error) {
    errorMessage.value = error.message;
  }
}
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title"><i class="mdi mdi-login"></i> Вход</h1>
    <form class="form-grid" @submit.prevent="submit">
      <TextFieldBuilder id="login" v-model="form.login" label="Логин" />
      <TextFieldBuilder id="password" v-model="form.password" label="Пароль" type="password" />
      <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
      <div class="form-actions">
        <button type="button" class="btn btn--ghost" @click="router.push({ name: 'register' })">
          <i class="mdi mdi-account-plus-outline"></i>
          Регистрация
        </button>
        <button type="submit" class="btn">
          <i class="mdi mdi-login"></i>
          Войти
        </button>
      </div>
    </form>
  </section>
</template>
