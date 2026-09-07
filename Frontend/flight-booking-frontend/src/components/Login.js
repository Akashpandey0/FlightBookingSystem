import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';

const Login = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await authAPI.login(formData);
      login(response.data.user, response.data.token);
      navigate('/flights', { replace: true });
    } catch (err) {
      const errorMessage = err.response?.data?.error || 'Login failed';
      if (errorMessage.includes('verify your email')) {
        setError('Please verify your email first. Check your inbox for the verification link.');
      } else {
        setError(errorMessage);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 d-flex align-items-center" style={{background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'}}>
      <div className="container">
        <div className="row justify-content-center fade-in">
          <div className="col-md-5">
            <div className="card shadow-lg border-0 card-interactive" style={{borderRadius: '20px', backdropFilter: 'blur(10px)', background: 'rgba(255,255,255,0.95)'}}>
              <div className="card-body p-5">
                <div className="text-center mb-4">
                  <div className="mb-3">
                    <i className="fas fa-plane" style={{fontSize: '3rem', color: '#667eea'}}></i>
                  </div>
                  <h2 className="card-title fw-bold" style={{color: '#333'}}>Welcome Back</h2>
                  <p className="text-muted">Sign in to your account</p>
                </div>
            {error && (
              <div className="alert alert-danger border-0" style={{borderRadius: '15px', background: 'rgba(220, 53, 69, 0.1)', color: '#dc3545'}}>
                <i className="fas fa-exclamation-triangle me-2"></i>{error}
              </div>
            )}
            
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label className="form-label fw-semibold" style={{color: '#555'}}>
                  <i className="fas fa-user me-2"></i>Username
                </label>
                <input
                  type="text"
                  className="form-control form-control-lg"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  placeholder="Enter your username"
                  style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                  onFocus={(e) => e.target.style.borderColor = '#667eea'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                  required
                />
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
                  placeholder="Enter your password"
                  style={{borderRadius: '15px', border: '2px solid #e9ecef', transition: 'all 0.3s'}}
                  onFocus={(e) => e.target.style.borderColor = '#667eea'}
                  onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                  required
                />
              </div>
              
              <button 
                type="submit" 
                className="btn btn-lg w-100 mb-4 btn-interactive" 
                disabled={loading}
                style={{
                  background: 'linear-gradient(45deg, #667eea, #764ba2)',
                  border: 'none',
                  borderRadius: '15px',
                  color: 'white',
                  fontWeight: '600'
                }}
              >
                {loading ? (
                  <><div className="spinner-custom d-inline-block me-2" style={{width: '20px', height: '20px'}}></div>Signing In...</>
                ) : (
                  <><i className="fas fa-sign-in-alt me-2"></i>Sign In</>
                )}
              </button>
            </form>
            
            <div className="text-center">
              <p className="text-muted mb-0">
                Don't have an account? 
                <Link 
                  to="/register" 
                  className="text-decoration-none fw-semibold ms-1"
                  style={{color: '#667eea', transition: 'all 0.3s'}}
                  onMouseEnter={(e) => e.target.style.color = '#764ba2'}
                  onMouseLeave={(e) => e.target.style.color = '#667eea'}
                >
                  Create Account
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

export default Login;