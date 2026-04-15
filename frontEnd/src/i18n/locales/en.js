export default {
  app: {
    name: 'Diploma Rent Cars'
  },
  languages: {
    ru: 'Russian',
    ro: 'Romanian',
    en: 'English'
  },
  common: {
    loading: 'Loading...',
    noData: 'No data',
    selectPlaceholder: 'Select a value',
    actions: {
      close: 'Close',
      create: 'Create',
      edit: 'Edit',
      understood: 'Understood',
      submitRequest: 'Submit request',
      backToLogin: 'Back to login',
      register: 'Register',
      login: 'Login',
      logout: 'Logout'
    },
    status: {
      available: 'Available',
      unavailable: 'Unavailable',
      yes: 'Yes',
      no: 'No'
    },
    roles: {
      ADMIN: 'Administrator',
      USER: 'User'
    },
    labels: {
      login: 'Login',
      email: 'Email',
      password: 'Password',
      role: 'Role',
      firstName: 'First name',
      lastName: 'Last name',
      phone: 'Phone number',
      hours: 'Rental duration (hours)',
      language: 'Language',
      actions: 'Actions',
      photo: 'Car photo'
    }
  },
  header: {
    cars: 'Cars list',
    adminPanel: 'Admin panel',
    profile: 'Profile',
    loginAccount: 'Log in',
    language: 'Language'
  },
  home: {
    title: 'Car rental service',
    description: 'Choose a car, set the rental duration, and get a price estimate with the loyalty program applied.'
  },
  auth: {
    login: {
      title: 'Login',
      submit: 'Log in',
      registerLink: 'Register'
    },
    register: {
      title: 'Registration',
      submit: 'Register',
      backToLogin: 'Back to login',
      successFallback: 'Registration completed successfully'
    }
  },
  profile: {
    title: 'Profile',
    login: 'Login',
    email: 'Email',
    role: 'Role'
  },
  carCard: {
    year: 'Year',
    topSpeed: 'Top speed',
    acceleration: 'Acceleration',
    engine: 'Engine',
    rent: 'Rent',
    pricePerHour: '₽ / hour',
    kmPerHour: 'km/h',
    liters: 'L'
  },
  cars: {
    title: 'Car rental',
    rentModalTitle: 'Rental request',
    price: 'Price',
    loyaltyMultiplier: 'Loyalty multiplier',
    selectedCar: 'Car',
    submitRental: 'Submit request'
  },
  admin: {
    title: 'Admin panel',
    sidebarTitle: 'Directories',
    empty: 'No data',
    createSuccess: 'Record created successfully',
    editDialogTitle: 'Editing',
    editNotImplemented: 'The editing UI is already prepared. Backend PUT/PATCH endpoints still need to be added.',
    entities: {
      carBrand: {
        title: 'Brands',
        createTitle: 'Create brand',
        columns: {
          name: 'Name'
        },
        fields: {
          name: 'Brand name'
        }
      },
      carModel: {
        title: 'Models',
        createTitle: 'Create model',
        columns: {
          name: 'Name'
        },
        fields: {
          name: 'Model name'
        }
      },
      engineType: {
        title: 'Engine types',
        createTitle: 'Create engine type',
        columns: {
          engineType: 'Engine type'
        },
        fields: {
          engineType: 'Engine type'
        }
      },
      engine: {
        title: 'Engines',
        createTitle: 'Create engine',
        columns: {
          engineName: 'Engine name',
          engineType: 'Engine type',
          engineVolume: 'Volume, L',
          horsepower: 'Horsepower',
          torque: 'Torque',
          fuelConsumption: 'Fuel consumption'
        },
        fields: {
          engineName: 'Engine name',
          engineTypeUuid: 'Engine type',
          engineVolume: 'Engine volume (L)',
          fuelConsumption: 'Fuel consumption',
          horsepower: 'Horsepower',
          torque: 'Torque'
        }
      },
      carSpecs: {
        title: 'Car specs',
        createTitle: 'Create car specs',
        columns: {
          brand: 'Brand',
          model: 'Model',
          releaseYear: 'Release year',
          topSpeed: 'Top speed',
          acceleration: 'Acceleration'
        },
        fields: {
          carBrandUuid: 'Brand',
          carModelUuid: 'Model',
          releaseYear: 'Release year',
          topSpeed: 'Top speed',
          acceleration: '0-100 acceleration'
        }
      },
      car: {
        title: 'Cars',
        createTitle: 'Create car',
        columns: {
          brand: 'Brand',
          model: 'Model',
          releaseYear: 'Release year',
          hourlyRentalPrice: 'Price/hour',
          available: 'Availability'
        },
        fields: {
          engineUuid: 'Engine',
          carSpecsUuid: 'Car specs',
          price: 'Base rental price / hour',
          available: 'Availability',
          image: 'Car photo'
        }
      },
      loyalty: {
        title: 'Loyalty',
        createTitle: 'Create loyalty rule',
        columns: {
          minHours: 'Min hours',
          maxHours: 'Max hours',
          multiplier: 'Multiplier',
          active: 'Active'
        },
        fields: {
          minHours: 'Min hours',
          maxHours: 'Max hours (optional)',
          multiplier: 'Multiplier',
          active: 'Active'
        }
      },
      rentalOrders: {
        title: 'Rental requests',
        createTitle: 'Rental requests',
        columns: {
          customer: 'Customer',
          phone: 'Phone',
          car: 'Car',
          hours: 'Rental hours',
          totalPrice: 'Amount',
          status: 'Status',
          createdAt: 'Request date'
        }
      }
    }
  },
  errors: {
    browserCryptoUnsupported: 'Your browser does not support the Web Crypto API',
    publicKeyMissing: 'Fill PUBLIC_KEY_PEM with a real RSA public key in PEM format',
    requestFailed: 'Request failed: {status}'
  }
};


