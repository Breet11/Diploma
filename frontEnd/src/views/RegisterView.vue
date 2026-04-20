<script setup>
import { reactive } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { useToast } from '../composables/useToast';
import { encryptAuthPassword } from '../utils/crypto';

const router = useRouter();
const { t } = useI18n();
const { success: showSuccess, error: showError } = useToast();

const form = reactive({
  email: '',
  login: '',
  firstName: '',
  lastName: '',
  phone: '',
  password: '',
  confirmPassword: ''
});

async function submit() {
  if (form.password !== form.confirmPassword) {
    showError(t('errors.passwordsDoNotMatch'));
    return;
  }

  try {
    const response = await api.register({
      ...form,
      password: await encryptAuthPassword(form.password),
      confirmPassword: await encryptAuthPassword(form.confirmPassword)
    });
    showSuccess(response?.message || response || t('auth.register.successFallback'));
    router.push({ name: 'login' });
  } catch (error) {
    showError(error.message);
  }
}
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title"><i class="mdi mdi-account-plus-outline"></i> {{ t('auth.register.title') }}</h1>
    <form class="form-grid" @submit.prevent="submit">
      <TextFieldBuilder id="email" v-model="form.email" :label="t('common.labels.email')" type="email" />
      <TextFieldBuilder id="login" v-model="form.login" :label="t('common.labels.login')" />
      <TextFieldBuilder id="firstName" v-model="form.firstName" :label="t('common.labels.firstName')" />
      <TextFieldBuilder id="lastName" v-model="form.lastName" :label="t('common.labels.lastName')" />
      <TextFieldBuilder id="phone" v-model="form.phone" :label="t('common.labels.phone')" placeholder="+1..." />
      <TextFieldBuilder id="password" v-model="form.password" :label="t('common.labels.password')" type="password" />
      <TextFieldBuilder id="confirmPassword" v-model="form.confirmPassword" :label="t('common.labels.confirmPassword')" type="password" />
      <div class="form-actions">
        <button type="button" class="btn btn--ghost" @click="router.push({ name: 'login' })">{{ t('auth.register.backToLogin') }}</button>
        <button type="submit" class="btn">{{ t('auth.register.submit') }}</button>
      </div>
    </form>
  </section>
</template>
