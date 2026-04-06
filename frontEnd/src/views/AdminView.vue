<script setup>
import { onMounted, reactive, ref, watch } from 'vue';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
import { api } from '../api';

const entities = [
  {
    key: 'carBrand',
    title: 'Бренды',
    createTitle: 'Создать бренд',
    icon: 'mdi-tag-outline',
    columns: [{ key: 'name', label: 'Название' }],
    list: () => api.getCarBrands(),
    create: (payload) => api.createCarBrand(payload),
    fields: [{ key: 'name', label: 'Название бренда' }]
  },
  {
    key: 'carModel',
    title: 'Модели',
    createTitle: 'Создать модель',
    icon: 'mdi-car-info',
    columns: [{ key: 'name', label: 'Название' }],
    list: () => api.getCarModels(),
    create: (payload) => api.createCarModel(payload),
    fields: [{ key: 'name', label: 'Название модели' }]
  },
  {
    key: 'engineType',
    title: 'Типы двигателя',
    createTitle: 'Создать тип двигателя',
    icon: 'mdi-engine-outline',
    columns: [{ key: 'engineType', label: 'Тип двигателя' }],
    list: () => api.getEngineTypes(),
    create: (payload) => api.createEngineType(payload),
    fields: [{ key: 'engineType', label: 'Тип двигателя' }]
  },
  {
    key: 'engine',
    title: 'Двигатели',
    createTitle: 'Создать двигатель',
    icon: 'mdi-engine',
    columns: [
      { key: 'engineName', label: 'Название двигателя' },
      { key: 'engineType', label: 'Тип двигателя' },
      { key: 'engineVolume', label: 'Объем, л' },
      { key: 'horsepower', label: 'Лошадиные силы' },
      { key: 'torque', label: 'Крутящий момент' },
      { key: 'fuelConsumption', label: 'Расход топлива' }
    ],
    list: () => api.getEngines(),
    create: (payload) => api.createEngine(payload),
    fields: [
      { key: 'engineName', label: 'Название двигателя' },
      {
        key: 'engineTypeUuid',
        label: 'Тип двигателя',
        type: 'select',
        source: {
          list: () => api.getEngineTypes(),
          label: (item) => item.engineType
        }
      },
      { key: 'engineVolume', label: 'Объем двигателя (л)', type: 'number' },
      { key: 'fuelConsumption', label: 'Расход топлива' },
      { key: 'horsepower', label: 'Лошадиные силы', type: 'number' },
      { key: 'torque', label: 'Крутящий момент', type: 'number' }
    ],
    normalize: (payload) => ({
      ...payload,
      engineVolume: Number(payload.engineVolume),
      horsepower: Number(payload.horsepower),
      torque: Number(payload.torque)
    })
  },
  {
    key: 'carSpecs',
    title: 'Характеристики авто',
    createTitle: 'Создать характеристики авто',
    icon: 'mdi-card-text-outline',
    columns: [
      { key: 'brand', label: 'Бренд' },
      { key: 'model', label: 'Модель' },
      { key: 'releaseYear', label: 'Год выпуска' },
      { key: 'topSpeed', label: 'Макс. скорость' },
      { key: 'acceleration', label: 'Разгон' }
    ],
    list: () => api.getCarSpecs(),
    create: (payload) => api.createCarSpecs(payload),
    fields: [
      {
        key: 'carBrandUuid',
        label: 'Бренд',
        type: 'select',
        source: {
          list: () => api.getCarBrands(),
          label: (item) => item.name
        }
      },
      {
        key: 'carModelUuid',
        label: 'Модель',
        type: 'select',
        source: {
          list: () => api.getCarModels(),
          label: (item) => item.name
        }
      },
      { key: 'releaseYear', label: 'Год выпуска', type: 'number' },
      { key: 'topSpeed', label: 'Макс. скорость', type: 'number' },
      { key: 'acceleration', label: 'Разгон 0-100' }
    ],
    normalize: (payload) => ({
      ...payload,
      releaseYear: Number(payload.releaseYear),
      topSpeed: Number(payload.topSpeed)
    })
  },
  {
    key: 'car',
    title: 'Автомобили',
    createTitle: 'Создать автомобиль',
    icon: 'mdi-car-side',
    columns: [
      { key: 'brand', label: 'Бренд' },
      { key: 'model', label: 'Модель' },
      { key: 'releaseYear', label: 'Год выпуска' },
      { key: 'hourlyRentalPrice', label: 'Цена/час' },
      { key: 'available', label: 'Доступность', format: (value) => (value ? 'Доступен' : 'Недоступен') }
    ],
    list: () => api.getCars(),
    create: (payload, imageFile) => api.createCar(payload, imageFile),
    fields: [
      {
        key: 'engineUuid',
        label: 'Двигатель',
        type: 'select',
        source: {
          list: () => api.getEngines(),
          label: (item) => `${item.engineName} (${item.engineType}, ${item.engineVolume} л)`
        }
      },
      {
        key: 'carSpecsUuid',
        label: 'Характеристики авто',
        type: 'select',
        source: {
          list: () => api.getCarSpecs(),
          label: (item) => `${item.brand} ${item.model}, ${item.releaseYear}`
        }
      },
      { key: 'price', label: 'Базовая цена аренды / час', type: 'number' },
      { key: 'available', label: 'Доступность', type: 'checkbox' }
    ],
    hasFile: true,
    normalize: (payload) => ({
      ...payload,
      price: Number(payload.price),
      available: Boolean(payload.available)
    })
  },
  {
    key: 'loyalty',
    title: 'Лояльность',
    createTitle: 'Создать правило лояльности',
    icon: 'mdi-star-crescent-outline',
    columns: [
      { key: 'minHours', label: 'Мин. часов' },
      { key: 'maxHours', label: 'Макс. часов' },
      { key: 'multiplier', label: 'Коэффициент' },
      { key: 'active', label: 'Активно', format: (value) => (value ? 'Да' : 'Нет') }
    ],
    list: () => api.getLoyaltyRules(),
    create: (payload) => api.createLoyaltyRule(payload),
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
  },
  {
    key: 'rentalOrders',
    title: 'Заявки на аренду',
    createTitle: 'Заявки на аренду',
    icon: 'mdi-clipboard-text-clock-outline',
    columns: [
      { key: 'customer', label: 'Клиент' },
      { key: 'phone', label: 'Телефон' },
      { key: 'car', label: 'Автомобиль' },
      { key: 'hours', label: 'Часы аренды' },
      { key: 'totalPrice', label: 'Сумма' },
      { key: 'status', label: 'Статус' },
      { key: 'createdAt', label: 'Дата заявки', format: (value) => new Date(value).toLocaleString('ru-RU') }
    ],
    list: () => api.getAdminRentalOrders(),
    readOnly: true
  }
];

const activeEntityKey = ref(entities[0].key);
const activeEntity = ref(entities[0]);

const rows = ref([]);
const loading = ref(false);
const tableError = ref('');

const currentAction = ref(null);
const form = reactive({});
const imageFile = ref(null);
const referenceOptions = reactive({});
const errorMessage = ref('');
const successMessage = ref('');

function createDropdownOption(item, labelFactory) {
  const { uuid, ...data } = item;
  return {
    value: uuid,
    label: labelFactory(item),
    data
  };
}

async function loadReferenceOptions(entity) {
  const selectFields = (entity.fields || []).filter((field) => field.type === 'select' && field.source);

  for (const field of selectFields) {
    const items = await field.source.list();
    referenceOptions[field.key] = items.map((item) => createDropdownOption(item, field.source.label));
  }
}

function getFieldOptions(field) {
  return referenceOptions[field.key] || [];
}

watch(activeEntityKey, () => {
  const entity = entities.find((item) => item.key === activeEntityKey.value);
  if (entity) {
    activeEntity.value = entity;
    loadRows();
  }
});

function resetForm() {
  Object.keys(form).forEach((key) => delete form[key]);
  imageFile.value = null;
}

async function openCreate() {
  if (activeEntity.value.readOnly) {
    return;
  }

  currentAction.value = { mode: 'create', entity: activeEntity.value };
  errorMessage.value = '';
  successMessage.value = '';
  resetForm();

  try {
    await loadReferenceOptions(activeEntity.value);
  } catch (error) {
    errorMessage.value = error.message;
  }

  activeEntity.value.fields?.forEach((field) => {
    form[field.key] = field.type === 'checkbox' ? false : '';
  });
  activeEntity.value.selects?.forEach((select) => {
    form[select.key] = '';
  });
}

function openEdit(row) {
  currentAction.value = { mode: 'edit', entity: activeEntity.value, row };
  errorMessage.value = '';
  successMessage.value = '';
}

function close() {
  currentAction.value = null;
  resetForm();
}

function onFileChange(event) {
  const [file] = event.target.files || [];
  imageFile.value = file || null;
}

async function loadRows() {
  loading.value = true;
  tableError.value = '';

  try {
    rows.value = await activeEntity.value.list();
  } catch (error) {
    tableError.value = error.message;
  } finally {
    loading.value = false;
  }
}

async function submit() {
  if (!currentAction.value || currentAction.value.mode !== 'create') {
    return;
  }

  errorMessage.value = '';
  successMessage.value = '';

  try {
    let payload = { ...form };
    if (currentAction.value.entity.normalize) {
      payload = currentAction.value.entity.normalize(payload);
    }

    await currentAction.value.entity.create(payload, imageFile.value);
    successMessage.value = 'Запись успешно создана';
    await loadRows();
  } catch (error) {
    errorMessage.value = error.message;
  }
}

onMounted(loadRows);
</script>

<template>
  <section class="page">
    <h1 class="page__title">Админ панель</h1>

    <div class="admin-layout">
      <aside class="admin-sidebar">
        <p class="admin-sidebar__title">Справочники</p>
        <button
          v-for="entity in entities"
          :key="entity.key"
          type="button"
          class="admin-sidebar__item"
          :class="{ 'admin-sidebar__item--active': activeEntityKey === entity.key }"
          @click="activeEntityKey = entity.key"
        >
          <i class="mdi" :class="entity.icon"></i>
          {{ entity.title }}
        </button>
      </aside>

      <div class="admin-main">
        <div class="admin-main__header">
          <h2 class="admin-main__title">{{ activeEntity.title }}</h2>
          <button
            v-if="!activeEntity.readOnly"
            type="button"
            class="btn admin-icon-btn"
            :title="activeEntity.createTitle"
            @click="openCreate"
          >
            <i class="mdi mdi-plus"></i>
          </button>
        </div>

        <p v-if="loading">Загрузка...</p>
        <p v-else-if="tableError" class="text-error">{{ tableError }}</p>

        <table v-else class="admin-table">
          <thead>
            <tr>
              <th v-for="column in activeEntity.columns" :key="column.key">{{ column.label }}</th>
              <th v-if="!activeEntity.readOnly">Действия</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="rows.length === 0">
              <td :colspan="activeEntity.columns.length + (activeEntity.readOnly ? 0 : 1)">Нет данных</td>
            </tr>
            <tr v-for="row in rows" :key="row.uuid">
              <td v-for="column in activeEntity.columns" :key="column.key">
                {{ column.format ? column.format(row[column.key]) : row[column.key] }}
              </td>
              <td v-if="!activeEntity.readOnly">
                <button type="button" class="btn btn--ghost admin-icon-btn" title="Редактировать" @click="openEdit(row)">
                  <i class="mdi mdi-file-document-edit-outline"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <BaseModal
      :is-open="Boolean(currentAction)"
      :title="currentAction?.mode === 'edit' ? 'Редактирование' : currentAction?.entity?.createTitle || ''"
      @close="close"
    >
      <div v-if="currentAction?.mode === 'edit'" class="form-grid">
        <p>Редактирование UI уже готово. Для сохранения нужно добавить backend PUT/PATCH endpoints.</p>
        <div class="form-actions">
          <button type="button" class="btn" @click="close">Понятно</button>
        </div>
      </div>

      <form v-else-if="currentAction" class="form-grid" @submit.prevent="submit">
        <template v-for="field in currentAction.entity.fields" :key="field.key">
          <div v-if="field.type === 'checkbox'" class="field">
            <label :for="field.key" class="field__label">{{ field.label }}</label>
            <input :id="field.key" v-model="form[field.key]" type="checkbox" class="field__checkbox" />
          </div>
          <SelectFieldBuilder
            v-else-if="field.type === 'select'"
            :id="field.key"
            v-model="form[field.key]"
            :label="field.label"
            :options="getFieldOptions(field)"
          />
          <TextFieldBuilder
            v-else
            :id="field.key"
            v-model="form[field.key]"
            :label="field.label"
            :type="field.type || 'text'"
          />
        </template>

        <SelectFieldBuilder
          v-for="select in currentAction.entity.selects || []"
          :id="select.key"
          :key="select.key"
          v-model="form[select.key]"
          :label="select.label"
          :options="select.options"
        />

        <div v-if="currentAction.entity.hasFile" class="field">
          <label class="field__label" for="carImage">Фото автомобиля</label>
          <input id="carImage" class="field__control" type="file" accept="image/*" @change="onFileChange" />
        </div>

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

