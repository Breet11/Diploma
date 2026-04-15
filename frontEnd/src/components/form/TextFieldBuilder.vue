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
  <v-text-field :id="id" v-model="model" :type="type" :label="label" :placeholder="placeholder" />
</template>

