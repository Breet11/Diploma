import { getToken } from '../utils/auth';

const BASE_URL = 'http://localhost:8080';

export async function http(path, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

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
