export function formatUsd(value, localeTag = 'en-US') {
  const amount = Number(value ?? 0);
  return new Intl.NumberFormat(localeTag, {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(amount);
}

