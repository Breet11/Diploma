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

  getAdminRentalOrders() {
    return http('/rentals/admin');
  },

  getProfile() {
    return http('/profile/me');
  },

  createCarBrand(payload) {
    return http('/car-brands', { method: 'POST', body: JSON.stringify(payload) });
  },

  getCarBrands() {
    return http('/car-brands');
  },

  createCarModel(payload) {
    return http('/car-models', { method: 'POST', body: JSON.stringify(payload) });
  },

  getCarModels() {
    return http('/car-models');
  },

  createEngineType(payload) {
    return http('/engine-types', { method: 'POST', body: JSON.stringify(payload) });
  },

  getEngineTypes() {
    return http('/engine-types');
  },

  createEngineSpecs(payload) {
    return http('/engine-specs', { method: 'POST', body: JSON.stringify(payload) });
  },

  getEngineSpecs() {
    return http('/engine-specs');
  },

  createEngine(payload) {
    return http('/engines', { method: 'POST', body: JSON.stringify(payload) });
  },

  getEngines() {
    return http('/engines');
  },

  createCarSpecs(payload) {
    return http('/car-specs', { method: 'POST', body: JSON.stringify(payload) });
  },

  getCarSpecs() {
    return http('/car-specs');
  },

  createCar(payload, imageFile) {
    const formData = new FormData();
    formData.append('payload', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    if (imageFile) {
      formData.append('image', imageFile);
    }
    return http('/cars', { method: 'POST', body: formData });
  },

  getLoyaltyRules() {
    return http('/loyalty-rules');
  },

  createLoyaltyRule(payload) {
    return http('/loyalty-rules', { method: 'POST', body: JSON.stringify(payload) });
  }
};
