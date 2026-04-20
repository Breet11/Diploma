<script setup>
import { computed, reactive, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import CarCard from '../components/car/CarCard.vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import { api } from '../api';
import { useToast } from '../composables/useToast';
import { LOCALE_TAGS } from '../i18n';
import { isAuthenticated } from '../utils/auth';
import { formatUsd } from '../utils/currency';

const { t, locale } = useI18n();
const { success: showSuccess, error: showError } = useToast();
const cars = ref([]);
const selectedCar = ref(null);
const loading = ref(false);

const isRentalModalOpen = ref(false);

const rentalForm = reactive({
  hours: 1,
  firstName: '',
  lastName: '',
  phone: ''
});

const priceInfo = ref(null);

const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);
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
  try {
    cars.value = await api.getCars();
  } catch (error) {
    showError(error.message);
  } finally {
    loading.value = false;
  }
}

function openRentalModal(car) {
  selectedCar.value = car;
  isRentalModalOpen.value = true;
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
    showSuccess(response.message);
    closeRentalModal();
  } catch (error) {
    showError(error.message);
  }
}

watch(() => rentalForm.hours, recalculatePrice);
watch(selectedCar, recalculatePrice);

loadCars();
</script>

<template>
  <section class="page">
    <div class="page__toolbar">
      <h1 class="page__title">{{ t('cars.title') }}</h1>
    </div>

    <p v-if="loading">{{ t('common.loading') }}</p>
    <div v-else class="cars-grid">
      <CarCard v-for="car in cars" :key="car.uuid" :car="car" @rent="openRentalModal" />
    </div>

    <BaseModal :is-open="isRentalModalOpen" :title="t('cars.rentModalTitle')" @close="closeRentalModal">
      <form class="form-grid" @submit.prevent="submitRental">
        <p v-if="selectedCar"><strong>{{ selectedCar.brand }} {{ selectedCar.model }}</strong></p>

        <TextFieldBuilder id="hours" v-model="rentalForm.hours" type="number" :label="t('common.labels.hours')" />

        <template v-if="!authorized">
          <TextFieldBuilder id="firstName" v-model="rentalForm.firstName" :label="t('common.labels.firstName')" />
          <TextFieldBuilder id="lastName" v-model="rentalForm.lastName" :label="t('common.labels.lastName')" />
          <TextFieldBuilder id="phone" v-model="rentalForm.phone" :label="t('common.labels.phone')" placeholder="+7..." />
        </template>

        <p v-if="priceInfo" class="price-preview">
          {{ t('cars.price') }}: <strong>{{ formatUsd(priceInfo.totalPrice, localeTag) }}</strong>
          ({{ t('cars.loyaltyMultiplier') }}: {{ priceInfo.multiplier }})
        </p>


        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="closeRentalModal">{{ t('common.actions.close') }}</button>
          <button type="submit" class="btn" :disabled="!canSubmitRental">{{ t('common.actions.submitRequest') }}</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>
