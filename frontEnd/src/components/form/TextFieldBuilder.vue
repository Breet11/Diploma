<script setup>
const model = defineModel({
  type: [String, Number],
  default: ''
});

defineProps({
  id: {
    type: String,
    required: true
  },
  label: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'text'
  }
});
</script>

<template>
  <div class="field">
    <label :for="id" class="field__label">{{ label }}</label>
    <input
      :id="id"
      v-model="model"
      :type="type"
      :placeholder="placeholder"
      class="field__control"
    />
  </div>
</template>

