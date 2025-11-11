const DEFAULT_DATE_OPTIONS = {
  year: 'numeric',
  month: 'short',
  day: 'numeric',
  hour: '2-digit',
  minute: '2-digit',
};

export const formatDateTime = (value, options = {}) => {
  if (!value) {
    return 'Unknown';
  }

  try {
    const date = value instanceof Date ? value : new Date(value);
    if (Number.isNaN(date.getTime())) {
      return 'Unknown';
    }

    return new Intl.DateTimeFormat(undefined, { ...DEFAULT_DATE_OPTIONS, ...options }).format(date);
  } catch (error) {
    return 'Unknown';
  }
};

export const pluralize = (count, singular, plural = `${singular}s`) => {
  return `${count} ${count === 1 ? singular : plural}`;
};

export const safeArray = (value) => (Array.isArray(value) ? value : []);

export default {
  formatDateTime,
  pluralize,
  safeArray,
};
