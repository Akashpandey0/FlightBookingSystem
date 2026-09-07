import React, { useState, useEffect } from 'react';
import { adminAPI, flightAPI } from '../services/api';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('flights');
  const [flights, setFlights] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [flightForm, setFlightForm] = useState({
    flightNumber: '',
    airline: '',
    source: '',
    sourceAirport: '',
    destination: '',
    destinationAirport: '',
    departureTime: '',
    arrivalTime: '',
    economyPrice: '',
    businessPrice: '',
    economySeats: '',
    businessSeats: '',
    price: '',
    totalSeats: ''
  });
  const [editingFlight, setEditingFlight] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (activeTab === 'flights') {
      loadFlights();
    } else if (activeTab === 'bookings') {
      loadBookings();
    }
  }, [activeTab]);

  const loadFlights = async () => {
    try {
      const response = await flightAPI.getAllFlights();
      setFlights(response.data);
    } catch (error) {
      setMessage('Error loading flights.');
    }
  };

  const loadBookings = async () => {
    try {
      const response = await adminAPI.getAllBookings();
      setBookings(response.data);
    } catch (error) {
      setMessage('Error loading bookings.');
    }
  };

  const handleFlightFormChange = (e) => {
    setFlightForm({
      ...flightForm,
      [e.target.name]: e.target.value
    });
  };

  const handleFlightSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const flightData = {
        ...flightForm,
        departureTime: flightForm.departureTime,
        arrivalTime: flightForm.arrivalTime,
        economyPrice: parseFloat(flightForm.economyPrice),
        businessPrice: parseFloat(flightForm.businessPrice),
        economySeats: parseInt(flightForm.economySeats),
        businessSeats: parseInt(flightForm.businessSeats),
        availableEconomySeats: parseInt(flightForm.economySeats),
        availableBusinessSeats: parseInt(flightForm.businessSeats),
        price: parseFloat(flightForm.economyPrice), // Legacy field
        totalSeats: parseInt(flightForm.economySeats) + parseInt(flightForm.businessSeats)
      };
      
      if (editingFlight) {
        await adminAPI.updateFlight(editingFlight.id, flightData);
        setMessage('Flight updated successfully!');
        setEditingFlight(null);
      } else {
        await adminAPI.addFlight(flightData);
        setMessage('Flight added successfully!');
      }
      
      setFlightForm({
        flightNumber: '',
        airline: '',
        source: '',
        sourceAirport: '',
        destination: '',
        destinationAirport: '',
        departureTime: '',
        arrivalTime: '',
        economyPrice: '',
        businessPrice: '',
        economySeats: '',
        businessSeats: '',
        price: '',
        totalSeats: ''
      });
      
      loadFlights();
    } catch (error) {
      console.error('Flight operation error:', error);
      const errorMsg = error.response?.data?.error || 
                      error.response?.data?.message || 
                      error.message || 
                      'Operation failed.';
      setMessage(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  const formatDateTimeForInput = (dateTimeString) => {
    const date = new Date(dateTimeString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };

  const handleEditFlight = (flight) => {
    setEditingFlight(flight);
    setFlightForm({
      flightNumber: flight.flightNumber,
      airline: flight.airline,
      source: flight.source,
      sourceAirport: flight.sourceAirport || '',
      destination: flight.destination,
      destinationAirport: flight.destinationAirport || '',
      departureTime: formatDateTimeForInput(flight.departureTime),
      arrivalTime: formatDateTimeForInput(flight.arrivalTime),
      economyPrice: flight.economyPrice || flight.price,
      businessPrice: flight.businessPrice || (flight.price * 2),
      economySeats: flight.economySeats || flight.totalSeats,
      businessSeats: flight.businessSeats || 0,
      price: flight.price,
      totalSeats: flight.totalSeats
    });
  };

  const handleDeleteFlight = async (id) => {
    if (window.confirm('Are you sure you want to delete this flight?')) {
      try {
        await adminAPI.deleteFlight(id);
        setMessage('Flight deleted successfully!');
        loadFlights();
      } catch (error) {
        setMessage('Error deleting flight.');
      }
    }
  };

  return (
    <div className="fade-in">
      <h2 className="slide-in-left"><i className="fas fa-cog me-2"></i>Admin Dashboard</h2>
      
      {message && (
        <div className={`alert ${message.includes('successfully') ? 'alert-success' : 'alert-danger'}`}>
          {message}
        </div>
      )}

      <ul className="nav nav-tabs mb-4">
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'flights' ? 'active' : ''}`}
            onClick={() => setActiveTab('flights')}
          >
            Manage Flights
          </button>
        </li>
        <li className="nav-item">
          <button 
            className={`nav-link ${activeTab === 'bookings' ? 'active' : ''}`}
            onClick={() => setActiveTab('bookings')}
          >
            View Bookings
          </button>
        </li>
      </ul>

      {activeTab === 'flights' && (
        <div>
          <div className="card mb-4 card-interactive">
            <div className="card-header" style={{background: 'linear-gradient(45deg, #28a745, #20c997)'}}>
              <h4 className="text-white mb-0 bounce">
                <i className="fas fa-plus-circle me-2"></i>
                {editingFlight ? 'Edit Flight' : 'Add New Flight'}
              </h4>
            </div>
            <div className="card-body">
              <form onSubmit={handleFlightSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Flight Number</label>
                    <input
                      type="text"
                      className="form-control"
                      name="flightNumber"
                      value={flightForm.flightNumber}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Airline</label>
                    <input
                      type="text"
                      className="form-control"
                      name="airline"
                      value={flightForm.airline}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-3 mb-3">
                    <label className="form-label">Source City</label>
                    <input
                      type="text"
                      className="form-control"
                      name="source"
                      value={flightForm.source}
                      onChange={handleFlightFormChange}
                      placeholder="e.g., Delhi"
                      required
                    />
                  </div>
                  <div className="col-md-3 mb-3">
                    <label className="form-label">Source Airport</label>
                    <input
                      type="text"
                      className="form-control"
                      name="sourceAirport"
                      value={flightForm.sourceAirport}
                      onChange={handleFlightFormChange}
                      placeholder="e.g., DEL - Indira Gandhi International"
                    />
                  </div>
                  <div className="col-md-3 mb-3">
                    <label className="form-label">Destination City</label>
                    <input
                      type="text"
                      className="form-control"
                      name="destination"
                      value={flightForm.destination}
                      onChange={handleFlightFormChange}
                      placeholder="e.g., Mumbai"
                      required
                    />
                  </div>
                  <div className="col-md-3 mb-3">
                    <label className="form-label">Destination Airport</label>
                    <input
                      type="text"
                      className="form-control"
                      name="destinationAirport"
                      value={flightForm.destinationAirport}
                      onChange={handleFlightFormChange}
                      placeholder="e.g., BOM - Chhatrapati Shivaji"
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Departure Time</label>
                    <input
                      type="datetime-local"
                      className="form-control"
                      name="departureTime"
                      value={flightForm.departureTime}
                      onChange={handleFlightFormChange}
                      min={new Date().toISOString().slice(0, 16)}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Arrival Time</label>
                    <input
                      type="datetime-local"
                      className="form-control"
                      name="arrivalTime"
                      value={flightForm.arrivalTime}
                      onChange={handleFlightFormChange}
                      min={new Date().toISOString().slice(0, 16)}
                      required
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Economy Price (₹)</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      name="economyPrice"
                      value={flightForm.economyPrice}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Business Price (₹)</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      name="businessPrice"
                      value={flightForm.businessPrice}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Economy Seats</label>
                    <input
                      type="number"
                      className="form-control"
                      name="economySeats"
                      value={flightForm.economySeats}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Business Seats</label>
                    <input
                      type="number"
                      className="form-control"
                      name="businessSeats"
                      value={flightForm.businessSeats}
                      onChange={handleFlightFormChange}
                      required
                    />
                  </div>
                </div>
                <button type="submit" className="btn btn-primary btn-interactive" disabled={loading}>
                  {loading ? (
                    <><div className="spinner-custom d-inline-block me-2" style={{width: '16px', height: '16px'}}></div>Saving...</>
                  ) : (
                    <><i className="fas fa-save me-2"></i>{editingFlight ? 'Update Flight' : 'Add Flight'}</>
                  )}
                </button>
                {editingFlight && (
                  <button 
                    type="button" 
                    className="btn btn-secondary ms-2"
                    onClick={() => {
                      setEditingFlight(null);
                      setFlightForm({
                        flightNumber: '',
                        airline: '',
                        source: '',
                        sourceAirport: '',
                        destination: '',
                        destinationAirport: '',
                        departureTime: '',
                        arrivalTime: '',
                        economyPrice: '',
                        businessPrice: '',
                        economySeats: '',
                        businessSeats: '',
                        price: '',
                        totalSeats: ''
                      });
                    }}
                  >
                    Cancel
                  </button>
                )}
              </form>
            </div>
          </div>

          <div className="card">
            <div className="card-header">
              <h4>All Flights</h4>
            </div>
            <div className="card-body">
              <div className="table-responsive">
                <table className="table table-striped">
                  <thead>
                    <tr>
                      <th>Flight Number</th>
                      <th>Airline</th>
                      <th>Route</th>
                      <th>Departure</th>
                      <th>Economy Price</th>
                      <th>Business Price</th>
                      <th>Available Seats</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {flights.map(flight => (
                      <tr key={flight.id}>
                        <td>{flight.flightNumber}</td>
                        <td>{flight.airline}</td>
                        <td>{flight.source} → {flight.destination}</td>
                        <td>{new Date(flight.departureTime).toLocaleString()}</td>
                        <td>₹{flight.economyPrice || flight.price}</td>
                        <td>₹{flight.businessPrice || (flight.price * 2)}</td>
                        <td>
                          E: {flight.availableEconomySeats || flight.availableSeats}<br/>
                          B: {flight.availableBusinessSeats || 0}
                        </td>
                        <td>
                          <button 
                            className="btn btn-sm btn-primary me-2"
                            onClick={() => handleEditFlight(flight)}
                          >
                            Edit
                          </button>
                          <button 
                            className="btn btn-sm btn-danger"
                            onClick={() => handleDeleteFlight(flight.id)}
                          >
                            Delete
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'bookings' && (
        <div className="card">
          <div className="card-header">
            <h4>All Bookings</h4>
          </div>
          <div className="card-body">
            <div className="table-responsive">
              <table className="table table-striped">
                <thead>
                  <tr>
                    <th>Booking Reference</th>
                    <th>Customer</th>
                    <th>Flight</th>
                    <th>Passengers</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Booking Date</th>
                  </tr>
                </thead>
                <tbody>
                  {bookings.map(booking => (
                    <tr key={booking.id}>
                      <td>{booking.bookingReference}</td>
                      <td>{booking.user.firstName} {booking.user.lastName}</td>
                      <td>{booking.flight.airline} - {booking.flight.flightNumber}</td>
                      <td>{booking.numberOfPassengers}</td>
                      <td>₹{booking.totalAmount}</td>
                      <td>
                        <span className={`badge ${
                          booking.status === 'CONFIRMED' ? 'bg-success' : 
                          booking.status === 'CANCELLED' ? 'bg-danger' : 'bg-secondary'
                        }`}>
                          {booking.status}
                        </span>
                      </td>
                      <td>{new Date(booking.bookingDate).toLocaleString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;