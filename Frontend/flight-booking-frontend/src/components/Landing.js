import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Landing = () => {
  return (
    <div className="landing-page">
      {/* Hero Section */}
      <section className="hero-section" style={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        color: 'white'
      }}>
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 fade-in">
              <h1 className="display-4 fw-bold mb-4">
                Your Journey Begins Here
              </h1>
              <p className="lead mb-4">
                Book flights to anywhere in the world with ease. Compare prices, 
                choose your preferred airline, and travel with confidence.
              </p>
              <div className="d-flex gap-3">
                <Link to="/register" className="btn btn-light btn-lg px-4 btn-interactive">
                  <i className="fas fa-rocket me-2"></i>Get Started
                </Link>
                <Link to="/login" className="btn btn-outline-light btn-lg px-4 btn-interactive">
                  <i className="fas fa-sign-in-alt me-2"></i>Sign In
                </Link>
              </div>
            </div>
            <div className="col-lg-6 text-center">
              <i className="fas fa-plane" style={{
                fontSize: '15rem', 
                opacity: 0.8,
                animation: 'float 3s ease-in-out infinite'
              }}></i>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-5">
        <div className="container">
          <div className="row text-center mb-5">
            <div className="col-12 fade-in-up">
              <h2 className="display-5 fw-bold">Why Choose Us?</h2>
              <p className="lead text-muted">Experience seamless flight booking</p>
            </div>
          </div>
          <div className="row g-4">
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm card-interactive">
                <div className="card-body text-center p-4">
                  <i className="fas fa-search text-primary mb-3 pulse" style={{fontSize: '3rem'}}></i>
                  <h5 className="card-title">Easy Search</h5>
                  <p className="card-text">Find flights quickly with our advanced search filters</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm card-interactive">
                <div className="card-body text-center p-4">
                  <i className="fas fa-shield-alt text-success mb-3 pulse" style={{fontSize: '3rem', animationDelay: '0.5s'}}></i>
                  <h5 className="card-title">Secure Booking</h5>
                  <p className="card-text">Your data is protected with industry-standard security</p>
                </div>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card h-100 border-0 shadow-sm card-interactive">
                <div className="card-body text-center p-4">
                  <i className="fas fa-headset text-info mb-3 pulse" style={{fontSize: '3rem', animationDelay: '1s'}}></i>
                  <h5 className="card-title">24/7 Support</h5>
                  <p className="card-text">Get help whenever you need it from our support team</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* FAQ Section */}
      <section className="py-5">
        <div className="container">
          <div className="row">
            <div className="col-12 text-center mb-5 fade-in-up">
              <h2 className="display-5 fw-bold">Frequently Asked Questions</h2>
              <p className="lead text-muted">Get answers to common questions</p>
            </div>
          </div>
          <div className="row justify-content-center">
            <div className="col-lg-8">
              <FAQAccordion />
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-5" style={{background: '#f8f9fa'}}>
        <div className="container text-center fade-in-up">
          <h2 className="display-6 fw-bold mb-4">Ready to Take Off?</h2>
          <p className="lead mb-4">Join thousands of travelers who trust us with their journeys</p>
          <Link to="/register" className="btn btn-primary btn-lg px-5 btn-interactive">
            <i className="fas fa-plane-departure me-2"></i>Book Your Flight Now
          </Link>
        </div>
      </section>
    </div>
  );
};

const FAQAccordion = () => {
  const [activeIndex, setActiveIndex] = useState(null);

  const faqs = [
    {
      icon: 'fas fa-question-circle',
      question: 'How do I book a flight?',
      answer: 'Simply register or login, search for flights using our search filters, select your preferred flight, enter passenger details, and complete the payment process. You\'ll receive a booking confirmation immediately.'
    },
    {
      icon: 'fas fa-credit-card',
      question: 'What payment methods do you accept?',
      answer: 'We accept all major credit cards, debit cards, net banking, and UPI payments through our secure Razorpay payment gateway. All transactions are encrypted and secure.'
    },
    {
      icon: 'fas fa-times-circle',
      question: 'Can I cancel my booking?',
      answer: 'Yes, you can cancel your booking from the "My Bookings" section. Cancellation is allowed for confirmed bookings, and you\'ll receive a full refund. The refund will be processed to your original payment method.'
    },
    {
      icon: 'fas fa-users',
      question: 'Can I book for multiple passengers?',
      answer: 'Yes, you can book for up to 6 passengers in a single booking. You\'ll need to provide individual details for each passenger including name, age, gender, and seat preferences.'
    },
    {
      icon: 'fas fa-ticket-alt',
      question: 'How do I get my ticket?',
      answer: 'After successful payment, you can download your ticket from the "My Bookings" section. The ticket contains all flight details and passenger information needed for your journey.'
    },
    {
      icon: 'fas fa-chair',
      question: 'What\'s the difference between Economy and Business class?',
      answer: 'Economy class offers standard seating and services at affordable prices. Business class provides premium seating, enhanced comfort, and additional amenities at a higher price point.'
    }
  ];

  const toggleFAQ = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  return (
    <div className="faq-accordion">
      {faqs.map((faq, index) => (
        <div key={index} className="card border-0 shadow-sm mb-3">
          <div className="card-header bg-white border-0">
            <button 
              className="btn btn-link text-decoration-none w-100 text-start p-0 d-flex align-items-center"
              onClick={() => toggleFAQ(index)}
              style={{ color: '#333' }}
            >
              <i className={`${faq.icon} me-2 text-primary`}></i>
              <span className="fw-semibold">{faq.question}</span>
              <i className={`fas fa-chevron-${activeIndex === index ? 'up' : 'down'} ms-auto text-muted`}></i>
            </button>
          </div>
          {activeIndex === index && (
            <div className="card-body pt-0">
              <p className="mb-0 text-muted">{faq.answer}</p>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default Landing;