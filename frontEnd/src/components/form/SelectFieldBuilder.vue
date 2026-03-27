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
  options: {
    type: Array,
    default: () => []
  },
  placeholder: {
    type: String,
    default: 'Выберите значение'
  }
});
</script>

<template>
  <div class="field">
    <label :for="id" class="field__label">{{ label }}</label>
    <select :id="id" v-model="model" class="field__control">
      <option disabled value="">{{ placeholder }}</option>
      <option v-for="option in options" :key="option.value" :value="option.value">
        {{ option.label }}
      </option>
    </select>
  </div>
</template>

