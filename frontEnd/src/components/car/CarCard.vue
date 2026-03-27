<script setup>
defineProps({
  car: {
    type: Object,
    required: true
  }
});
</script>

<template>
  <article class="car-card">
    <div class="car-card__title-wrap">
      <h3 class="car-card__title">{{ car.brand }} {{ car.model }}</h3>
      <span class="car-card__badge" :class="car.available ? 'car-card__badge--ok' : 'car-card__badge--warn'">
        {{ car.available ? 'В наличии' : 'Нет в наличии' }}
      </span>
    </div>

    <ul class="car-card__meta">
      <li><strong>Год:</strong> {{ car.releaseYear }}</li>
      <li><strong>Макс. скорость:</strong> {{ car.topSpeed }} км/ч</li>
      <li><strong>Разгон:</strong> {{ car.acceleration }}</li>
      <li><strong>Двигатель:</strong> {{ car.engineType }}</li>
    </ul>

    <p class="car-card__price">{{ Number(car.price).toLocaleString('ru-RU') }} ₽</p>
  </article>
</template>

