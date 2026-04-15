<script setup>
import { reactive, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { encryptAuthPassword } from '../utils/crypto';

const router = useRouter();
const { t } = useI18n();
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
    const response = await api.register({
      ...form,
      password: await encryptAuthPassword(form.password)
    });
    successMessage.value = response?.message || response || t('auth.register.successFallback');
    router.push({ name: 'login' });
  } catch (error) {
    errorMessage.value = error.message;
  }
}
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title"><i class="mdi mdi-account-plus-outline"></i> {{ t('auth.register.title') }}</h1>
    <form class="form-grid" @submit.prevent="submit">
      <TextFieldBuilder id="email" v-model="form.email" :label="t('common.labels.email')" type="email" />
      <TextFieldBuilder id="login" v-model="form.login" :label="t('common.labels.login')" />
      <TextFieldBuilder id="password" v-model="form.password" :label="t('common.labels.password')" type="password" />
      <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
      <p v-if="successMessage" class="text-success">{{ successMessage }}</p>
      <div class="form-actions">
        <button type="button" class="btn btn--ghost" @click="router.push({ name: 'login' })">{{ t('auth.register.backToLogin') }}</button>
        <button type="submit" class="btn">{{ t('auth.register.submit') }}</button>
      </div>
    </form>
  </section>
</template>
