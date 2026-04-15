import { reactive, readonly } from 'vue';

const toasts = reactive([]);
let toastId = 0;

function removeToast(id) {
  const index = toasts.findIndex((toast) => toast.id === id);
  if (index >= 0) {
    toasts.splice(index, 1);
  }
}

function showToast(message, type = 'info', options = {}) {
  if (!message) {
    return;
  }

  const id = ++toastId;
  const toast = {
    id,
    message,
    type,
    timeout: options.timeout ?? 4000
  };

  toasts.push(toast);

  if (toast.timeout > 0) {
    window.setTimeout(() => removeToast(id), toast.timeout);
  }

  return id;
}

export function useToast() {
  return {
    toasts: readonly(toasts),
    showToast,
    removeToast,
    success(message, options) {
      return showToast(message, 'success', options);
    },
    error(message, options) {
      return showToast(message, 'error', options);
    },
    info(message, options) {
      return showToast(message, 'info', options);
    },
    warning(message, options) {
      return showToast(message, 'warning', options);
    }
  };
}

