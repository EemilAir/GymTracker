import axios from 'axios';

// Create an axios instance
const axiosInstance = axios.create({
  baseURL: 'https://gym-tracker-gymtracker.2.rahtiapp.fi/api', // BackEnd URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to include auth token if available
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;