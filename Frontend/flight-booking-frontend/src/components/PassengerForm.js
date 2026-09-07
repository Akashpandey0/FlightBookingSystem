import React, { useState } from 'react';

const PassengerForm = ({ numberOfPassengers, onSubmit, onCancel, loading }) => {
  const [passengers, setPassengers] = useState(
    Array.from({ length: numberOfPassengers }, () => ({
      name: '',
      age: '',
      gender: 'Male',
      seatPreference: 'Window'
    }))
  );

  const [contactPhone, setContactPhone] = useState('');

  const handlePassengerChange = (index, field, value) => {
    const updatedPassengers = [...passengers];
    updatedPassengers[index][field] = value;
    setPassengers(updatedPassengers);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validate all passengers have required fields
    const isValid = passengers.every(p => p.name.trim() && p.age.trim() && p.gender);
    if (!isValid) {
      alert('Please fill in all required passenger details');
      return;
    }

    onSubmit({
      passengers,
      contactPhone
    });
  };

  return (
    <div className="modal fade show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}>
      <div className="modal-dialog modal-lg">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              <i className="fas fa-users me-2"></i>
              Passenger Details ({numberOfPassengers} passenger{numberOfPassengers > 1 ? 's' : ''})
            </h5>
            <button type="button" className="btn-close" onClick={onCancel}></button>
          </div>
          <form onSubmit={handleSubmit}>
            <div className="modal-body">
              {passengers.map((passenger, index) => (
                <div key={index} className="card mb-3">
                  <div className="card-header">
                    <h6 className="mb-0">Passenger {index + 1}</h6>
                  </div>
                  <div className="card-body">
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label className="form-label">Full Name *</label>
                        <input
                          type="text"
                          className="form-control"
                          value={passenger.name}
                          onChange={(e) => handlePassengerChange(index, 'name', e.target.value)}
                          placeholder="Enter full name"
                          required
                        />
                      </div>
                      <div className="col-md-3 mb-3">
                        <label className="form-label">Age *</label>
                        <input
                          type="number"
                          className="form-control"
                          value={passenger.age}
                          onChange={(e) => handlePassengerChange(index, 'age', e.target.value)}
                          min="1"
                          max="120"
                          required
                        />
                      </div>
                      <div className="col-md-3 mb-3">
                        <label className="form-label">Gender *</label>
                        <select
                          className="form-control"
                          value={passenger.gender}
                          onChange={(e) => handlePassengerChange(index, 'gender', e.target.value)}
                          required
                        >
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                          <option value="Other">Other</option>
                        </select>
                      </div>
                      <div className="col-md-6 mb-3">
                        <label className="form-label">Seat Preference</label>
                        <select
                          className="form-control"
                          value={passenger.seatPreference}
                          onChange={(e) => handlePassengerChange(index, 'seatPreference', e.target.value)}
                        >
                          <option value="Window">Window</option>
                          <option value="Aisle">Aisle</option>
                          <option value="Middle">Middle</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
              
              <div className="card">
                <div className="card-header">
                  <h6 className="mb-0">Contact Information</h6>
                </div>
                <div className="card-body">
                  <div className="row">
                    <div className="col-md-12 mb-3">
                      <label className="form-label">Contact Phone</label>
                      <input
                        type="tel"
                        className="form-control"
                        value={contactPhone}
                        onChange={(e) => setContactPhone(e.target.value)}
                        placeholder="+91 9876543210"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="modal-footer">
              <button type="button" className="btn btn-secondary" onClick={onCancel}>
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? (
                  <>
                    <div className="spinner-border spinner-border-sm me-2"></div>
                    Processing...
                  </>
                ) : (
                  <>
                    <i className="fas fa-credit-card me-2"></i>
                    Proceed to Payment
                  </>
                )}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PassengerForm;