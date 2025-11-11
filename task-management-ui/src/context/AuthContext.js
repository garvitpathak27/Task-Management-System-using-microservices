import React, { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { authAPI } from '../services/api';

const AUTH_TOKEN_KEY = 'authToken';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadUserProfile = useCallback(async () => {
    try {
      const response = await authAPI.getProfile();
      setUser(response.data);
    } catch (error) {
      window.localStorage.removeItem(AUTH_TOKEN_KEY);
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const token = window.localStorage.getItem(AUTH_TOKEN_KEY);
    if (token) {
      loadUserProfile();
    } else {
      setLoading(false);
    }
  }, [loadUserProfile]);

  const login = useCallback(
    async (credentials) => {
      const response = await authAPI.signin(credentials);
      const token = response?.data?.token ?? response?.data?.jwt ?? response?.data;

      if (!token || typeof token !== 'string') {
        throw new Error('Invalid token received from server');
      }

      window.localStorage.setItem(AUTH_TOKEN_KEY, token);
      await loadUserProfile();
      return response?.data;
    },
    [loadUserProfile]
  );

  const register = useCallback(async (userData) => {
    const response = await authAPI.signup(userData);
    return response.data;
  }, []);

  const logout = useCallback(() => {
    window.localStorage.removeItem(AUTH_TOKEN_KEY);
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({
      user,
      login,
      register,
      logout,
      loading,
      isAuthenticated: Boolean(user),
    }),
    [loading, login, logout, register, user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
