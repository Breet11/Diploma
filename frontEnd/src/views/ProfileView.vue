<script setup>
import { computed, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { api } from '../api';
import { useToast } from '../composables/useToast';
import { LOCALE_TAGS } from '../i18n';
import { formatUsd } from '../utils/currency';

const profile = ref(null);
const rentalOrders = ref([]);
const loadingOrders = ref(false);
const { t, locale } = useI18n();
const { error: showError } = useToast();
const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);

function formatRentalStatus(status) {
  return t(`common.rentalOrderStatus.${status}`, status || '-');
}

onMounted(async () => {
  try {
    profile.value = await api.getProfile();
  } catch (error) {
    showError(error.message);
  }

  loadingOrders.value = true;
  try {
    rentalOrders.value = await api.getMyRentalOrders();
  } catch (error) {
    showError(error.message);
  } finally {
    loadingOrders.value = false;
  }
});
</script>

<template>
  <section class="page">
    <h1 class="page__title">{{ t('profile.title') }}</h1>
    <div v-if="profile" class="profile-card">
      <p><strong>{{ t('profile.login') }}:</strong> {{ profile.login }}</p>
      <p><strong>{{ t('profile.email') }}:</strong> {{ profile.email }}</p>
      <p><strong>{{ t('common.labels.firstName') }}:</strong> {{ profile.firstName }}</p>
      <p><strong>{{ t('common.labels.lastName') }}:</strong> {{ profile.lastName }}</p>
      <p><strong>{{ t('common.labels.phone') }}:</strong> {{ profile.phone }}</p>
      <p><strong>{{ t('profile.role') }}:</strong> {{ t(`common.roles.${profile.role}`) }}</p>
    </div>

    <div class="profile-card">
      <h2 class="admin-main__title">{{ t('profile.orderHistoryTitle') }}</h2>

      <p v-if="loadingOrders">{{ t('common.loading') }}</p>
      <p v-else-if="rentalOrders.length === 0">{{ t('profile.orderHistoryEmpty') }}</p>
      <table v-else class="admin-table">
        <thead>
          <tr>
            <th>{{ t('profile.orders.car') }}</th>
            <th>{{ t('profile.orders.hours') }}</th>
            <th>{{ t('profile.orders.totalPrice') }}</th>
            <th>{{ t('profile.orders.status') }}</th>
            <th>{{ t('profile.orders.createdAt') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in rentalOrders" :key="order.uuid">
            <td>{{ order.car }}</td>
            <td>{{ order.hours }}</td>
            <td>{{ formatUsd(order.totalPrice, localeTag) }}</td>
            <td>{{ formatRentalStatus(order.status) }}</td>
            <td>{{ new Date(order.createdAt).toLocaleString(localeTag) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

