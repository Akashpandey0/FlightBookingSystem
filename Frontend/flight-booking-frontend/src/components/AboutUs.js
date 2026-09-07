import React from 'react';

const AboutUs = () => {
  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-lg-10">
          <div className="card shadow-lg border-0" style={{borderRadius: '20px'}}>
            <div className="card-header text-center" style={{background: 'linear-gradient(45deg, #667eea, #764ba2)', borderRadius: '20px 20px 0 0'}}>
              <h2 className="text-white mb-0">
                <i className="fas fa-plane me-3"></i>About Us
              </h2>
            </div>
            <div className="card-body p-5">
              <div className="row mb-4">
                <div className="col-md-6">
                  <h4 className="text-primary mb-3">
                    <i className="fas fa-rocket me-2"></i>Our Mission
                  </h4>
                  <p className="text-muted">
                    To provide seamless, secure, and affordable flight booking experiences 
                    for travelers worldwide. We connect people to their destinations with 
                    ease and reliability.
                  </p>
                </div>
                <div className="col-md-6">
                  <h4 className="text-primary mb-3">
                    <i className="fas fa-eye me-2"></i>Our Vision
                  </h4>
                  <p className="text-muted">
                    To become the leading flight booking platform that transforms 
                    the way people travel by offering innovative solutions and 
                    exceptional customer service.
                  </p>
                </div>
              </div>
              
              <div className="row mb-4">
                <div className="col-12">
                  <h4 className="text-primary mb-3">
                    <i className="fas fa-star me-2"></i>Why Choose Us?
                  </h4>
                  <div className="row">
                    <div className="col-md-4 mb-3">
                      <div className="text-center p-3">
                        <i className="fas fa-shield-alt fa-3x text-success mb-3"></i>
                        <h6>Secure Payments</h6>
                        <p className="small text-muted">Your transactions are protected with advanced security</p>
                      </div>
                    </div>
                    <div className="col-md-4 mb-3">
                      <div className="text-center p-3">
                        <i className="fas fa-clock fa-3x text-warning mb-3"></i>
                        <h6>24/7 Support</h6>
                        <p className="small text-muted">Round-the-clock customer assistance</p>
                      </div>
                    </div>
                    <div className="col-md-4 mb-3">
                      <div className="text-center p-3">
                        <i className="fas fa-tags fa-3x text-info mb-3"></i>
                        <h6>Best Prices</h6>
                        <p className="small text-muted">Competitive pricing with no hidden fees</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div className="row">
                <div className="col-12">
                  <h4 className="text-primary mb-3">
                    <i className="fas fa-users me-2"></i>Our Team
                  </h4>
                  <p className="text-muted">
                    We are a dedicated team of travel enthusiasts and technology experts 
                    committed to making your flight booking experience smooth and enjoyable. 
                    Our platform is built with cutting-edge technology to ensure reliability, 
                    security, and user-friendly navigation.
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

export default AboutUs;