import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
  verifyEmailOtp: (email, otp) => api.post('/auth/verify-email-otp', { email, otp }),
  resendEmailOtp: (email) => api.post('/auth/resend-email-otp', { email }),
  validateEmail: (email) => api.post('/auth/validate-email', { email }),
};

export const flightAPI = {
  searchFlights: (searchData) => api.post('/flights/search', searchData),
  getAllFlights: () => api.get('/flights'),
  getFlightById: (id) => api.get(`/flights/${id}`),
};

export const bookingAPI = {
  bookFlight: (bookingData) => api.post('/bookings', bookingData),
  getMyBookings: () => api.get('/bookings/my-bookings'),
  cancelBooking: (bookingReference) => api.put(`/bookings/${bookingReference}/cancel`),
  downloadTicket: (bookingReference) => api.get(`/bookings/${bookingReference}/ticket`, { responseType: 'blob' }),
};

export const adminAPI = {
  addFlight: (flightData) => api.post('/admin/flights', flightData),
  updateFlight: (id, flightData) => api.put(`/admin/flights/${id}`, flightData),
  deleteFlight: (id) => api.delete(`/admin/flights/${id}`),
  getAllBookings: () => api.get('/admin/bookings'),
};

export const paymentAPI = {
  verifyPayment: (paymentData) => api.post('/payment/verify', paymentData),
};

export default api;