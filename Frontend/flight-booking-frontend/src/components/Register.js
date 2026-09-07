import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phoneNumber: ''
  });
  
  const countryCodes = [
    { code: '+1', country: 'US/Canada' },
    { code: '+91', country: 'India' },
    { code: '+44', country: 'UK' },
    { code: '+49', country: 'Germany' },
    { code: '+33', country: 'France' },
    { code: '+86', country: 'China' },
    { code: '+81', country: 'Japan' },
    { code: '+61', country: 'Australia' },
    { code: '+55', country: 'Brazil' },
    { code: '+7', country: 'Russia' }
  ];
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const validatePassword = (password) => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
    return regex.test(password);
  };

  const validateEmail = (email) => {
    return email.endsWith('@gmail.com');
  };
  
  const validatePhoneNumber = (phone) => {
    const regex = /^\+[1-9]\d{1,14}$/;
    return regex.test(phone);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (!validateEmail(formData.email)) {
      setError('Email must be a valid Gmail address');
      setLoading(false);
      return;
    }

    if (!validatePassword(formData.password)) {
      setError('Password must be 8-16 characters with at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character');
      setLoading(false);
      return;
    }
    
    if (!validatePhoneNumber(formData.phoneNumber)) {
      setError('Phone number must start with country code (e.g., +1, +91, +44)');
      setLoading(false);
      return;
    }

    try {
      const response = await authAPI.register(formData);
      setError('');
      setSuccess(response.data.message || 'Registration successful! Please check your email for the verification OTP.');
      // Redirect to OTP verification after 2 seconds
      setTimeout(() => {
        navigate('/verify-email-otp', { 
          state: { email: response.data.email || formData.email },
          replace: true 
        });
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed');
      setSuccess('');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 d-flex align-items-center py-5" style={{background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'}}>
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-7">
            <div className="card shadow-lg border-0" style={{borderRadius: '20px', backdropFilter: 'blur(10px)', background: 'rgba(255,255,255,0.95)'}}>
              <div className="card-body p-5">
                <div className="text-center mb-4">
                  <div className="mb-3">
                    <i className="fas fa-user-plus" style={{fontSize: '3rem', color: '#667eea'}}></i>
                  </div>
                  <h2 className="card-title fw-bold" style={{color: '#333'}}>Create Account</h2>
                  <p className="text-muted">Join us for seamless flight booking</p>
                </div>
            {error && (
              <div className="alert alert-danger border-0" style={{borderRadius: '15px', background: 'rgba(220, 53, 69, 0.1)', color: '#dc3545'}}>
                <i className="fas fa-exclamation-triangle me-2"></i>{error}
              </div>
            )}
            
            {success && (
              <div className="alert alert-success border-0" style={{borderRadius: '15px', background: 'rgba(40, 167, 69, 0.1)', color: '#28a745'}}>
                <i className="fas fa-check-circle me-2"></i>{success}
                <div className="mt-2 small">
                  <i className="fas fa-clock me-1"></i>Redirecting to login in 3 seconds...
                </div>
              </div>
            )}
            
            <form onSubmit={handleSubmit}>
              <div className="row">
                <div className="col-md-6 mb-4">
                  <label className="form-label fw-semibold" style={{color: '#555'}}>
                    <i className="fas fa-user me-2"></i>First Name
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-lg"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    placeholder="Enter first name"
                    style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                    onFocus={(e) => e.target.style.borderColor = '#667eea'}
                    onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                    required
                  />
                </div>
                <div className="col-md-6 mb-4">
                  <label className="form-label fw-semibold" style={{color: '#555'}}>
                    <i className="fas fa-user me-2"></i>Last Name
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-lg"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    placeholder="Enter last name"
                    style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                    onFocus={(e) => e.target.style.borderColor = '#667eea'}
                    onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                    required
                  />
                </div>
              </div>
              
              <div className="mb-4">
                <label className="form-label fw-semibold" style={{color: '#555'}}>
                  <i className="fas fa-at me-2"></i>Username
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  placeholder="Choose a username"
                  style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                  onFocus={(e) => e.target.style.borderColor = '#667eea'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                  required
                />
              </div>
              
              <div className="mb-4">
                <label className="form-label fw-semibold" style={{color: '#555'}}>
                  <i className="fas fa-envelope me-2"></i>Email
                </label>
                <input
                  type="email"
                  className="form-control form-control-lg"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="your.email@gmail.com"
                  pattern="^[a-zA-Z0-9._%+-]+@gmail\.com$"
                  title="Email must be a valid Gmail address"
                  style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                  onFocus={(e) => e.target.style.borderColor = '#667eea'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                  required
                />
                <div className="form-text text-muted">
                  <i className="fas fa-info-circle me-1"></i>Only Gmail addresses are allowed
                </div>
              </div>
              
              <div className="mb-4">
                <label className="form-label fw-semibold" style={{color: '#555'}}>
                  <i className="fas fa-phone me-2"></i>Phone Number
                </label>
                <div className="input-group">
                  <select 
                    className="form-select" 
                    style={{maxWidth: '140px', borderRadius: '15px 0 0 15px', border: '2px solid #e9ecef'}}
                    onChange={(e) => {
                      const countryCode = e.target.value;
                      const phoneWithoutCode = formData.phoneNumber.replace(/^\+\d+/, '');
                      setFormData({...formData, phoneNumber: countryCode + phoneWithoutCode});
                    }}
                  >
                    <option value="">Country</option>
                    {countryCodes.map((item) => (
                      <option key={item.code} value={item.code}>
                        {item.code} {item.country}
                      </option>
                    ))}
                  </select>
                  <input
                    type="tel"
                    className="form-control form-control-lg"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    placeholder="+1234567890"
                    pattern="^\+[1-9]\d{1,14}$"
                    title="Phone number must start with country code"
                    style={{borderRadius: '0 15px 15px 0', border: '2px solid #e9ecef', borderLeft: 'none', transition: 'all 0.3s'}}
                    onFocus={(e) => e.target.style.borderColor = '#667eea'}
                    onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                    required
                  />
                </div>
                <div className="form-text text-muted">
                  <i className="fas fa-info-circle me-1"></i>Include country code (e.g., +1, +91, +44)
                </div>
              </div>
              
              <div className="mb-4">
                <label className="form-label fw-semibold" style={{color: '#555'}}>
                  <i className="fas fa-lock me-2"></i>Password
                </label>
                <input
                  type="password"
                  className="form-control form-control-lg"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Create a strong password"
                  pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$"
                  title="Password must be 8-16 characters with at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character"
                  style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                  onFocus={(e) => e.target.style.borderColor = '#667eea'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                  required
                />
                <div className="form-text text-muted">
                  <i className="fas fa-shield-alt me-1"></i>8-16 characters with 1 uppercase, 1 lowercase, 1 digit, 1 special character
                </div>
              </div>
              
              <button 
                type="submit" 
                className="btn btn-lg w-100 mb-4" 
                disabled={loading}
                style={{
                  background: 'linear-gradient(45deg, #667eea, #764ba2)',
                  border: 'none',
                  borderRadius: '15px',
                  color: 'white',
                  fontWeight: '600',
                  transition: 'all 0.3s',
                  transform: loading ? 'scale(0.98)' : 'scale(1)'
                }}
                onMouseEnter={(e) => !loading && (e.target.style.transform = 'scale(1.02)')}
                onMouseLeave={(e) => !loading && (e.target.style.transform = 'scale(1)')}
              >
                {loading ? (
                  <><span className="spinner-border spinner-border-sm me-2"></span>Creating Account...</>
                ) : (
                  <><i className="fas fa-user-plus me-2"></i>Create Account</>
                )}
              </button>
            </form>
            
            <div className="text-center">
              <p className="text-muted mb-0">
                Already have an account? 
                <Link 
                  to="/login" 
                  className="text-decoration-none fw-semibold ms-1"
                  style={{color: '#667eea', transition: 'all 0.3s'}}
                  onMouseEnter={(e) => e.target.style.color = '#764ba2'}
                  onMouseLeave={(e) => e.target.style.color = '#667eea'}
                >
                  Sign In
                </Link>
              </p>
            </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;