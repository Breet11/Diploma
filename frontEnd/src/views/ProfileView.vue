<script setup>
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { api } from '../api';

const profile = ref(null);
const errorMessage = ref('');
const { t } = useI18n();

onMounted(async () => {
  try {
    profile.value = await api.getProfile();
  } catch (error) {
    errorMessage.value = error.message;
  }
});
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title">{{ t('profile.title') }}</h1>
    <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
    <div v-else-if="profile" class="profile-card">
      <p><strong>{{ t('profile.login') }}:</strong> {{ profile.login }}</p>
      <p><strong>{{ t('profile.email') }}:</strong> {{ profile.email }}</p>
      <p><strong>{{ t('profile.role') }}:</strong> {{ t(`common.roles.${profile.role}`) }}</p>
    </div>
  </section>
</template>

