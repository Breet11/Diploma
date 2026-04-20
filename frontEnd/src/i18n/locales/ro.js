export default {
  app: {
    name: 'Diploma Rent Cars'
  },
  languages: {
    ru: 'Rusă',
    ro: 'Română',
    en: 'Engleză'
  },
  common: {
    loading: 'Se încarcă...',
    noData: 'Nu există date',
    selectPlaceholder: 'Selectați o valoare',
    actions: {
      close: 'Închide',
      create: 'Creează',
      save: 'Salvează',
      edit: 'Editează',
      delete: 'Șterge',
      understood: 'Am înțeles',
      submitRequest: 'Trimite cererea',
      backToLogin: 'Înapoi la autentificare',
      register: 'Înregistrare',
      login: 'Autentificare',
      logout: 'Ieșire'
    },
    status: {
      available: 'Disponibil',
      unavailable: 'Indisponibil',
      yes: 'Da',
      no: 'Nu'
    },
    roles: {
      ADMIN: 'Administrator',
      USER: 'Utilizator'
    },
    labels: {
      login: 'Login',
      email: 'Email',
      password: 'Parolă',
      confirmPassword: 'Confirmare parolă',
      role: 'Rol',
      firstName: 'Prenume',
      lastName: 'Nume',
      phone: 'Număr de telefon',
      hours: 'Durata închirierii (ore)',
      language: 'Limbă',
      actions: 'Acțiuni',
      photo: 'Fotografia automobilului'
    }
  },
  header: {
    cars: 'Lista automobilelor',
    adminPanel: 'Panou admin',
    profile: 'Profil',
    loginAccount: 'Autentificare',
    language: 'Limbă'
  },
  home: {
    title: 'Serviciu de închiriere auto',
    description: 'Alegeți un automobil, setați durata închirierii și obțineți calculul prețului ținând cont de programul de loialitate.'
  },
  auth: {
    login: {
      title: 'Autentificare',
      submit: 'Intră',
      registerLink: 'Înregistrare'
    },
    register: {
      title: 'Înregistrare',
      submit: 'Creează cont',
      backToLogin: 'Înapoi la autentificare',
      successFallback: 'Înregistrarea a fost efectuată cu succes'
    }
  },
  profile: {
    title: 'Profil',
    login: 'Login',
    email: 'Email',
    role: 'Rol'
  },
  carCard: {
    year: 'An',
    topSpeed: 'Viteză maximă',
    acceleration: 'Accelerație',
    engine: 'Motor',
    rent: 'Închiriază',
    pricePerHour: '/ oră',
    kmPerHour: 'km/h',
    liters: 'l'
  },
  cars: {
    title: 'Închiriere automobile',
    rentModalTitle: 'Cerere de închiriere',
    price: 'Preț',
    loyaltyMultiplier: 'Coeficient de loialitate',
    selectedCar: 'Automobil',
    submitRental: 'Trimite cererea'
  },
  admin: {
    title: 'Panou admin',
    sidebarTitle: 'Nomenclatoare',
    empty: 'Nu există date',
    createSuccess: 'Înregistrarea a fost creată cu succes',
    updateSuccess: 'Înregistrarea a fost actualizată cu succes',
    deleteSuccess: 'Înregistrarea a fost ștearsă cu succes',
    confirmDelete: 'Ștergeți această înregistrare?',
    editDialogTitle: 'Editare',
    editNotImplemented: 'Interfața pentru editare este deja pregătită. Pentru salvare trebuie adăugate endpoint-uri backend PUT/PATCH.',
    entities: {
      carBrand: {
        title: 'Mărci',
        createTitle: 'Creează marcă',
        columns: {
          name: 'Denumire'
        },
        fields: {
          name: 'Denumirea mărcii'
        }
      },
      carModel: {
        title: 'Modele',
        createTitle: 'Creează model',
        columns: {
          name: 'Denumire'
        },
        fields: {
          name: 'Denumirea modelului'
        }
      },
      engineType: {
        title: 'Tipuri de motor',
        createTitle: 'Creează tip de motor',
        columns: {
          engineType: 'Tip de motor'
        },
        fields: {
          engineType: 'Tip de motor'
        }
      },
      engine: {
        title: 'Motoare',
        createTitle: 'Creează motor',
        columns: {
          engineName: 'Denumirea motorului',
          engineType: 'Tip de motor',
          engineVolume: 'Volum, l',
          horsepower: 'Cai putere',
          torque: 'Cuplu',
          fuelConsumption: 'Consum combustibil'
        },
        fields: {
          engineName: 'Denumirea motorului',
          engineTypeUuid: 'Tip de motor',
          engineVolume: 'Volumul motorului (l)',
          fuelConsumption: 'Consum combustibil',
          horsepower: 'Cai putere',
          torque: 'Cuplu'
        }
      },
      carSpecs: {
        title: 'Specificații auto',
        createTitle: 'Creează specificații auto',
        columns: {
          brand: 'Marcă',
          model: 'Model',
          releaseYear: 'An fabricație',
          topSpeed: 'Viteză maximă',
          acceleration: 'Accelerație'
        },
        fields: {
          carBrandUuid: 'Marcă',
          carModelUuid: 'Model',
          releaseYear: 'An fabricație',
          topSpeed: 'Viteză maximă',
          acceleration: 'Accelerație 0-100'
        }
      },
      car: {
        title: 'Automobile',
        createTitle: 'Creează automobil',
        columns: {
          brand: 'Marcă',
          model: 'Model',
          releaseYear: 'An fabricație',
          hourlyRentalPrice: 'Preț/oră',
          available: 'Disponibilitate'
        },
        fields: {
          engineUuid: 'Motor',
          carSpecsUuid: 'Specificații auto',
          price: 'Preț de bază al închirierii / oră',
          available: 'Disponibilitate',
          image: 'Fotografia automobilului'
        }
      },
      loyalty: {
        title: 'Loialitate',
        createTitle: 'Creează regulă de loialitate',
        columns: {
          minHours: 'Ore minime',
          maxHours: 'Ore maxime',
          multiplier: 'Coeficient',
          active: 'Activ'
        },
        fields: {
          minHours: 'Ore minime',
          maxHours: 'Ore maxime (opțional)',
          multiplier: 'Coeficient',
          active: 'Activ'
        }
      },
      rentalOrders: {
        title: 'Cereri de închiriere',
        createTitle: 'Cereri de închiriere',
        columns: {
          customer: 'Client',
          phone: 'Telefon',
          car: 'Automobil',
          hours: 'Ore de închiriere',
          totalPrice: 'Sumă',
          status: 'Status',
          createdAt: 'Data cererii'
        }
      }
    }
  },
  errors: {
    browserCryptoUnsupported: 'Browserul dumneavoastră nu suportă Web Crypto API',
    publicKeyMissing: 'Completați PUBLIC_KEY_PEM cu o cheie publică RSA reală în format PEM',
    requestFailed: 'Cererea a eșuat: {status}',
    passwordsDoNotMatch: 'Parola și confirmarea parolei nu coincid'
  }
};


