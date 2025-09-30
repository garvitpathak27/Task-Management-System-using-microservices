import axios from 'axios';

const API_BASE_URL = 'http://localhost:8090';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth API calls
export const authAPI = {
  signup: (userData) => api.post('/auth/signup', userData),      // CHANGED: signUp → signup
  signin: (credentials) => api.post('/auth/signin', credentials), // ALREADY CORRECT
  getProfile: () => api.get('/api/users/profile'),
};
// Task API calls
export const taskAPI = {
  createTask: (taskData) => api.post('/api/tasks', taskData),
  getTask: (taskId) => api.get(`/api/tasks/${taskId}`),
  getUserTasks: () => api.get('/api/tasks/user'),
  getAllTasks: () => api.get('/api/tasks'),
  updateTask: (taskId, taskData) => api.put(`/api/tasks/${taskId}`, taskData),
  assignTask: (taskId, userId) => api.put(`/api/tasks/${taskId}/user/${userId}/assigned`),
  completeTask: (taskId) => api.put(`/api/tasks/${taskId}/complete`),
  deleteTask: (taskId) => api.delete(`/api/tasks/${taskId}`),
};

// Submission API calls
export const submissionAPI = {
  createSubmission: (submissionData) => api.post('/api/submissions', submissionData),
  getSubmission: (submissionId) => api.get(`/api/submissions/${submissionId}`),
  getAllSubmissions: () => api.get('/submissions'),
  getTaskSubmissions: (taskId) => api.get(`/api/submissions/task/${taskId}`),
  updateSubmission: (submissionId, submissionData) => api.put(`/api/submissions/${submissionId}`, submissionData),
};

// User API calls
export const userAPI = {
  getAllUsers: () => api.get('/users'),
  getUserById: (userId) => api.get(`/api/users/${userId}`),
};

export default api;
