import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authAPI } from '../services/api';

const EmailOtpVerification = () => {
  const [otp, setOtp] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [resendLoading, setResendLoading] = useState(false);
  const [timer, setTimer] = useState(60);
  const [canResend, setCanResend] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email;

  useEffect(() => {
    if (!email) {
      navigate('/register');
      return;
    }

    const countdown = setInterval(() => {
      setTimer((prev) => {
        if (prev <= 1) {
          setCanResend(true);
          clearInterval(countdown);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(countdown);
  }, [email, navigate]);

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP');
      setLoading(false);
      return;
    }

    try {
      const response = await authAPI.verifyEmailOtp(email, otp);
      login(response.data.user, response.data.token);
      navigate('/flights', { replace: true });
    } catch (err) {
      setError(err.response?.data?.error || 'Invalid OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleResendOtp = async () => {
    setResendLoading(true);
    setError('');

    try {
      await authAPI.resendEmailOtp(email);
      setTimer(60);
      setCanResend(false);
      
      const countdown = setInterval(() => {
        setTimer((prev) => {
          if (prev <= 1) {
            setCanResend(true);
            clearInterval(countdown);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to resend OTP');
    } finally {
      setResendLoading(false);
    }
  };

  if (!email) {
    return null;
  }

  return (
    <div className="min-vh-100 d-flex align-items-center py-5" style={{background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'}}>
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-5">
            <div className="card shadow-lg border-0" style={{borderRadius: '20px', backdropFilter: 'blur(10px)', background: 'rgba(255,255,255,0.95)'}}>
              <div className="card-body p-5">
                <div className="text-center mb-4">
                  <div className="mb-3">
                    <i className="fas fa-envelope" style={{fontSize: '3rem', color: '#667eea'}}></i>
                  </div>
                  <h2 className="card-title fw-bold" style={{color: '#333'}}>Verify Email</h2>
                  <p className="text-muted">Enter the 6-digit OTP sent to</p>
                  <p className="fw-semibold" style={{color: '#667eea'}}>{email}</p>
                </div>

                {error && (
                  <div className="alert alert-danger border-0" style={{borderRadius: '15px', background: 'rgba(220, 53, 69, 0.1)', color: '#dc3545'}}>
                    <i className="fas fa-exclamation-triangle me-2"></i>{error}
                  </div>
                )}

                <form onSubmit={handleVerifyOtp}>
                  <div className="mb-4">
                    <label className="form-label fw-semibold" style={{color: '#555'}}>
                      <i className="fas fa-key me-2"></i>Enter OTP
                    </label>
                    <input
                      type="text"
                      className="form-control form-control-lg text-center"
                      value={otp}
                      onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                      placeholder="000000"
                      maxLength="6"
                      style={{
                        borderRadius: '15px', 
                        border: '2px solid #e9ecef', 
                        transition: 'all 0.3s',
                        fontSize: '1.5rem',
                        letterSpacing: '0.5rem'
                      }}
                      onFocus={(e) => e.target.style.borderColor = '#667eea'}
                      onBlur={(e) => e.target.style.borderColor = '#e9ecef'}
                      required
                    />
                  </div>

                  <button 
                    type="submit" 
                    className="btn btn-lg w-100 mb-3" 
                    disabled={loading || otp.length !== 6}
                    style={{
                      background: 'linear-gradient(45deg, #667eea, #764ba2)',
                      border: 'none',
                      borderRadius: '15px',
                      color: 'white',
                      fontWeight: '600',
                      transition: 'all 0.3s'
                    }}
                  >
                    {loading ? (
                      <><span className="spinner-border spinner-border-sm me-2"></span>Verifying...</>
                    ) : (
                      <><i className="fas fa-check me-2"></i>Verify OTP</>
                    )}
                  </button>
                </form>

                <div className="text-center">
                  <p className="text-muted mb-2">Didn't receive the OTP?</p>
                  {canResend ? (
                    <button
                      className="btn btn-link p-0 text-decoration-none fw-semibold"
                      onClick={handleResendOtp}
                      disabled={resendLoading}
                      style={{color: '#667eea'}}
                    >
                      {resendLoading ? (
                        <><span className="spinner-border spinner-border-sm me-2"></span>Sending...</>
                      ) : (
                        <><i className="fas fa-redo me-2"></i>Resend OTP</>
                      )}
                    </button>
                  ) : (
                    <p className="text-muted">
                      <i className="fas fa-clock me-2"></i>Resend in {timer}s
                    </p>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmailOtpVerification;