<script setup>
import { useToast } from '../../composables/useToast';

const { toasts, removeToast } = useToast();
</script>

<template>
  <Teleport to="body">
	<div class="toast-host">
	  <TransitionGroup name="toast-list">
		<v-alert
		  v-for="toast in toasts"
		  :key="toast.id"
		  :type="toast.type"
		  variant="tonal"
		  closable
		  border="start"
		  class="toast-host__item"
		  @click:close="removeToast(toast.id)"
		>
		  {{ toast.message }}
		</v-alert>
	  </TransitionGroup>
	</div>
  </Teleport>
</template>

<style scoped>
.toast-host {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 3000;
  display: grid;
  gap: 12px;
  width: min(420px, calc(100vw - 32px));
}

.toast-host__item {
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.18);
}

.toast-list-enter-active,
.toast-list-leave-active {
  transition: all 0.2s ease;
}

.toast-list-enter-from,
.toast-list-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>

