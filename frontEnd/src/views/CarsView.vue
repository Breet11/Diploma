<script setup>
import { computed, onMounted, onUnmounted, reactive, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import CarCard from '../components/car/CarCard.vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
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
const maxHourlyPrice = ref(0);
const releaseYearRange = ref([0, 0]);

const isRentalModalOpen = ref(false);

const rentalForm = reactive({
  hours: 1,
  firstName: '',
  lastName: '',
  phone: ''
});

const priceInfo = ref(null);
const filters = reactive({
  brand: '',
  fuelType: ''
});
const viewportWidth = ref(typeof window !== 'undefined' ? window.innerWidth : 1024);
const isFiltersOpen = ref(true);

const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);
const isMobileFiltersMode = computed(() => viewportWidth.value <= 768);
const authorized = computed(() => isAuthenticated());
const selectedCarImageSrc = computed(() => {
  if (!selectedCar.value || !selectedCar.value.imageBase64) {
    return 'https://via.placeholder.com/640x360?text=Car+Image';
  }

  const contentType = selectedCar.value.imageContentType || 'image/jpeg';
  return `data:${contentType};base64,${selectedCar.value.imageBase64}`;
});

const selectedCarTechnicalSpecs = computed(() => {
  if (!selectedCar.value) {
    return [];
  }

  return [
    { key: 'releaseYear', label: t('cars.specs.releaseYear'), value: selectedCar.value.releaseYear },
    { key: 'engineType', label: t('cars.specs.fuelType'), value: selectedCar.value.engineType },
    { key: 'engineVolume', label: t('cars.specs.engineVolume'), value: selectedCar.value.engineVolume },
    { key: 'horsepower', label: t('cars.specs.horsepower'), value: selectedCar.value.horsepower },
    { key: 'torque', label: t('cars.specs.torque'), value: selectedCar.value.torque },
    { key: 'fuelConsumption', label: t('cars.specs.fuelConsumption'), value: selectedCar.value.fuelConsumption },
    { key: 'topSpeed', label: t('cars.specs.topSpeed'), value: selectedCar.value.topSpeed },
    { key: 'acceleration', label: t('cars.specs.acceleration'), value: selectedCar.value.acceleration }
  ];
});

function displaySpecValue(value) {
  if (value === null || value === undefined || value === '') {
    return '-';
  }

  return String(value);
}

const canSubmitRental = computed(() => {
  if (!selectedCar.value || !rentalForm.hours || Number(rentalForm.hours) <= 0) {
    return false;
  }

  if (authorized.value) {
    return true;
  }

  return Boolean(rentalForm.firstName && rentalForm.lastName && rentalForm.phone);
});

const priceRange = computed(() => {
  if (cars.value.length === 0) {
    return { min: 0, max: 0 };
  }

  const prices = cars.value
    .map((car) => Number(car.hourlyRentalPrice) || 0)
    .filter((price) => price >= 0);

  return {
    min: Math.min(...prices),
    max: Math.max(...prices)
  };
});

const releaseYearBounds = computed(() => {
  if (cars.value.length === 0) {
    return { min: 0, max: 0 };
  }

  const years = cars.value
    .map((car) => Number(car.releaseYear) || 0)
    .filter((year) => year > 0);

  if (years.length === 0) {
    return { min: 0, max: 0 };
  }

  return {
    min: Math.min(...years),
    max: Math.max(...years)
  };
});

const brandOptions = computed(() => buildSelectOptions(cars.value.map((car) => car.brand)));
const fuelTypeOptions = computed(() => buildSelectOptions(cars.value.map((car) => car.engineType)));

const filteredCars = computed(() => {
  return cars.value.filter((car) => {
    if (filters.brand && car.brand !== filters.brand) {
      return false;
    }

    if (filters.fuelType && car.engineType !== filters.fuelType) {
      return false;
    }

    const currentYear = Number(car.releaseYear) || 0;
    if (currentYear < releaseYearRange.value[0] || currentYear > releaseYearRange.value[1]) {
      return false;
    }

    const currentPrice = Number(car.hourlyRentalPrice) || 0;
    return currentPrice <= maxHourlyPrice.value;
  });
});

watch(
  priceRange,
  (range) => {
    if (maxHourlyPrice.value < range.min || maxHourlyPrice.value > range.max || maxHourlyPrice.value === 0) {
      maxHourlyPrice.value = range.max;
    }
  },
  { immediate: true }
);

watch(
  releaseYearBounds,
  (bounds) => {
    if (
      releaseYearRange.value[0] < bounds.min ||
      releaseYearRange.value[1] > bounds.max ||
      (releaseYearRange.value[0] === 0 && releaseYearRange.value[1] === 0)
    ) {
      releaseYearRange.value = [bounds.min, bounds.max];
    }
  },
  { immediate: true }
);

function buildSelectOptions(values) {
  const uniqueValues = [...new Set(values.filter(Boolean))]
    .sort((first, second) => String(first).localeCompare(String(second), localeTag.value));

  return [
    { value: '', label: t('cars.filters.all') },
    ...uniqueValues.map((value) => ({ value, label: value }))
  ];
}

function resetFilters() {
  filters.brand = '';
  filters.fuelType = '';
  maxHourlyPrice.value = priceRange.value.max;
  releaseYearRange.value = [releaseYearBounds.value.min, releaseYearBounds.value.max];
}

function handleViewportResize() {
  viewportWidth.value = window.innerWidth;
  if (!isMobileFiltersMode.value) {
    isFiltersOpen.value = true;
  }
}

function toggleFilters() {
  if (!isMobileFiltersMode.value) {
    return;
  }

  isFiltersOpen.value = !isFiltersOpen.value;
}

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

    await api.createRental(payload);
    showSuccess(t('cars.rentalCreatedSuccess'));
    closeRentalModal();
  } catch (error) {
    showError(error.message);
  }
}

watch(() => rentalForm.hours, recalculatePrice);
watch(selectedCar, recalculatePrice);

onMounted(() => {
  handleViewportResize();
  isFiltersOpen.value = !isMobileFiltersMode.value;
  window.addEventListener('resize', handleViewportResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleViewportResize);
});

loadCars();
</script>

<template>
  <section class="page">
    <div class="page__toolbar">
      <h1 class="page__title">{{ t('cars.title') }}</h1>
    </div>

    <div class="cars-page-layout">
      <div>
        <p v-if="loading">{{ t('common.loading') }}</p>
        <p v-else-if="filteredCars.length === 0">{{ t('cars.filters.noResults') }}</p>
        <div v-else class="cars-grid">
          <CarCard v-for="car in filteredCars" :key="car.uuid" :car="car" @rent="openRentalModal" />
        </div>
      </div>

      <aside class="cars-filters">
        <div class="cars-filters__header">
          <h2 class="cars-filters__title">{{ t('cars.filters.title') }}</h2>
          <button
            v-if="isMobileFiltersMode"
            type="button"
            class="btn btn--ghost cars-filters__toggle"
            :aria-expanded="String(isFiltersOpen)"
            @click="toggleFilters"
          >
            {{ isFiltersOpen ? t('cars.filters.hide') : t('cars.filters.show') }}
          </button>
        </div>

        <div v-show="!isMobileFiltersMode || isFiltersOpen" class="cars-filters__content">
          <SelectFieldBuilder id="filterBrand" v-model="filters.brand" :label="t('cars.filters.brand')" :options="brandOptions" />
          <SelectFieldBuilder
            id="filterFuelType"
            v-model="filters.fuelType"
            :label="t('cars.filters.fuelType')"
            :options="fuelTypeOptions"
          />

          <div class="cars-filters__price-block">
            <p class="cars-filters__price-label">
              {{ t('cars.filters.maxPrice') }}: <strong>{{ formatUsd(maxHourlyPrice, localeTag) }}</strong>
            </p>
            <v-slider v-model="maxHourlyPrice" :min="priceRange.min" :max="priceRange.max" :step="1" thumb-label color="primary" />
          </div>

          <div class="cars-filters__price-block">
            <p class="cars-filters__price-label">
              {{ t('cars.filters.releaseYearRange') }}:
              <strong>{{ releaseYearRange[0] }} - {{ releaseYearRange[1] }}</strong>
            </p>
            <v-range-slider
              v-model="releaseYearRange"
              :min="releaseYearBounds.min"
              :max="releaseYearBounds.max"
              :step="1"
              thumb-label
              color="primary"
            />
          </div>

          <button type="button" class="btn cars-filters__reset" @click="resetFilters">{{ t('cars.filters.reset') }}</button>
        </div>
      </aside>
    </div>

    <BaseModal :is-open="isRentalModalOpen" :title="t('cars.rentModalTitle')" @close="closeRentalModal">
      <div class="rental-modal-layout">
        <form class="form-grid" @submit.prevent="submitRental">
          <div v-if="selectedCar" class="rental-car-preview">
            <v-img
              :src="selectedCarImageSrc"
              :alt="`${selectedCar.brand} ${selectedCar.model}`"
              class="rental-car-preview__image"
              cover
            />
            <p class="rental-car-preview__title"><strong>{{ selectedCar.brand }} {{ selectedCar.model }}</strong></p>
          </div>

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

        <aside v-if="selectedCar" class="rental-specs-panel">
          <h3 class="rental-specs-panel__title">{{ t('cars.specs.title') }}</h3>
          <div class="rental-specs-panel__grid">
            <div v-for="spec in selectedCarTechnicalSpecs" :key="spec.key" class="rental-specs-panel__item">
              <span class="rental-specs-panel__label">{{ spec.label }}</span>
              <span class="rental-specs-panel__value">{{ displaySpecValue(spec.value) }}</span>
            </div>
          </div>
        </aside>
      </div>
    </BaseModal>
  </section>
</template>

<style scoped>
.rental-modal-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 260px;
  gap: 14px;
  align-items: start;
}

.rental-car-preview {
  display: grid;
  gap: 8px;
}

.rental-car-preview__image {
  width: 100%;
  max-height: 210px;
  border-radius: 10px;
}

.rental-car-preview__title {
  margin: 0;
}

.rental-specs-panel {
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 10px;
  background: #f9fafb;
}

.rental-specs-panel__title {
  margin: 0 0 10px;
  font-size: 16px;
}

.rental-specs-panel__grid {
  display: grid;
  gap: 8px;
}

.rental-specs-panel__item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  border-bottom: 1px dashed #d1d5db;
  padding-bottom: 6px;
}

.rental-specs-panel__item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.rental-specs-panel__label {
  color: #6b7280;
}

.rental-specs-panel__value {
  font-weight: 600;
}

@media (max-width: 900px) {
  .rental-modal-layout {
    grid-template-columns: 1fr;
  }
}
</style>

