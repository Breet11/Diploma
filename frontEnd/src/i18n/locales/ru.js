export default {
  app: {
    name: 'Diploma Rent Cars'
  },
  languages: {
    ru: 'Русский',
    ro: 'Română',
    en: 'English'
  },
  common: {
    loading: 'Загрузка...',
    noData: 'Нет данных',
    selectPlaceholder: 'Выберите значение',
    actions: {
      close: 'Закрыть',
      create: 'Создать',
      save: 'Сохранить',
      edit: 'Редактировать',
      delete: 'Удалить',
      understood: 'Понятно',
      submitRequest: 'Отправить заявку',
      backToLogin: 'Назад ко входу',
      register: 'Регистрация',
      login: 'Войти',
      logout: 'Выход'
    },
    status: {
      available: 'Доступен',
      unavailable: 'Недоступен',
      yes: 'Да',
      no: 'Нет'
    },
    roles: {
      ADMIN: 'Администратор',
      USER: 'Пользователь'
    },
    rentalOrderStatus: {
      NEW: 'Новая',
      IN_PROGRESS: 'В обработке',
      APPROVED: 'Подтверждена',
      REJECTED: 'Отклонена',
      COMPLETED: 'Завершена'
    },
    labels: {
      login: 'Логин',
      email: 'Email',
      password: 'Пароль',
      confirmPassword: 'Подтверждение пароля',
      role: 'Роль',
      firstName: 'Имя',
      lastName: 'Фамилия',
      phone: 'Номер телефона',
      hours: 'Длительность аренды (часы)',
      language: 'Язык',
      actions: 'Действия',
      photo: 'Фото автомобиля'
    }
  },
  header: {
    cars: 'Список автомобилей',
    adminPanel: 'Админ панель',
    profile: 'Личный кабинет',
    loginAccount: 'Войти в аккаунт',
    menu: 'Меню',
    language: 'Язык'
  },
  home: {
    title: 'Сервис аренды автомобилей',
    description: 'Выбирайте автомобиль, задавайте длительность аренды и получайте расчет цены с учетом программы лояльности.'
  },
  auth: {
    login: {
      title: 'Вход',
      submit: 'Войти',
      registerLink: 'Регистрация'
    },
    register: {
      title: 'Регистрация',
      submit: 'Зарегистрироваться',
      backToLogin: 'Назад ко входу',
      successFallback: 'Регистрация прошла успешно'
    }
  },
  profile: {
    title: 'Личный кабинет',
    login: 'Логин',
    email: 'Email',
    role: 'Роль',
    orderHistoryTitle: 'Мои заявки на аренду',
    orderHistoryEmpty: 'У вас пока нет заявок на аренду',
    orders: {
      car: 'Автомобиль',
      hours: 'Часы',
      totalPrice: 'Итоговая стоимость',
      status: 'Статус',
      createdAt: 'Создано'
    }
  },
  carCard: {
    year: 'Год',
    topSpeed: 'Макс. скорость',
    acceleration: 'Разгон',
    engine: 'Двигатель',
    rent: 'Арендовать',
    pricePerHour: '/ час',
    kmPerHour: 'км/ч',
    liters: 'л'
  },
  cars: {
    title: 'Аренда автомобилей',
    rentModalTitle: 'Оформление аренды',
    price: 'Стоимость',
    loyaltyMultiplier: 'Коэффициент лояльности',
    selectedCar: 'Автомобиль',
    submitRental: 'Отправить заявку'
  },
  admin: {
    title: 'Админ панель',
    sidebarTitle: 'Справочники',
    empty: 'Нет данных',
    createSuccess: 'Запись успешно создана',
    updateSuccess: 'Запись успешно обновлена',
    deleteSuccess: 'Запись успешно удалена',
    confirmDelete: 'Удалить эту запись?',
    editDialogTitle: 'Редактирование',
    editNotImplemented: 'Редактирование UI уже готово. Для сохранения нужно добавить backend PUT/PATCH endpoints.',
    entities: {
      carBrand: {
        title: 'Бренды',
        createTitle: 'Создать бренд',
        columns: {
          name: 'Название'
        },
        fields: {
          name: 'Название бренда'
        }
      },
      carModel: {
        title: 'Модели',
        createTitle: 'Создать модель',
        columns: {
          name: 'Название'
        },
        fields: {
          name: 'Название модели'
        }
      },
      engineType: {
        title: 'Типы двигателя',
        createTitle: 'Создать тип двигателя',
        columns: {
          engineType: 'Тип двигателя'
        },
        fields: {
          engineType: 'Тип двигателя'
        }
      },
      engine: {
        title: 'Двигатели',
        createTitle: 'Создать двигатель',
        columns: {
          engineName: 'Название двигателя',
          engineType: 'Тип двигателя',
          engineVolume: 'Объем, л',
          horsepower: 'Лошадиные силы',
          torque: 'Крутящий момент',
          fuelConsumption: 'Расход топлива'
        },
        fields: {
          engineName: 'Название двигателя',
          engineTypeUuid: 'Тип двигателя',
          engineVolume: 'Объем двигателя (л)',
          fuelConsumption: 'Расход топлива',
          horsepower: 'Лошадиные силы',
          torque: 'Крутящий момент'
        }
      },
      carSpecs: {
        title: 'Характеристики авто',
        createTitle: 'Создать характеристики авто',
        columns: {
          brand: 'Бренд',
          model: 'Модель',
          releaseYear: 'Год выпуска',
          topSpeed: 'Макс. скорость',
          acceleration: 'Разгон'
        },
        fields: {
          carBrandUuid: 'Бренд',
          carModelUuid: 'Модель',
          releaseYear: 'Год выпуска',
          topSpeed: 'Макс. скорость',
          acceleration: 'Разгон 0-100'
        }
      },
      car: {
        title: 'Автомобили',
        createTitle: 'Создать автомобиль',
        noImageAttachedYet: 'К этой записи пока не прикреплено изображение',
        columns: {
          brand: 'Бренд',
          model: 'Модель',
          releaseYear: 'Год выпуска',
          hourlyRentalPrice: 'Цена/час',
          available: 'Доступность'
        },
        fields: {
          engineUuid: 'Двигатель',
          carSpecsUuid: 'Характеристики авто',
          price: 'Базовая цена аренды / час',
          available: 'Доступность',
          image: 'Фото автомобиля'
        }
      },
      loyalty: {
        title: 'Лояльность',
        createTitle: 'Создать правило лояльности',
        columns: {
          minHours: 'Мин. часов',
          maxHours: 'Макс. часов',
          multiplier: 'Коэффициент',
          active: 'Активно'
        },
        fields: {
          minHours: 'Мин. часов',
          maxHours: 'Макс. часов (опционально)',
          multiplier: 'Коэффициент',
          active: 'Активно'
        }
      },
      rentalOrders: {
        title: 'Заявки на аренду',
        createTitle: 'Заявки на аренду',
        columns: {
          customer: 'Клиент',
          phone: 'Телефон',
          car: 'Автомобиль',
          hours: 'Часы аренды',
          totalPrice: 'Сумма',
          status: 'Статус',
          createdAt: 'Дата заявки'
        },
        fields: {
          status: 'Статус'
        }
      }
    }
  },
  errors: {
    browserCryptoUnsupported: 'Ваш браузер не поддерживает Web Crypto API',
    publicKeyMissing: 'Заполни PUBLIC_KEY_PEM реальным RSA public key в формате PEM',
    requestFailed: 'Ошибка запроса: {status}',
    passwordsDoNotMatch: 'Пароль и подтверждение пароля не совпадают'
  }
};


