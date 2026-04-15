import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import { aliases, mdi } from 'vuetify/iconsets/mdi';

export default createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi
    }
  },
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#111827',
          secondary: '#1f2937',
          accent: '#2563eb',
          success: '#047857',
          warning: '#b45309',
          error: '#b91c1c',
          background: '#f3f4f6',
          surface: '#ffffff'
        }
      }
    }
  },
  defaults: {
    VBtn: {
      rounded: 'lg'
    },
    VTextField: {
      variant: 'outlined',
      density: 'comfortable',
      hideDetails: 'auto',
      color: 'primary'
    },
    VSelect: {
      variant: 'outlined',
      density: 'comfortable',
      hideDetails: 'auto',
      color: 'primary'
    },
    VCard: {
      rounded: 'xl',
      elevation: 1
    },
    VDialog: {
      maxWidth: 680
    }
  }
});

