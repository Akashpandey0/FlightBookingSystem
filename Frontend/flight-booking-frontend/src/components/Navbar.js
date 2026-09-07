import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/', { replace: true });
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark sticky-top" style={{background: 'linear-gradient(45deg, #667eea, #764ba2)', boxShadow: '0 2px 10px rgba(0,0,0,0.1)'}}>
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/" style={{fontSize: '1.5rem', transition: 'all 0.3s'}}>
          <i className="fas fa-plane me-2" style={{transition: 'transform 0.3s'}}></i>Flight Booking
        </Link>
        
        <div className="navbar-nav ms-auto">
          {user ? (
            <>
              <Link className="nav-link px-3" to="/flights" style={{transition: 'all 0.3s'}} 
                    onMouseEnter={(e) => e.target.style.transform = 'scale(1.05)'}
                    onMouseLeave={(e) => e.target.style.transform = 'scale(1)'}>
                <i className="fas fa-search me-1"></i>Search Flights
              </Link>
              <Link className="nav-link px-3" to="/bookings" style={{transition: 'all 0.3s'}}
                    onMouseEnter={(e) => e.target.style.transform = 'scale(1.05)'}
                    onMouseLeave={(e) => e.target.style.transform = 'scale(1)'}>
                <i className="fas fa-history me-1"></i>My Bookings
              </Link>
              {user.role === 'ADMIN' && (
                <Link className="nav-link px-3" to="/admin" style={{transition: 'all 0.3s'}}>
                  <i className="fas fa-cog me-1"></i>Admin
                </Link>
              )}
              <Link className="nav-link px-3" to="/about" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-info-circle me-1"></i>About
              </Link>
              <Link className="nav-link px-3" to="/contact" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-envelope me-1"></i>Contact
              </Link>
              <span className="nav-link px-3 text-light">
                <i className="fas fa-user me-1"></i>Welcome, {user.firstName}
              </span>
              <button className="btn btn-outline-light btn-sm ms-2 btn-interactive" onClick={handleLogout} 
                      style={{transition: 'all 0.3s'}}>
                <i className="fas fa-sign-out-alt me-1"></i>Logout
              </button>
            </>
          ) : (
            <>
              <Link className="nav-link px-3" to="/about" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-info-circle me-1"></i>About
              </Link>
              <Link className="nav-link px-3" to="/contact" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-envelope me-1"></i>Contact
              </Link>
              <Link className="nav-link px-3" to="/login" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-sign-in-alt me-1"></i>Login
              </Link>
              <Link className="nav-link px-3" to="/register" style={{transition: 'all 0.3s'}}>
                <i className="fas fa-user-plus me-1"></i>Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;