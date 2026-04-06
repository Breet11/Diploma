<script setup>
import { computed } from 'vue';

const props = defineProps({
  car: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['rent']);

const imageSrc = computed(() => {
  if (props.car.imageBase64) {
    const contentType = props.car.imageContentType || 'image/jpeg';
    return `data:${contentType};base64,${props.car.imageBase64}`;
  }

  return 'https://via.placeholder.com/640x360?text=Car+Image';
});
</script>

<template>
  <article class="car-card">
    <img :src="imageSrc" :alt="`${props.car.brand} ${props.car.model}`" class="car-card__image" />

    <div class="car-card__title-wrap">
      <h3 class="car-card__title">{{ props.car.brand }} {{ props.car.model }}</h3>
      <span class="car-card__badge" :class="props.car.available ? 'car-card__badge--ok' : 'car-card__badge--warn'">
        {{ props.car.available ? 'Доступен' : 'Недоступен' }}
      </span>
    </div>

    <ul class="car-card__meta">
      <li><strong>Год:</strong> {{ props.car.releaseYear }}</li>
      <li><strong>Макс. скорость:</strong> {{ props.car.topSpeed }} км/ч</li>
      <li><strong>Разгон:</strong> {{ props.car.acceleration }}</li>
      <li><strong>Двигатель:</strong> {{ props.car.engineType }}</li>
    </ul>

    <p class="car-card__price">{{ Number(props.car.hourlyRentalPrice).toLocaleString('ru-RU') }} ₽ / час</p>
    <button type="button" class="btn" :disabled="!props.car.available" @click="emit('rent', props.car)">Арендовать</button>
  </article>
</template>
