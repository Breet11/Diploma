<script setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { LOCALE_TAGS } from '../../i18n';

const props = defineProps({
  car: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['rent']);
const { t, locale } = useI18n();
const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);
const formattedPrice = computed(() => Number(props.car.hourlyRentalPrice).toLocaleString(localeTag.value));

const imageSrc = computed(() => {
  if (props.car.imageBase64) {
    const contentType = props.car.imageContentType || 'image/jpeg';
    return `data:${contentType};base64,${props.car.imageBase64}`;
  }

  return 'https://via.placeholder.com/640x360?text=Car+Image';
});
</script>

<template>
  <v-card class="car-card">
    <v-img :src="imageSrc" :alt="`${props.car.brand} ${props.car.model}`" class="car-card__image" cover />

    <v-card-title>
      <h3 class="car-card__title">{{ props.car.brand }} {{ props.car.model }}</h3>
    </v-card-title>

    <v-card-text>

      <p class="car-card__price">{{ formattedPrice }} {{ t('carCard.pricePerHour') }}</p>
    </v-card-text>

    <v-card-actions>
      <v-btn block color="primary" :disabled="!props.car.available" @click="emit('rent', props.car)">
        {{ t('carCard.rent') }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>
