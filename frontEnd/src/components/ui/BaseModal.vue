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

function onBackdropClick(event) {
  if (event.target === event.currentTarget) {
    emit('close');
  }
}
</script>

<template>
  <Teleport to="body">
    <div v-if="isOpen" class="modal" @click="onBackdropClick">
      <div class="modal__dialog">
        <header class="modal__header">
          <h2 class="modal__title">{{ title }}</h2>
          <button class="modal__close" type="button" @click="$emit('close')">x</button>
        </header>
        <div class="modal__content">
          <slot />
        </div>
      </div>
    </div>
  </Teleport>
</template>

