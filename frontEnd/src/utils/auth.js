import { ref } from 'vue';

const TOKEN_KEY = 'diploma_access_token';
const ROLE_KEY = 'diploma_role';

const token = ref(localStorage.getItem(TOKEN_KEY) || '');
const role = ref(localStorage.getItem(ROLE_KEY) || '');

function syncStorage() {
  if (token.value) {
    localStorage.setItem(TOKEN_KEY, token.value);
  } else {
    localStorage.removeItem(TOKEN_KEY);
  }

  if (role.value) {
    localStorage.setItem(ROLE_KEY, role.value);
  } else {
    localStorage.removeItem(ROLE_KEY);
  }
}

function decodeJwtPayload(rawToken) {
  try {
    const parts = rawToken.split('.');
    if (parts.length < 2) {
      return null;
    }

    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(Math.ceil(base64.length / 4) * 4, '=');
    const payload = atob(padded);
    return JSON.parse(payload);
  } catch {
    return null;
  }
}

function isTokenExpired(rawToken) {
  const payload = decodeJwtPayload(rawToken);
  if (!payload || typeof payload.exp !== 'number') {
    return true;
  }

  return Date.now() >= payload.exp * 1000;
}

function getValidTokenOrClear() {
  const currentToken = token.value;
  if (!currentToken) {
    return '';
  }

  if (isTokenExpired(currentToken)) {
    clearAuth();
    return '';
  }

  return currentToken;
}

export function saveAuth(auth) {
  token.value = auth?.accessToken || '';
  role.value = auth?.role || '';
  syncStorage();
}

export function clearAuth() {
  token.value = '';
  role.value = '';
  syncStorage();
}

export function getToken() {
  return getValidTokenOrClear();
}

export function getRole() {
  if (!getToken()) {
    return '';
  }
  return role.value;
}

export function isAuthenticated() {
  return Boolean(getToken());
}

export function isAdmin() {
  return isAuthenticated() && getRole() === 'ADMIN';
}

window.addEventListener('storage', (event) => {
  if (event.key === TOKEN_KEY) {
    token.value = event.newValue || '';
  }
  if (event.key === ROLE_KEY) {
    role.value = event.newValue || '';
  }
});

