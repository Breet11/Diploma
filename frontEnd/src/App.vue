<script setup>
import AppHeader from './components/layout/AppHeader.vue';
</script>

<template>
  <v-app class="app-shell">
    <AppHeader />
    <v-main>
      <v-container class="app-content">
        <RouterView />
      </v-container>
    </v-main>
  </v-app>
</template>

