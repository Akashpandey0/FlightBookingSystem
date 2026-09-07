import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/animations.css';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import EmailOtpVerification from './components/EmailOtpVerification';
import FlightSearch from './components/FlightSearch';
import BookingHistory from './components/BookingHistory';
import AdminDashboard from './components/AdminDashboard';
import AboutUs from './components/AboutUs';
import ContactUs from './components/ContactUs';
import Landing from './components/Landing';
import { AuthProvider, useAuth } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Navbar />
          <Routes>
              <Route path="/login" element={<AuthRoute><Login /></AuthRoute>} />
              <Route path="/register" element={<AuthRoute><Register /></AuthRoute>} />
              <Route path="/verify-email-otp" element={<AuthRoute><EmailOtpVerification /></AuthRoute>} />
              <Route path="/flights" element={<ProtectedRoute><div className="container mt-4"><FlightSearch /></div></ProtectedRoute>} />
              <Route path="/bookings" element={<ProtectedRoute><div className="container mt-4"><BookingHistory /></div></ProtectedRoute>} />
              <Route path="/admin" element={<AdminRoute><div className="container mt-4"><AdminDashboard /></div></AdminRoute>} />
              <Route path="/about" element={<div className="container mt-4"><AboutUs /></div>} />
              <Route path="/contact" element={<div className="container mt-4"><ContactUs /></div>} />
              <Route path="/" element={<PublicRoute><Landing /></PublicRoute>} />
            </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

function ProtectedRoute({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/" />;
}

function AuthRoute({ children }) {
  const { user } = useAuth();
  return user ? <Navigate to="/flights" replace /> : children;
}

function AdminRoute({ children }) {
  const { user } = useAuth();
  return user && user.role === 'ADMIN' ? children : <Navigate to="/flights" />;
}

function PublicRoute({ children }) {
  const { user } = useAuth();
  return user ? <Navigate to="/flights" replace /> : children;
}

export default App;