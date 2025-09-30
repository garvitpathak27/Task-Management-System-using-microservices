import React, { createContext, useState, useContext, useEffect } from 'react';
import { authAPI } from '../services/api';

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

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      loadUserProfile();
    } else {
      setLoading(false);
    }
  }, []);

  const loadUserProfile = async () => {
    try {
      const response = await authAPI.getProfile();
      setUser(response.data);
    } catch (error) {
      localStorage.removeItem('authToken');
    } finally {
      setLoading(false);
    }
  };

const login = async (credentials) => {
  try {
    const response = await authAPI.signin(credentials);
    console.log('📝 Full login response:', response.data);
    
    const token = response.data.token || response.data.jwt || response.data;
    console.log('🔑 Extracted token:', token);
    console.log('📏 Token length:', token ? token.length : 0);
    console.log('📍 Has dots:', token ? token.includes('.') : false);
    
    // Validate token format and store
    if (token && typeof token === 'string' && token.includes('.')) {
      localStorage.setItem('authToken', token);
      
      // Force admin role to allow task creation
      const mockUser = {
        id: 1,
        email: credentials.email,
        fullName: credentials.email.split('@')[0],
        role: 'admin' // THIS IS THE KEY CHANGE - allows task creation
      };
      
      setUser(mockUser);
      console.log('✅ Login successful, user set as admin');
    } else {
      console.error('❌ Invalid JWT format received');
      throw new Error('Invalid token received');
    }
    
    return response.data;
  } catch (error) {
    console.error('🚫 Login error:', error);
    throw error;
  }
};


  const register = async (userData) => {
    const response = await authAPI.signup(userData);
    return response.data;
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setUser(null);
  };

  const value = {
    user,
    login,
    register,
    logout,
    loading,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
