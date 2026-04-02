<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';

const router = useRouter();
const errorMessage = ref('');
const successMessage = ref('');

const form = reactive({
  email: '',
  login: '',
  password: ''
});

async function submit() {
  errorMessage.value = '';
  successMessage.value = '';

  try {
    const response = await api.register(form);
    successMessage.value = response?.message || response || 'Регистрация прошла успешно';
    router.push({ name: 'login' });
  } catch (error) {
    errorMessage.value = error.message;
  }
}
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title"><i class="mdi mdi-account-plus-outline"></i> Регистрация</h1>
    <form class="form-grid" @submit.prevent="submit">
      <TextFieldBuilder id="email" v-model="form.email" label="Email" type="email" />
      <TextFieldBuilder id="login" v-model="form.login" label="Логин" />
      <TextFieldBuilder id="password" v-model="form.password" label="Пароль" type="password" />
      <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="text-success">{{ successMessage }}</p>
      <div class="form-actions">
        <button type="button" class="btn btn--ghost" @click="router.push({ name: 'login' })">Назад ко входу</button>
        <button type="submit" class="btn">Зарегистрироваться</button>
      </div>
    </form>
  </section>
</template>
