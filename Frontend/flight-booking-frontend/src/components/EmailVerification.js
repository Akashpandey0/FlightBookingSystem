import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';

const EmailVerification = () => {
  const [status, setStatus] = useState('verifying');
  const [message, setMessage] = useState('');
  const [searchParams] = useSearchParams();
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const token = searchParams.get('token');
    
    if (!token) {
      setStatus('error');
      setMessage('Invalid verification link');
      return;
    }

    const verifyEmail = async () => {
      try {
        const response = await authAPI.verifyEmail(token);
        setStatus('success');
        setMessage(response.data.message);
        
        // Auto login after verification
        if (response.data.token && response.data.user) {
          login(response.data.user, response.data.token);
          setTimeout(() => {
            navigate('/flights', { replace: true });
          }, 2000);
        }
      } catch (error) {
        setStatus('error');
        setMessage(error.response?.data?.error || 'Email verification failed');
      }
    };

    verifyEmail();
  }, [searchParams, login, navigate]);

  return (
    <div className="min-vh-100 d-flex align-items-center" style={{background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'}}>
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-5">
            <div className="card shadow-lg border-0" style={{borderRadius: '20px', backdropFilter: 'blur(10px)', background: 'rgba(255,255,255,0.95)'}}>
              <div className="card-body p-5 text-center">
                {status === 'verifying' && (
                  <>
                    <div className="mb-3">
                      <div className="spinner-border text-primary" style={{width: '3rem', height: '3rem'}}></div>
                    </div>
                    <h3 className="fw-bold" style={{color: '#333'}}>Verifying Email...</h3>
                    <p className="text-muted">Please wait while we verify your email address.</p>
                  </>
                )}
                
                {status === 'success' && (
                  <>
                    <div className="mb-3">
                      <i className="fas fa-check-circle" style={{fontSize: '3rem', color: '#28a745'}}></i>
                    </div>
                    <h3 className="fw-bold" style={{color: '#28a745'}}>Email Verified!</h3>
                    <p className="text-muted">{message}</p>
                    <p className="text-muted">Redirecting to flights...</p>
                  </>
                )}
                
                {status === 'error' && (
                  <>
                    <div className="mb-3">
                      <i className="fas fa-times-circle" style={{fontSize: '3rem', color: '#dc3545'}}></i>
                    </div>
                    <h3 className="fw-bold" style={{color: '#dc3545'}}>Verification Failed</h3>
                    <p className="text-muted">{message}</p>
                    <button 
                      className="btn btn-primary"
                      onClick={() => navigate('/login')}
                      style={{borderRadius: '15px'}}
                    >
                      Go to Login
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmailVerification;