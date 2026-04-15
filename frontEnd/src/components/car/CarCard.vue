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
      <div class="car-card__title-wrap">
        <h3 class="car-card__title">{{ props.car.brand }} {{ props.car.model }}</h3>
        <v-chip :color="props.car.available ? 'success' : 'warning'" size="small" variant="tonal">
          {{ props.car.available ? t('common.status.available') : t('common.status.unavailable') }}
        </v-chip>
      </div>
    </v-card-title>

    <v-card-text>
      <v-list density="compact" bg-color="transparent" class="car-card__meta">
        <v-list-item>
          <template #title><strong>{{ t('carCard.year') }}:</strong> {{ props.car.releaseYear }}</template>
        </v-list-item>
        <v-list-item>
          <template #title><strong>{{ t('carCard.topSpeed') }}:</strong> {{ props.car.topSpeed }} {{ t('carCard.kmPerHour') }}</template>
        </v-list-item>
        <v-list-item>
          <template #title><strong>{{ t('carCard.acceleration') }}:</strong> {{ props.car.acceleration }}</template>
        </v-list-item>
        <v-list-item>
          <template #title><strong>{{ t('carCard.engine') }}:</strong> {{ props.car.engineType }}</template>
        </v-list-item>
      </v-list>

      <p class="car-card__price">{{ formattedPrice }} {{ t('carCard.pricePerHour') }}</p>
    </v-card-text>

    <v-card-actions>
      <v-btn block color="primary" :disabled="!props.car.available" @click="emit('rent', props.car)">
        {{ t('carCard.rent') }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>
