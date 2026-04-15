<script setup>
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
</script>

<template>
  <section class="page">
    <h1 class="page__title">{{ t('home.title') }}</h1>
    <p class="page__description">
      {{ t('home.description') }}
    </p>
  </section>
</template>
