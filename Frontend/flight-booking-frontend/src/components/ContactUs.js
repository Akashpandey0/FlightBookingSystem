import React, { useState } from 'react';

const ContactUs = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: ''
  });
  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
    // Reset form
    setFormData({
      name: '',
      email: '',
      subject: '',
      message: ''
    });
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-lg-8">
          <div className="card shadow-lg border-0" style={{borderRadius: '20px'}}>
            <div className="card-header text-center" style={{background: 'linear-gradient(45deg, #667eea, #764ba2)', borderRadius: '20px 20px 0 0'}}>
              <h2 className="text-white mb-0">
                <i className="fas fa-envelope me-3"></i>Contact Us
              </h2>
            </div>
            <div className="card-body p-5">
              {submitted && (
                <div className="alert alert-success border-0" style={{borderRadius: '15px'}}>
                  <i className="fas fa-check-circle me-2"></i>
                  Thank you for your message! We'll get back to you soon.
                </div>
              )}
              
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-4">
                    <label className="form-label fw-semibold">
                      <i className="fas fa-user me-2"></i>Full Name
                    </label>
                    <input
                      type="text"
                      className="form-control form-control-lg"
                      name="name"
                      value={formData.name}
                      onChange={handleChange}
                      placeholder="Enter your full name"
                      style={{borderRadius: '15px', border: '2px solid #e9ecef'}}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-4">
                    <label className="form-label fw-semibold">
                      <i className="fas fa-envelope me-2"></i>Email Address
                    </label>
                    <input
                      type="email"
                      className="form-control form-control-lg"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      placeholder="Enter your email"
                      style={{borderRadius: '15px', border: '2px solid #e9ecef'}}
                      required
                    />
                  </div>
                </div>
                
                <div className="mb-4">
                  <label className="form-label fw-semibold">
                    <i className="fas fa-tag me-2"></i>Subject
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-lg"
                    name="subject"
                    value={formData.subject}
                    onChange={handleChange}
                    placeholder="Enter subject"
                    style={{borderRadius: '15px', border: '2px solid #e9ecef'}}
                    required
                  />
                </div>
                
                <div className="mb-4">
                  <label className="form-label fw-semibold">
                    <i className="fas fa-comment me-2"></i>Message
                  </label>
                  <textarea
                    className="form-control form-control-lg"
                    name="message"
                    value={formData.message}
                    onChange={handleChange}
                    rows="5"
                    placeholder="Enter your message"
                    style={{borderRadius: '15px', border: '2px solid #e9ecef'}}
                    required
                  ></textarea>
                </div>
                
                <button
                  type="submit"
                  className="btn btn-lg w-100"
                  style={{
                    background: 'linear-gradient(45deg, #667eea, #764ba2)',
                    border: 'none',
                    borderRadius: '15px',
                    color: 'white',
                    fontWeight: '600'
                  }}
                >
                  <i className="fas fa-paper-plane me-2"></i>Send Message
                </button>
              </form>
            </div>
          </div>
        </div>
        
        <div className="col-lg-4">
          <div className="card shadow-lg border-0" style={{borderRadius: '20px'}}>
            <div className="card-header text-center" style={{background: 'linear-gradient(45deg, #28a745, #20c997)', borderRadius: '20px 20px 0 0'}}>
              <h4 className="text-white mb-0">
                <i className="fas fa-info-circle me-2"></i>Get In Touch
              </h4>
            </div>
            <div className="card-body p-4">
              <div className="mb-4">
                <h6 className="text-primary">
                  <i className="fas fa-map-marker-alt me-2"></i>Address
                </h6>
                <p className="text-muted small">
                  123 Flight Street<br/>
                  Aviation City, AC 12345<br/>
                  India
                </p>
              </div>
              
              <div className="mb-4">
                <h6 className="text-primary">
                  <i className="fas fa-phone me-2"></i>Phone
                </h6>
                <p className="text-muted small">+91 9999999999</p>
              </div>
              
              <div className="mb-4">
                <h6 className="text-primary">
                  <i className="fas fa-envelope me-2"></i>Email
                </h6>
                <p className="text-muted small">support@flightbooking.com</p>
              </div>
              
              <div className="mb-4">
                <h6 className="text-primary">
                  <i className="fas fa-clock me-2"></i>Business Hours
                </h6>
                <p className="text-muted small">
                  Monday - Friday: 9:00 AM - 6:00 PM<br/>
                  Saturday: 10:00 AM - 4:00 PM<br/>
                  Sunday: Closed
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContactUs;