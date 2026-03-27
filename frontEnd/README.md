# Diploma Frontend (Vue)

Minimal Vue 3 + Vue Router frontend scaffold for the Diploma project.

## What is included

- App shell with shared header navigation
- Home page (`/`)
- Cars list page (`/cars`)
- Reusable `CarCard` component
- Reusable modal component (`BaseModal`)
- Reusable form field builders:
  - `TextFieldBuilder`
  - `SelectFieldBuilder`

## Project structure

- `src/router/index.js` - routes
- `src/views/HomeView.vue` - home page
- `src/views/CarsView.vue` - car list + create modal
- `src/components/layout/AppHeader.vue` - header/navigation
- `src/components/car/CarCard.vue` - product card
- `src/components/ui/BaseModal.vue` - modal wrapper
- `src/components/form/TextFieldBuilder.vue` - text input builder
- `src/components/form/SelectFieldBuilder.vue` - select builder
- `src/data/cars.mock.js` - temporary mocked data

## Run

```bash
npm install
npm run dev
```

## Build

```bash
npm run build
npm run preview
```

