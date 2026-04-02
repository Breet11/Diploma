<script setup>
defineProps({
  car: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['rent']);
</script>

<template>
  <article class="car-card">
    <img :src="car.imageUrl" :alt="`${car.brand} ${car.model}`" class="car-card__image" />

    <div class="car-card__title-wrap">
      <h3 class="car-card__title">{{ car.brand }} {{ car.model }}</h3>
      <span class="car-card__badge" :class="car.available ? 'car-card__badge--ok' : 'car-card__badge--warn'">
        {{ car.available ? 'Доступен' : 'Недоступен' }}
      </span>
    </div>

    <ul class="car-card__meta">
      <li><strong>Год:</strong> {{ car.releaseYear }}</li>
      <li><strong>Макс. скорость:</strong> {{ car.topSpeed }} км/ч</li>
      <li><strong>Разгон:</strong> {{ car.acceleration }}</li>
      <li><strong>Двигатель:</strong> {{ car.engineType }}</li>
    </ul>

    <p class="car-card__price">{{ Number(car.hourlyRentalPrice).toLocaleString('ru-RU') }} ₽ / час</p>
    <button type="button" class="btn" :disabled="!car.available" @click="emit('rent', car)">Арендовать</button>
  </article>
</template>
