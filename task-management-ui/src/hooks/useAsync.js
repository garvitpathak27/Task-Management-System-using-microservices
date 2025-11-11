import { useCallback, useEffect, useRef, useState } from 'react';

const noop = () => {};

/**
 * Lightweight helper for managing async side-effects with loading and error states.
 */
export const useAsync = (asyncFunction, options = {}) => {
  const { immediate = false, onSuccess = noop, onError = noop, resetOnExecute = true } = options;
  const [status, setStatus] = useState('idle');
  const [value, setValue] = useState(null);
  const [error, setError] = useState(null);
  const mountedRef = useRef(true);

  useEffect(() => {
    return () => {
      mountedRef.current = false;
    };
  }, []);

  const execute = useCallback(
    async (...args) => {
      setStatus('pending');
      if (resetOnExecute) {
        setValue(null);
        setError(null);
      }

      try {
        const result = await asyncFunction(...args);
        if (!mountedRef.current) return result;

        setValue(result);
        setStatus('success');
        onSuccess(result);
        return result;
      } catch (err) {
        if (!mountedRef.current) throw err;

        setError(err);
        setStatus('error');
        onError(err);
        throw err;
      }
    },
    [asyncFunction, onError, onSuccess, resetOnExecute]
  );

  useEffect(() => {
    if (immediate) {
      execute();
    }
  }, [execute, immediate]);

  return {
    execute,
    status,
    value,
    error,
    isIdle: status === 'idle',
    isLoading: status === 'pending',
    isSuccess: status === 'success',
    isError: status === 'error',
  };
};

export default useAsync;
