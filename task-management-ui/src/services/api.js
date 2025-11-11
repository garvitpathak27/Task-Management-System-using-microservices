import axios from 'axios';

// Read base URL from env (build-time). Provide a safe fallback and normalize a few
// common misconfigurations (e.g. ":8090" or missing protocol).
let API_BASE_URL = process.env.REACT_APP_API_BASE_URL ?? 'http://localhost:8090';

// Normalization helpers:
const normalizeBaseUrl = (raw) => {
  if (!raw) return 'http://localhost:8090';
  // If user provided a port-only like ":8090", prepend localhost
  if (raw.startsWith(':')) return `http://localhost${raw}`;
  // If no protocol specified but contains host (e.g. "localhost:8090"), add http://
  if (!/^https?:\/\//i.test(raw)) return `http://${raw}`;
  return raw;
};

API_BASE_URL = normalizeBaseUrl(API_BASE_URL);
// Helpful debug info during development (will appear in browser console)
if (process.env.NODE_ENV === 'development') {
  // eslint-disable-next-line no-console
  console.debug('[api] Using API_BASE_URL =', API_BASE_URL);
}
const AUTH_TOKEN_KEY = 'authToken';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = window.localStorage.getItem(AUTH_TOKEN_KEY);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const buildPath = (path) => path.replace(/\/{2,}/g, '/');

export const authAPI = {
  signup: (userData) => api.post(buildPath('/auth/signup'), userData),
  signin: (credentials) => api.post(buildPath('/auth/signin'), credentials),
  getProfile: () => api.get(buildPath('/api/users/profile')),
};

export const taskAPI = {
  createTask: (taskData) => api.post(buildPath('/api/tasks'), taskData),
  getTask: (taskId) => api.get(buildPath(`/api/tasks/${taskId}`)),
  getUserTasks: () => api.get(buildPath('/api/tasks/user')),
  getAllTasks: () => api.get(buildPath('/api/tasks')),
  updateTask: (taskId, taskData) => api.put(buildPath(`/api/tasks/${taskId}`), taskData),
  assignTask: (taskId, userId) => api.put(buildPath(`/api/tasks/${taskId}/user/${userId}/assigned`)),
  completeTask: (taskId) => api.put(buildPath(`/api/tasks/${taskId}/complete`)),
  deleteTask: (taskId) => api.delete(buildPath(`/api/tasks/${taskId}`)),
};

export const submissionAPI = {
  createSubmission: (submissionData) => api.post(buildPath('/api/submissions'), submissionData),
  getSubmission: (submissionId) => api.get(buildPath(`/api/submissions/${submissionId}`)),
  getAllSubmissions: () => api.get(buildPath('/api/submissions')),
  getTaskSubmissions: (taskId) => api.get(buildPath(`/api/submissions/task/${taskId}`)),
  updateSubmission: (submissionId, submissionData) => api.put(buildPath(`/api/submissions/${submissionId}`), submissionData),
};

export const userAPI = {
  getAllUsers: () => api.get(buildPath('/api/users')),
  getUserById: (userId) => api.get(buildPath(`/api/users/${userId}`)),
};

export default api;
