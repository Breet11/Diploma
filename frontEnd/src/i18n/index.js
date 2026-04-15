import { createI18n } from 'vue-i18n';
import en from './locales/en';
import ro from './locales/ro';
import ru from './locales/ru';

export const SUPPORTED_LOCALES = ['ru', 'ro', 'en'];
export const LOCALE_TAGS = {
  ru: 'ru-RU',
  ro: 'ro-RO',
  en: 'en-US'
};

const STORAGE_KEY = 'diploma.locale';

function resolveInitialLocale() {
  if (typeof window === 'undefined') {
    return 'ru';
  }

  const storedLocale = window.localStorage.getItem(STORAGE_KEY);
  if (storedLocale && SUPPORTED_LOCALES.includes(storedLocale)) {
    return storedLocale;
  }

  const browserLocale = window.navigator.language?.split('-')[0] || 'ru';
  return SUPPORTED_LOCALES.includes(browserLocale) ? browserLocale : 'ru';
}

const messages = {
  ru,
  ro,
  en
};

const i18n = createI18n({
  legacy: false,
  locale: resolveInitialLocale(),
  fallbackLocale: 'en',
  messages
});

export function setLocale(nextLocale) {
  if (!SUPPORTED_LOCALES.includes(nextLocale)) {
    return;
  }

  i18n.global.locale.value = nextLocale;

  if (typeof window !== 'undefined') {
    window.localStorage.setItem(STORAGE_KEY, nextLocale);
    document.documentElement.lang = nextLocale;
  }
}

if (typeof window !== 'undefined') {
  document.documentElement.lang = i18n.global.locale.value;
}

export default i18n;

