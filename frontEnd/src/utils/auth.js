const TOKEN_KEY = 'diploma_access_token';
const ROLE_KEY = 'diploma_role';

export function saveAuth(auth) {
  localStorage.setItem(TOKEN_KEY, auth.accessToken);
  localStorage.setItem(ROLE_KEY, auth.role);
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(ROLE_KEY);
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || '';
}

export function getRole() {
  return localStorage.getItem(ROLE_KEY) || '';
}

export function isAuthenticated() {
  return Boolean(getToken());
}

export function isAdmin() {
  return getRole() === 'ADMIN';
}

