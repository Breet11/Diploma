<script setup>
import { computed, reactive, ref, watch } from 'vue';
import CarCard from '../components/car/CarCard.vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { isAuthenticated } from '../utils/auth';

const cars = ref([]);
const selectedCar = ref(null);
const loading = ref(false);
const errorMessage = ref('');

const isRentalModalOpen = ref(false);
const rentalSuccessMessage = ref('');
const rentalErrorMessage = ref('');

const rentalForm = reactive({
  hours: 1,
  firstName: '',
  lastName: '',
  phone: ''
});

const priceInfo = ref(null);

const authorized = computed(() => isAuthenticated());
const canSubmitRental = computed(() => {
  if (!selectedCar.value || !rentalForm.hours || Number(rentalForm.hours) <= 0) {
    return false;
  }

  if (authorized.value) {
    return true;
  }

  return Boolean(rentalForm.firstName && rentalForm.lastName && rentalForm.phone);
});

async function loadCars() {
  loading.value = true;
  errorMessage.value = '';
  try {
    cars.value = await api.getCars();
  } catch (error) {
    errorMessage.value = error.message;
  } finally {
    loading.value = false;
  }
}

function openRentalModal(car) {
  selectedCar.value = car;
  isRentalModalOpen.value = true;
  rentalSuccessMessage.value = '';
  rentalErrorMessage.value = '';
  rentalForm.hours = 1;
  rentalForm.firstName = '';
  rentalForm.lastName = '';
  rentalForm.phone = '';
}

function closeRentalModal() {
  isRentalModalOpen.value = false;
  selectedCar.value = null;
  priceInfo.value = null;
}

async function recalculatePrice() {
  if (!selectedCar.value || !rentalForm.hours || Number(rentalForm.hours) <= 0) {
    priceInfo.value = null;
    return;
  }

  try {
    priceInfo.value = await api.calculateRentalPrice({
      carUuid: selectedCar.value.uuid,
      hours: Number(rentalForm.hours)
    });
  } catch {
    priceInfo.value = null;
  }
}

async function submitRental() {
  if (!canSubmitRental.value) {
    return;
  }

  rentalErrorMessage.value = '';
  try {
    const payload = {
      carUuid: selectedCar.value.uuid,
      hours: Number(rentalForm.hours)
    };

    if (!authorized.value) {
      payload.firstName = rentalForm.firstName;
      payload.lastName = rentalForm.lastName;
      payload.phone = rentalForm.phone;
    }

    const response = await api.createRental(payload);
    rentalSuccessMessage.value = response.message;
  } catch (error) {
    rentalErrorMessage.value = error.message;
  }
}

watch(() => rentalForm.hours, recalculatePrice);
watch(selectedCar, recalculatePrice);

loadCars();
</script>

<template>
  <section class="page">
    <div class="page__toolbar">
      <h1 class="page__title">Аренда автомобилей</h1>
    </div>

    <p v-if="loading">Загрузка...</p>
    <p v-else-if="errorMessage" class="text-error">{{ errorMessage }}</p>

    <div v-else class="cars-grid">
      <CarCard v-for="car in cars" :key="car.uuid" :car="car" @rent="openRentalModal" />
    </div>

    <BaseModal :is-open="isRentalModalOpen" title="Оформление аренды" @close="closeRentalModal">
      <form class="form-grid" @submit.prevent="submitRental">
        <p v-if="selectedCar"><strong>{{ selectedCar.brand }} {{ selectedCar.model }}</strong></p>

        <TextFieldBuilder id="hours" v-model="rentalForm.hours" type="number" label="Длительность аренды (часы)" />

        <template v-if="!authorized">
          <TextFieldBuilder id="firstName" v-model="rentalForm.firstName" label="Имя" />
          <TextFieldBuilder id="lastName" v-model="rentalForm.lastName" label="Фамилия" />
          <TextFieldBuilder id="phone" v-model="rentalForm.phone" label="Номер телефона" placeholder="+7..." />
        </template>

        <p v-if="priceInfo" class="price-preview">
          Стоимость: <strong>{{ Number(priceInfo.totalPrice).toLocaleString('ru-RU') }} ₽</strong>
          (коэффициент лояльности: {{ priceInfo.multiplier }})
        </p>

        <p v-if="rentalSuccessMessage" class="text-success">{{ rentalSuccessMessage }}</p>
        <p v-if="rentalErrorMessage" class="text-error">{{ rentalErrorMessage }}</p>

        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="closeRentalModal">Закрыть</button>
          <button type="submit" class="btn" :disabled="!canSubmitRental">Отправить заявку</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>
