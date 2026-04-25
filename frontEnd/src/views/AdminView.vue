<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import BaseModal from '../components/ui/BaseModal.vue';
import TextFieldBuilder from '../components/form/TextFieldBuilder.vue';
import SelectFieldBuilder from '../components/form/SelectFieldBuilder.vue';
import { api } from '../api';
import { useToast } from '../composables/useToast';
import { LOCALE_TAGS } from '../i18n';
import { formatUsd } from '../utils/currency';

const { t, locale } = useI18n();
const { success: showSuccess, error: showError } = useToast();
const localeTag = computed(() => LOCALE_TAGS[locale.value] || LOCALE_TAGS.en);
const RENTAL_STATUS_VALUES = ['NEW', 'IN_PROGRESS', 'APPROVED', 'REJECTED', 'COMPLETED'];

function formatRentalStatus(status) {
  return t(`common.rentalOrderStatus.${status}`, status || '-');
}

const entities = [
  {
    key: 'carBrand',
    icon: 'mdi-tag-outline',
    columns: [{ key: 'name' }],
    list: () => api.getCarBrands(),
    create: (payload) => api.createCarBrand(payload),
    update: (uuid, payload) => api.updateCarBrand(uuid, payload),
    remove: (uuid) => api.deleteCarBrand(uuid),
    fields: [{ key: 'name' }]
  },
  {
    key: 'carModel',
    icon: 'mdi-car-info',
    columns: [{ key: 'name' }],
    list: () => api.getCarModels(),
    create: (payload) => api.createCarModel(payload),
    update: (uuid, payload) => api.updateCarModel(uuid, payload),
    remove: (uuid) => api.deleteCarModel(uuid),
    fields: [{ key: 'name' }]
  },
  {
    key: 'engineType',
    icon: 'mdi-engine-outline',
    columns: [{ key: 'engineType' }],
    list: () => api.getEngineTypes(),
    create: (payload) => api.createEngineType(payload),
    update: (uuid, payload) => api.updateEngineType(uuid, payload),
    remove: (uuid) => api.deleteEngineType(uuid),
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
    update: (uuid, payload) => api.updateEngine(uuid, payload),
    remove: (uuid) => api.deleteEngine(uuid),
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
    update: (uuid, payload) => api.updateCarSpecs(uuid, payload),
    remove: (uuid) => api.deleteCarSpecs(uuid),
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
      { key: 'hourlyRentalPrice', format: (value) => formatUsd(value, localeTag.value) },
      { key: 'available', format: (value) => (value ? t('common.status.available') : t('common.status.unavailable')) }
    ],
    list: () => api.getCars(),
    create: (payload, imageFile) => api.createCar(payload, imageFile),
    update: (uuid, payload, imageFile) => api.updateCar(uuid, payload, imageFile),
    remove: (uuid) => api.deleteCar(uuid),
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
      { key: 'price', type: 'number', fromRow: (row) => row.hourlyRentalPrice },
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
    update: (uuid, payload) => api.updateLoyaltyRule(uuid, payload),
    remove: (uuid) => api.deleteLoyaltyRule(uuid),
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
      { key: 'totalPrice', format: (value) => formatUsd(value, localeTag.value) },
      { key: 'status', format: (value) => formatRentalStatus(value) },
      { key: 'createdAt', format: (value) => new Date(value).toLocaleString(localeTag.value) }
    ],
    list: () => api.getAdminRentalOrders(),
    update: (uuid, payload) => api.updateRentalOrderStatus(uuid, payload.status),
    canCreate: false,
    fields: [],
    selects: [
      {
        key: 'status',
        options: RENTAL_STATUS_VALUES.map((value) => ({
          value,
          labelKey: `common.rentalOrderStatus.${value}`
        }))
      }
    ],
    normalize: (payload) => ({
      status: payload.status
    })
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

const isCarEditMode = computed(() => currentAction.value?.mode === 'edit' && currentAction.value?.entity?.key === 'car');
const currentCarImageSrc = computed(() => {
  if (!isCarEditMode.value) {
    return null;
  }

  const imageBase64 = currentAction.value?.row?.imageBase64;
  if (!imageBase64) {
    return null;
  }

  const contentType = currentAction.value?.row?.imageContentType || 'image/jpeg';
  return `data:${contentType};base64,${imageBase64}`;
});

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
  if (activeEntity.value.readOnly || activeEntity.value.canCreate === false || !activeEntity.value.create) {
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

async function openEdit(row) {
  if (activeEntity.value.readOnly || !activeEntity.value.update) {
    return;
  }

  currentAction.value = { mode: 'edit', entity: activeEntity.value, row };
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
    if (field.type === 'checkbox') {
      form[field.key] = Boolean(row[field.key]);
      return;
    }
    form[field.key] = field.fromRow ? field.fromRow(row) : (row[field.key] ?? '');
  });
  activeEntity.value.selects?.forEach((select) => {
    form[select.key] = row[select.key] ?? '';
  });
}

function close() {
  currentAction.value = null;
  resetForm();
}

function onFileChange(event) {
  const [file] = event.target.files || [];
  imageFile.value = file || null;
}

function hasActionColumn(entity) {
  return Boolean(entity.update || entity.remove);
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
  if (!currentAction.value) {
    return;
  }

  errorMessage.value = '';
  successMessage.value = '';

  try {
    let payload = { ...form };
    if (currentAction.value.entity.normalize) {
      payload = currentAction.value.entity.normalize(payload);
    }

    if (currentAction.value.mode === 'create') {
      await currentAction.value.entity.create(payload, imageFile.value);
      successMessage.value = t('admin.createSuccess');
    } else {
      await currentAction.value.entity.update(currentAction.value.row.uuid, payload, imageFile.value);
      successMessage.value = t('admin.updateSuccess');
    }
    showSuccess(successMessage.value);
    await loadRows();
    close();
  } catch (error) {
    errorMessage.value = error.message;
    showError(error.message);
  }
}

async function removeRow(row) {
  if (!activeEntity.value.remove || !confirm(t('admin.confirmDelete'))) {
    return;
  }

  try {
    await activeEntity.value.remove(row.uuid);
    showSuccess(t('admin.deleteSuccess'));
    await loadRows();
  } catch (error) {
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
            v-if="activeEntity.canCreate !== false && activeEntity.create"
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
              <th v-if="hasActionColumn(activeEntity)">{{ t('common.labels.actions') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="rows.length === 0">
              <td :colspan="activeEntity.columns.length + (hasActionColumn(activeEntity) ? 1 : 0)">{{ t('admin.empty') }}</td>
            </tr>
            <tr v-for="row in rows" :key="row.uuid">
              <td v-for="column in activeEntity.columns" :key="column.key">
                {{ column.format ? column.format(row[column.key]) : row[column.key] }}
              </td>
              <td v-if="hasActionColumn(activeEntity)">
                <button v-if="activeEntity.update" type="button" class="btn btn--ghost admin-icon-btn" :title="t('common.actions.edit')" @click="openEdit(row)">
                  <i class="mdi mdi-file-document-edit-outline"></i>
                </button>
                <button v-if="activeEntity.remove" type="button" class="btn btn--ghost admin-icon-btn" :title="t('common.actions.delete')" @click="removeRow(row)">
                  <i class="mdi mdi-delete-outline"></i>
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
      <form v-if="currentAction" class="form-grid" @submit.prevent="submit">
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

          <div v-if="isCarEditMode" class="admin-car-image-preview">
            <v-img
              v-if="currentCarImageSrc"
              :src="currentCarImageSrc"
              :alt="`${currentAction.row.brand} ${currentAction.row.model}`"
              class="admin-car-image-preview__image"
              cover
            />
            <p v-else class="admin-car-image-preview__empty">{{ t('admin.entities.car.noImageAttachedYet') }}</p>
          </div>

          <input id="carImage" class="field__control" type="file" accept="image/*" @change="onFileChange" />
        </div>

        <p v-if="successMessage" class="text-success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="text-error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button type="button" class="btn btn--ghost" @click="close">{{ t('common.actions.close') }}</button>
          <button type="submit" class="btn">{{ currentAction.mode === 'edit' ? t('common.actions.save') : t('common.actions.create') }}</button>
        </div>
      </form>
    </BaseModal>
  </section>
</template>

<style scoped>
.admin-car-image-preview {
  display: grid;
  gap: 8px;
  margin-bottom: 8px;
}

.admin-car-image-preview__image {
  width: 100%;
  max-height: 220px;
  border-radius: 10px;
}

.admin-car-image-preview__empty {
  margin: 0;
  color: #6b7280;
}
</style>

