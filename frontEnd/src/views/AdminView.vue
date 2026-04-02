<script setup>
import { reactive, ref } from 'vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
import { api } from '../api';

const actions = [
  {
    key: 'carBrand',
    title: 'Создать бренд',
    submit: (payload) => api.createCarBrand(payload),
    fields: [{ key: 'name', label: 'Название бренда' }]
  },
  {
    key: 'carModel',
    title: 'Создать модель',
    submit: (payload) => api.createCarModel(payload),
    fields: [{ key: 'name', label: 'Название модели' }]
  },
  {
    key: 'engineType',
    title: 'Создать тип двигателя',
    submit: (payload) => api.createEngineType(payload),
    fields: [{ key: 'engineType', label: 'Тип двигателя' }]
  },
  {
    key: 'engineSpecs',
    title: 'Создать engine specs',
    submit: (payload) => api.createEngineSpecs(payload),
    fields: [
      { key: 'engineTypeUuid', label: 'Engine Type UUID' },
      { key: 'fuelConsumption', label: 'Расход топлива' },
      { key: 'horsepower', label: 'Лошадиные силы', type: 'number' },
      { key: 'torque', label: 'Крутящий момент', type: 'number' }
    ]
  },
  {
    key: 'engine',
    title: 'Создать двигатель',
    submit: (payload) => api.createEngine(payload),
    fields: [{ key: 'engineSpecsUuid', label: 'Engine Specs UUID' }]
  },
  {
    key: 'carSpecs',
    title: 'Создать car specs',
    submit: (payload) => api.createCarSpecs(payload),
    fields: [
      { key: 'carBrandUuid', label: 'Car Brand UUID' },
      { key: 'carModelUuid', label: 'Car Model UUID' },
      { key: 'acceleration', label: 'Разгон 0-100' },
      { key: 'topSpeed', label: 'Макс. скорость', type: 'number' },
      { key: 'releaseYear', label: 'Год выпуска', type: 'number' }
    ]
  },
  {
    key: 'car',
    title: 'Создать автомобиль',
    submit: (payload) => api.createCar(payload),
    fields: [
      { key: 'engineUuid', label: 'Engine UUID' },
      { key: 'carSpecsUuid', label: 'Car Specs UUID' },
      { key: 'price', label: 'Базовая цена аренды / час', type: 'number' },
      { key: 'imageUrl', label: 'Ссылка на фото' }
    ],
    selects: [
      {
        key: 'available',
        label: 'Доступность',
        options: [
          { label: 'Да', value: 'true' },
          { label: 'Нет', value: 'false' }
        ]
      }
    ],
    normalize: (payload) => ({ ...payload, available: payload.available === 'true', price: Number(payload.price) })
  },
  {
    key: 'user',
    title: 'Создать пользователя',
    submit: (payload) => api.createUser(payload),
    fields: [
      { key: 'email', label: 'Email' },
      { key: 'login', label: 'Логин' },
      { key: 'password', label: 'Пароль', type: 'password' }
    ],
    selects: [
      {
        key: 'role',
        label: 'Роль',
        options: [
          { label: 'USER', value: 'USER' },
          { label: 'ADMIN', value: 'ADMIN' }
        ]
      }
    ]
  },
  {
    key: 'loyalty',
    title: 'Создать правило лояльности',
    submit: (payload) => api.createLoyaltyRule(payload),
    fields: [
      { key: 'minHours', label: 'Мин. часов', type: 'number' },
      { key: 'maxHours', label: 'Макс. часов (опционально)', type: 'number' },
      { key: 'multiplier', label: 'Коэффициент', type: 'number' }
    ],
    selects: [
      {
        key: 'active',
        label: 'Активно',
        options: [
          { label: 'Да', value: 'true' },
          { label: 'Нет', value: 'false' }
        ]
      }
    ],
    normalize: (payload) => ({
      ...payload,
      minHours: Number(payload.minHours),
      maxHours: payload.maxHours ? Number(payload.maxHours) : null,
      multiplier: Number(payload.multiplier),
      active: payload.active === 'true'
    })
  }
];

const currentAction = ref(null);
const form = reactive({});
const errorMessage = ref('');
const successMessage = ref('');

function open(action) {
  currentAction.value = action;
  errorMessage.value = '';
  successMessage.value = '';
  Object.keys(form).forEach((key) => delete form[key]);
  action.fields?.forEach((field) => {
    form[field.key] = '';
  });
  action.selects?.forEach((select) => {
    form[select.key] = '';
  });
}

function close() {
  currentAction.value = null;
}

async function submit() {
  if (!currentAction.value) {
    return;
  }

  errorMessage.value = '';
  successMessage.value = '';

  try {
    let payload = { ...form };
    if (currentAction.value.normalize) {
      payload = currentAction.value.normalize(payload);
    }

    await currentAction.value.submit(payload);
    successMessage.value = 'Сущность успешно создана';
  } catch (error) {
    errorMessage.value = error.message;
  }
}
</script>

<template>
  <section class="page">
    <h1 class="page__title">Админ панель</h1>
    <div class="admin-grid">
      <button v-for="action in actions" :key="action.key" type="button" class="btn" @click="open(action)">
        {{ action.title }}
      </button>
    </div>

    <BaseModal :is-open="Boolean(currentAction)" :title="currentAction?.title || ''" @close="close">
      <form v-if="currentAction" class="form-grid" @submit.prevent="submit">
        <TextFieldBuilder
          v-for="field in currentAction.fields"
          :id="field.key"
          :key="field.key"
          v-model="form[field.key]"
          :label="field.label"
          :type="field.type || 'text'"
        />

        <SelectFieldBuilder
          v-for="select in currentAction.selects || []"
          :id="select.key"
          :key="select.key"
          v-model="form[select.key]"
          :label="select.label"
          :options="select.options"
        />

        <p v-if="successMessage" class="text-success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="close">Закрыть</button>
          <button type="submit" class="btn">Создать</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>

