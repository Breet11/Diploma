<script setup>
defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['close']);
</script>

<template>
  <v-dialog :model-value="isOpen" @update:model-value="emit('close')">
    <v-card>
      <v-card-title class="d-flex align-center justify-space-between">
        <span>{{ title }}</span>
        <v-btn icon variant="text" @click="emit('close')">
          <v-icon icon="mdi-close" />
        </v-btn>
      </v-card-title>
      <v-card-text>
        <slot />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

