import { apiClient } from './http';

export const api = {

  login(payload) {
    return apiClient.post('/user/login', payload);
  },

  register(payload) {
    return apiClient.post('/user/register', payload);
  },

  getCars() {
    return apiClient.get('/cars');
  },

  calculateRentalPrice(payload) {
    return apiClient.post('/rentals/price', payload);
  },

  createRental(payload) {
    return apiClient.post('/rentals', payload);
  },

  getAdminRentalOrders() {
    return apiClient.get('/rentals/admin');
  },

  getProfile() {
    return apiClient.get('/profile/me');
  },

  createCarBrand(payload) {
    return apiClient.post('/car-brands', payload);
  },

  updateCarBrand(uuid, payload) {
    return apiClient.put(`/car-brands/${uuid}`, payload);
  },

  deleteCarBrand(uuid) {
    return apiClient.delete(`/car-brands/${uuid}`);
  },

  getCarBrands() {
    return apiClient.get('/car-brands');
  },

  createCarModel(payload) {
    return apiClient.post('/car-models', payload);
  },

  updateCarModel(uuid, payload) {
    return apiClient.put(`/car-models/${uuid}`, payload);
  },

  deleteCarModel(uuid) {
    return apiClient.delete(`/car-models/${uuid}`);
  },

  getCarModels() {
    return apiClient.get('/car-models');
  },

  createEngineType(payload) {
    return apiClient.post('/engine-types', payload);
  },

  updateEngineType(uuid, payload) {
    return apiClient.put(`/engine-types/${uuid}`, payload);
  },

  deleteEngineType(uuid) {
    return apiClient.delete(`/engine-types/${uuid}`);
  },

  getEngineTypes() {
    return apiClient.get('/engine-types');
  },

  createEngineSpecs(payload) {
    return apiClient.post('/engine-specs', payload);
  },

  getEngineSpecs() {
    return apiClient.get('/engine-specs');
  },

  createEngine(payload) {
    return apiClient.post('/engines', payload);
  },

  updateEngine(uuid, payload) {
    return apiClient.put(`/engines/${uuid}`, payload);
  },

  deleteEngine(uuid) {
    return apiClient.delete(`/engines/${uuid}`);
  },

  getEngines() {
    return apiClient.get('/engines');
  },

  createCarSpecs(payload) {
    return apiClient.post('/car-specs', payload);
  },

  updateCarSpecs(uuid, payload) {
    return apiClient.put(`/car-specs/${uuid}`, payload);
  },

  deleteCarSpecs(uuid) {
    return apiClient.delete(`/car-specs/${uuid}`);
  },

  getCarSpecs() {
    return apiClient.get('/car-specs');
  },

  createCar(payload, imageFile) {
    const formData = new FormData();
    formData.append('payload', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    if (imageFile) {
      formData.append('image', imageFile);
    }
    return apiClient.post('/cars', formData);
  },

  updateCar(uuid, payload, imageFile) {
    const formData = new FormData();
    formData.append('payload', new Blob([JSON.stringify(payload)], { type: 'application/json' }));
    if (imageFile) {
      formData.append('image', imageFile);
    }
    return apiClient.put(`/cars/${uuid}`, formData);
  },

  deleteCar(uuid) {
    return apiClient.delete(`/cars/${uuid}`);
  },

  getLoyaltyRules() {
    return apiClient.get('/loyalty-rules');
  },

  createLoyaltyRule(payload) {
    return apiClient.post('/loyalty-rules', payload);
  },

  updateLoyaltyRule(uuid, payload) {
    return apiClient.put(`/loyalty-rules/${uuid}`, payload);
  },

  deleteLoyaltyRule(uuid) {
    return apiClient.delete(`/loyalty-rules/${uuid}`);
  }
};
