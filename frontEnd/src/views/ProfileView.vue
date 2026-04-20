<script setup>
import { onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { api } from '../api';
import { useToast } from '../composables/useToast';

const profile = ref(null);
const { t } = useI18n();
const { error: showError } = useToast();

onMounted(async () => {
  try {
    profile.value = await api.getProfile();
  } catch (error) {
    showError(error.message);
  }
});
</script>

<template>
  <section class="page page--narrow">
    <h1 class="page__title">{{ t('profile.title') }}</h1>
    <div v-if="profile" class="profile-card">
      <p><strong>{{ t('profile.login') }}:</strong> {{ profile.login }}</p>
      <p><strong>{{ t('profile.email') }}:</strong> {{ profile.email }}</p>
      <p><strong>{{ t('common.labels.firstName') }}:</strong> {{ profile.firstName }}</p>
      <p><strong>{{ t('common.labels.lastName') }}:</strong> {{ profile.lastName }}</p>
      <p><strong>{{ t('common.labels.phone') }}:</strong> {{ profile.phone }}</p>
      <p><strong>{{ t('profile.role') }}:</strong> {{ t(`common.roles.${profile.role}`) }}</p>
    </div>
  </section>
</template>

