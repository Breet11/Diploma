<script setup>
import { reactive, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { saveAuth } from '../utils/auth';
import { encryptAuthPassword } from '../utils/crypto';

const router = useRouter();
const { t } = useI18n();
const errorMessage = ref('');

const form = reactive({
  login: '',
  password: ''
});

async function submit() {
  errorMessage.value = '';
  try {
    const auth = await api.login({
      ...form,
      password: await encryptAuthPassword(form.password)
    });
    saveAuth(auth);
    router.push({ name: 'profile' });
  } catch (error) {
    errorMessage.value = error.message;
  }
}
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title"><i class="mdi mdi-login"></i> {{ t('auth.login.title') }}</h1>
    <form class="form-grid" @submit.prevent="submit">
      <TextFieldBuilder id="login" v-model="form.login" :label="t('common.labels.login')" />
      <TextFieldBuilder id="password" v-model="form.password" :label="t('common.labels.password')" type="password" />
      <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
      <div class="form-actions">
        <button type="button" class="btn btn--ghost" @click="router.push({ name: 'register' })">
          <i class="mdi mdi-account-plus-outline"></i>
          {{ t('auth.login.registerLink') }}
        </button>
        <button type="submit" class="btn">
          <i class="mdi mdi-login"></i>
          {{ t('auth.login.submit') }}
        </button>
      </div>
    </form>
  </section>
</template>
