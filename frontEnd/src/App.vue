<script setup>
import AppHeader from './components/layout/AppHeader.vue';
import GlobalToasts from './components/ui/GlobalToasts.vue';
</script>

<template>
  <v-app class="app-shell">
    <AppHeader />
    <v-main>
      <v-container class="app-content">
        <RouterView />
      </v-container>
    </v-main>
    <GlobalToasts />
  </v-app>
</template>

