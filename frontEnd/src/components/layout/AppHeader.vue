<template>
  <v-app-bar color="primary" density="comfortable" flat>
    <v-container class="header__inner">
      <v-btn variant="text" class="header__logo" :to="{ name: 'home' }">
        {{ t('app.name') }}
      </v-btn>

      <v-spacer />

      <v-menu class="header__mobile-menu" location="bottom end">
        <template #activator="{ props: menuProps }">
          <v-btn
            v-bind="menuProps"
            icon
            variant="text"
            class="header__mobile-menu-btn"
            :title="t('header.menu')"
            :aria-label="t('header.menu')"
          >
            <v-icon icon="mdi-menu" />
          </v-btn>
        </template>

        <v-list density="comfortable" min-width="240">
          <v-list-item prepend-icon="mdi-car-side" :to="{ name: 'cars' }">
            <v-list-item-title>{{ t('header.cars') }}</v-list-item-title>
          </v-list-item>

          <v-list-item v-if="admin" prepend-icon="mdi-shield-crown-outline" :to="{ name: 'admin' }">
            <v-list-item-title>{{ t('header.adminPanel') }}</v-list-item-title>
          </v-list-item>

          <v-list-item prepend-icon="mdi-account-circle-outline" :to="accountRoute">
            <v-list-item-title>{{ authorized ? t('header.profile') : t('header.loginAccount') }}</v-list-item-title>
          </v-list-item>

          <v-divider class="my-1" />
          <v-list-subheader>
            <v-icon icon="mdi-translate" start />
            {{ t('header.language') }}
          </v-list-subheader>
          <v-list-item
            v-for="item in localeOptions"
            :key="`mobile-${item.value}`"
            :active="item.value === selectedLocale"
            @click="selectedLocale = item.value"
          >
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item>

          <v-divider v-if="authorized" class="my-1" />
          <v-list-item v-if="authorized" prepend-icon="mdi-logout" @click="logout">
            <v-list-item-title>{{ t('common.actions.logout') }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <div class="header__nav">
        <v-btn variant="text" :to="{ name: 'cars' }">
          {{ t('header.cars') }}
        </v-btn>
        <v-menu>
          <template #activator="{ props: menuProps }">
            <v-btn v-bind="menuProps" variant="text">
              <v-icon icon="mdi-translate" start />
              {{ currentLanguageLabel }}
            </v-btn>
          </template>

          <v-list density="compact">
            <v-list-item
              v-for="item in localeOptions"
              :key="item.value"
              :active="item.value === selectedLocale"
              @click="selectedLocale = item.value"
            >
              <v-list-item-title>{{ item.title }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
        <v-btn
          v-if="admin"
          icon
          variant="text"
          :to="{ name: 'admin' }"
          :title="t('header.adminPanel')"
          :aria-label="t('header.adminPanel')"
        >
          <v-icon icon="mdi-shield-crown-outline" />
        </v-btn>
        <v-btn
          icon
          variant="text"
          :to="accountRoute"
          :title="authorized ? t('header.profile') : t('header.loginAccount')"
          :aria-label="t('header.profile')"
        >
          <v-icon icon="mdi-account-circle-outline" />
        </v-btn>
        <v-btn v-if="authorized" variant="outlined" color="white" @click="logout">
          {{ t('common.actions.logout') }}
        </v-btn>
      </div>
    </v-container>
  </v-app-bar>
</template>

<script setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { setLocale } from '../../i18n';
import { clearAuth, isAdmin, isAuthenticated } from '../../utils/auth';

const router = useRouter();
const { t, locale } = useI18n();

const authorized = computed(() => isAuthenticated());
const admin = computed(() => isAdmin());
const accountRoute = computed(() => (authorized.value ? { name: 'profile' } : { name: 'login' }));
const localeOptions = computed(() => [
  { value: 'ru', title: t('languages.ru') },
  { value: 'ro', title: t('languages.ro') },
  { value: 'en', title: t('languages.en') }
]);
const currentLanguageLabel = computed(
  () => localeOptions.value.find((item) => item.value === locale.value)?.title || locale.value.toUpperCase()
);
const selectedLocale = computed({
  get: () => locale.value,
  set: (value) => setLocale(value)
});

function logout() {
  clearAuth();
  router.push({ name: 'cars' });
}
</script>
