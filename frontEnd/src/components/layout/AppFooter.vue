<script setup>
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const currentYear = new Date().getFullYear();
</script>

<template>
  <footer class="app-footer">
    <div class="app-footer__inner">
      <p class="app-footer__brand">{{ t('app.name') }}</p>
      <p class="app-footer__text">{{ t('footer.contact') }}: vladimir.ryjenco@gmail.com | +37368025725</p>
      <p class="app-footer__text">{{ t('footer.address') }}: Anenii Noi, Republic of Moldova</p>
      <p class="app-footer__copyright">&copy; {{ currentYear }} {{ t('footer.rights') }}</p>
    </div>
  </footer>
</template>

