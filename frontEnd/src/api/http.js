import { clearAuth, getToken } from '../utils/auth';

const BASE_URL = 'http://localhost:8080';

export async function http(path, options = {}) {
  const isFormData = options.body instanceof FormData;
  const headers = {
    ...(options.headers || {})
  };

  if (!isFormData) {
    headers['Content-Type'] = 'application/json';
  }

  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers
  });

  const text = await response.text();
  const data = parseResponseText(text);

  if (!response.ok) {
    if (response.status === 401 || response.status === 403) {
      clearAuth();
    }
    throw new Error(data?.message || data || `Request failed: ${response.status}`);
  }

  return data;
}

function parseResponseText(text) {
  if (!text) {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}
