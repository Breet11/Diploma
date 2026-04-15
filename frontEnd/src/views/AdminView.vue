<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
import { api } from '../api';
import { useToast } from '../composables/useToast';
import { LOCALE_TAGS } from '../i18n';

const { t, locale } = useI18n();
const { success: showSuccess, error: showError } = useToast();
const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);

const entities = [
  {
    key: 'carBrand',
    icon: 'mdi-tag-outline',
    columns: [{ key: 'name' }],
    list: () => api.getCarBrands(),
    create: (payload) => api.createCarBrand(payload),
    fields: [{ key: 'name' }]
  },
  {
    key: 'carModel',
    icon: 'mdi-car-info',
    columns: [{ key: 'name' }],
    list: () => api.getCarModels(),
    create: (payload) => api.createCarModel(payload),
    fields: [{ key: 'name' }]
  },
  {
    key: 'engineType',
    icon: 'mdi-engine-outline',
    columns: [{ key: 'engineType' }],
    list: () => api.getEngineTypes(),
    create: (payload) => api.createEngineType(payload),
    fields: [{ key: 'engineType' }]
  },
  {
    key: 'engine',
    icon: 'mdi-engine',
    columns: [
      { key: 'engineName' },
      { key: 'engineType' },
      { key: 'engineVolume' },
      { key: 'horsepower' },
      { key: 'torque' },
      { key: 'fuelConsumption' }
    ],
    list: () => api.getEngines(),
    create: (payload) => api.createEngine(payload),
    fields: [
      { key: 'engineName' },
      {
        key: 'engineTypeUuid',
        type: 'select',
        source: {
          list: () => api.getEngineTypes(),
          label: (item) => item.engineType
        }
      },
      { key: 'engineVolume', type: 'number' },
      { key: 'fuelConsumption' },
      { key: 'horsepower', type: 'number' },
      { key: 'torque', type: 'number' }
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
    icon: 'mdi-card-text-outline',
    columns: [
      { key: 'brand' },
      { key: 'model' },
      { key: 'releaseYear' },
      { key: 'topSpeed' },
      { key: 'acceleration' }
    ],
    list: () => api.getCarSpecs(),
    create: (payload) => api.createCarSpecs(payload),
    fields: [
      {
        key: 'carBrandUuid',
        type: 'select',
        source: {
          list: () => api.getCarBrands(),
          label: (item) => item.name
        }
      },
      {
        key: 'carModelUuid',
        type: 'select',
        source: {
          list: () => api.getCarModels(),
          label: (item) => item.name
        }
      },
      { key: 'releaseYear', type: 'number' },
      { key: 'topSpeed', type: 'number' },
      { key: 'acceleration' }
    ],
    normalize: (payload) => ({
      ...payload,
      releaseYear: Number(payload.releaseYear),
      topSpeed: Number(payload.topSpeed)
    })
  },
  {
    key: 'car',
    icon: 'mdi-car-side',
    columns: [
      { key: 'brand' },
      { key: 'model' },
      { key: 'releaseYear' },
      { key: 'hourlyRentalPrice' },
      { key: 'available', format: (value) => (value ? t('common.status.available') : t('common.status.unavailable')) }
    ],
    list: () => api.getCars(),
    create: (payload, imageFile) => api.createCar(payload, imageFile),
    fields: [
      {
        key: 'engineUuid',
        type: 'select',
        source: {
          list: () => api.getEngines(),
          label: (item) => `${item.engineName} (${item.engineType}, ${item.engineVolume} ${t('carCard.liters')})`
        }
      },
      {
        key: 'carSpecsUuid',
        type: 'select',
        source: {
          list: () => api.getCarSpecs(),
          label: (item) => `${item.brand} ${item.model}, ${item.releaseYear}`
        }
      },
      { key: 'price', type: 'number' },
      { key: 'available', type: 'checkbox' }
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
    icon: 'mdi-star-crescent-outline',
    columns: [
      { key: 'minHours' },
      { key: 'maxHours' },
      { key: 'multiplier' },
      { key: 'active', format: (value) => (value ? t('common.status.yes') : t('common.status.no')) }
    ],
    list: () => api.getLoyaltyRules(),
    create: (payload) => api.createLoyaltyRule(payload),
    fields: [
      { key: 'minHours', type: 'number' },
      { key: 'maxHours', type: 'number' },
      { key: 'multiplier', type: 'number' }
    ],
    selects: [
      {
        key: 'active',
        options: [
          { labelKey: 'common.status.yes', value: 'true' },
          { labelKey: 'common.status.no', value: 'false' }
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
    icon: 'mdi-clipboard-text-clock-outline',
    columns: [
      { key: 'customer' },
      { key: 'phone' },
      { key: 'car' },
      { key: 'hours' },
      { key: 'totalPrice' },
      { key: 'status' },
      { key: 'createdAt', format: (value) => new Date(value).toLocaleString(localeTag.value) }
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

function entityPath(entity, segment, fieldKey = null) {
  return fieldKey ? `admin.entities.${entity.key}.${segment}.${fieldKey}` : `admin.entities.${entity.key}.${segment}`;
}

function getEntityTitle(entity) {
  return t(entityPath(entity, 'title'));
}

function getEntityCreateTitle(entity) {
  return t(entityPath(entity, 'createTitle'));
}

function getColumnLabel(entity, column) {
  return t(entityPath(entity, 'columns', column.key));
}

function getFieldLabel(entity, field) {
  return t(entityPath(entity, 'fields', field.key));
}

function getSelectLabel(entity, select) {
  return t(entityPath(entity, 'fields', select.key));
}

function getSelectOptions(select) {
  return select.options.map((option) => ({
    ...option,
    label: option.labelKey ? t(option.labelKey) : option.label
  }));
}

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
    showError(error.message);
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
    showError(error.message);
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
    successMessage.value = t('admin.createSuccess');
    showSuccess(successMessage.value);
    await loadRows();
    close();
  } catch (error) {
    errorMessage.value = error.message;
    showError(error.message);
  }
}

onMounted(loadRows);
</script>

<template>
  <section class="page">
    <h1 class="page__title">{{ t('admin.title') }}</h1>

    <div class="admin-layout">
      <aside class="admin-sidebar">
        <p class="admin-sidebar__title">{{ t('admin.sidebarTitle') }}</p>
        <button
          v-for="entity in entities"
          :key="entity.key"
          type="button"
          class="admin-sidebar__item"
          :class="{ 'admin-sidebar__item--active': activeEntityKey === entity.key }"
          @click="activeEntityKey = entity.key"
        >
          <i class="mdi" :class="entity.icon"></i>
          {{ getEntityTitle(entity) }}
        </button>
      </aside>

      <div class="admin-main">
        <div class="admin-main__header">
          <h2 class="admin-main__title">{{ getEntityTitle(activeEntity) }}</h2>
          <button
            v-if="!activeEntity.readOnly"
            type="button"
            class="btn admin-icon-btn"
            :title="getEntityCreateTitle(activeEntity)"
            @click="openCreate"
          >
            <i class="mdi mdi-plus"></i>
          </button>
        </div>

        <p v-if="loading">{{ t('common.loading') }}</p>
        <p v-else-if="tableError" class="text-error">{{ tableError }}</p>

        <table v-else class="admin-table">
          <thead>
            <tr>
              <th v-for="column in activeEntity.columns" :key="column.key">{{ getColumnLabel(activeEntity, column) }}</th>
              <th v-if="!activeEntity.readOnly">{{ t('common.labels.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="rows.length === 0">
              <td :colspan="activeEntity.columns.length + (activeEntity.readOnly ? 0 : 1)">{{ t('admin.empty') }}</td>
            </tr>
            <tr v-for="row in rows" :key="row.uuid">
              <td v-for="column in activeEntity.columns" :key="column.key">
                {{ column.format ? column.format(row[column.key]) : row[column.key] }}
              </td>
              <td v-if="!activeEntity.readOnly">
                <button type="button" class="btn btn--ghost admin-icon-btn" :title="t('common.actions.edit')" @click="openEdit(row)">
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
      :title="currentAction?.mode === 'edit' ? t('admin.editDialogTitle') : (currentAction?.entity ? getEntityCreateTitle(currentAction.entity) : '')"
      @close="close"
    >
      <div v-if="currentAction?.mode === 'edit'" class="form-grid">
        <p>{{ t('admin.editNotImplemented') }}</p>
        <div class="form-actions">
          <button type="button" class="btn" @click="close">{{ t('common.actions.understood') }}</button>
        </div>
      </div>

      <form v-else-if="currentAction" class="form-grid" @submit.prevent="submit">
        <template v-for="field in currentAction.entity.fields" :key="field.key">
          <div v-if="field.type === 'checkbox'" class="field">
            <label :for="field.key" class="field__label">{{ getFieldLabel(currentAction.entity, field) }}</label>
            <input :id="field.key" v-model="form[field.key]" type="checkbox" class="field__checkbox" />
          </div>
          <SelectFieldBuilder
            v-else-if="field.type === 'select'"
            :id="field.key"
            v-model="form[field.key]"
            :label="getFieldLabel(currentAction.entity, field)"
            :options="getFieldOptions(field)"
          />
          <TextFieldBuilder
            v-else
            :id="field.key"
            v-model="form[field.key]"
            :label="getFieldLabel(currentAction.entity, field)"
            :type="field.type || 'text'"
          />
        </template>

        <SelectFieldBuilder
          v-for="select in currentAction.entity.selects || []"
          :id="select.key"
          :key="select.key"
          v-model="form[select.key]"
          :label="getSelectLabel(currentAction.entity, select)"
          :options="getSelectOptions(select)"
        />

        <div v-if="currentAction.entity.hasFile" class="field">
          <label class="field__label" for="carImage">{{ t('admin.entities.car.fields.image') }}</label>
          <input id="carImage" class="field__control" type="file" accept="image/*" @change="onFileChange" />
        </div>

        <p v-if="successMessage" class="text-success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="close">{{ t('common.actions.close') }}</button>
          <button type="submit" class="btn">{{ t('common.actions.create') }}</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>

