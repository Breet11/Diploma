<script setup>
import { onMounted, ref } from 'vue';
import { api } from '../api';

const profile = ref(null);
const errorMessage = ref('');

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
    <h1 class="page__title">Личный кабинет</h1>
    <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>
    <div v-else-if="profile" class="profile-card">
      <p><strong>Логин:</strong> {{ profile.login }}</p>
      <p><strong>Email:</strong> {{ profile.email }}</p>
      <p><strong>Роль:</strong> {{ profile.role }}</p>
    </div>
  </section>
</template>

