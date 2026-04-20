import axios from 'axios';
import { clearAuth, getToken } from '../utils/auth';
import i18n from '../i18n';

const BASE_URL = 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

apiClient.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  if (config.data instanceof FormData) {
    // Let the browser set multipart boundary automatically.
    delete config.headers['Content-Type'];
  }

  return config;
});

apiClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error?.response?.status;
    if (status === 401 || status === 403) {
      clearAuth();
    }

    const fallbackMessage = i18n.global.t('errors.requestFailed', { status: status || 'network' });
    const responseData = error?.response?.data;
    const message =
      responseData?.message ||
      (typeof responseData === 'string' ? responseData : null) ||
      error?.message ||
      fallbackMessage;

    return Promise.reject(new Error(message));
  }
);
