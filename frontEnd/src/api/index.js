import { http } from './http';

export const api = {
  login(payload) {
    return http('/user/login', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
  },

  register(payload) {
    return http('/user/register', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
  },

  getCars() {
    return http('/cars');
  },

  calculateRentalPrice(payload) {
    return http('/rentals/price', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
  },

  createRental(payload) {
    return http('/rentals', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
  },

  getProfile() {
    return http('/profile/me');
  },

  createCarBrand(payload) {
    return http('/car-brands', { method: 'POST', body: JSON.stringify(payload) });
  },

  createCarModel(payload) {
    return http('/car-models', { method: 'POST', body: JSON.stringify(payload) });
  },

  createEngineType(payload) {
    return http('/engine-types', { method: 'POST', body: JSON.stringify(payload) });
  },

  createEngineSpecs(payload) {
    return http('/engine-specs', { method: 'POST', body: JSON.stringify(payload) });
  },

  createEngine(payload) {
    return http('/engines', { method: 'POST', body: JSON.stringify(payload) });
  },

  createCarSpecs(payload) {
    return http('/car-specs', { method: 'POST', body: JSON.stringify(payload) });
  },

  createCar(payload) {
    return http('/cars', { method: 'POST', body: JSON.stringify(payload) });
  },

  createUser(payload) {
    return http('/users', { method: 'POST', body: JSON.stringify(payload) });
  },

  createLoyaltyRule(payload) {
    return http('/loyalty-rules', { method: 'POST', body: JSON.stringify(payload) });
  }
};
