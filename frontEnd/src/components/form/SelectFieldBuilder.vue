<script setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const model = defineModel({
  type: [String, Number],
  default: ''
});

const props = defineProps({
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
    default: ''
  }
});

const { t } = useI18n();
const resolvedPlaceholder = computed(() => props.placeholder || t('common.selectPlaceholder'));
</script>

<template>
  <v-select
    :id="props.id"
    v-model="model"
    :label="props.label"
    :placeholder="resolvedPlaceholder"
    :items="props.options"
    item-title="label"
    item-value="value"
  />
</template>

