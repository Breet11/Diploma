<script setup>
import { computed, reactive, ref } from 'vue';
import CarCard from '../components/car/CarCard.vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
import { carsMock } from '../data/cars.mock';

const cars = ref([...carsMock]);
const isCreateModalOpen = ref(false);

const form = reactive({
  brand: '',
  model: '',
  releaseYear: '',
  topSpeed: '',
  acceleration: '',
  engineType: '',
  price: '',
  available: ''
});

const availabilityOptions = [
  { label: 'В наличии', value: 'true' },
  { label: 'Нет в наличии', value: 'false' }
];

const canSubmit = computed(() => {
  return Object.values(form).every((value) => value !== '');
});

function openCreateModal() {
  isCreateModalOpen.value = true;
}

function closeCreateModal() {
  isCreateModalOpen.value = false;
}

function resetForm() {
  form.brand = '';
  form.model = '';
  form.releaseYear = '';
  form.topSpeed = '';
  form.acceleration = '';
  form.engineType = '';
  form.price = '';
  form.available = '';
}

function submitCreateForm() {
  if (!canSubmit.value) {
    return;
  }

  cars.value.unshift({
    id: crypto.randomUUID(),
    brand: form.brand,
    model: form.model,
    releaseYear: Number(form.releaseYear),
    topSpeed: Number(form.topSpeed),
    acceleration: form.acceleration,
    engineType: form.engineType,
    price: Number(form.price),
    available: form.available === 'true'
  });

  resetForm();
  closeCreateModal();
}
</script>

<template>
  <section class="page">
    <div class="page__toolbar">
      <h1 class="page__title">Список автомобилей</h1>
      <button class="btn" type="button" @click="openCreateModal">Добавить автомобиль</button>
    </div>

    <div class="cars-grid">
      <CarCard v-for="car in cars" :key="car.id" :car="car" />
    </div>

    <BaseModal :is-open="isCreateModalOpen" title="Создать автомобиль" @close="closeCreateModal">
      <form class="form-grid" @submit.prevent="submitCreateForm">
        <TextFieldBuilder id="brand" v-model="form.brand" label="Бренд" placeholder="Например, BMW" />
        <TextFieldBuilder id="model" v-model="form.model" label="Модель" placeholder="Например, M3" />
        <TextFieldBuilder id="releaseYear" v-model="form.releaseYear" type="number" label="Год выпуска" />
        <TextFieldBuilder id="topSpeed" v-model="form.topSpeed" type="number" label="Макс. скорость" />
        <TextFieldBuilder id="acceleration" v-model="form.acceleration" label="Разгон 0-100" placeholder="Например, 4.2s" />
        <TextFieldBuilder id="engineType" v-model="form.engineType" label="Тип двигателя" placeholder="Например, Diesel" />
        <TextFieldBuilder id="price" v-model="form.price" type="number" label="Цена" />
        <SelectFieldBuilder id="available" v-model="form.available" label="Наличие" :options="availabilityOptions" />

        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="closeCreateModal">Отмена</button>
          <button type="submit" class="btn" :disabled="!canSubmit">Сохранить</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>

