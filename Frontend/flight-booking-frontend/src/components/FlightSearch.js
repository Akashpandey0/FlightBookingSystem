import React, { useState, useEffect } from 'react';
import { flightAPI, bookingAPI, paymentAPI } from '../services/api';
import PassengerForm from './PassengerForm';

const FlightSearch = () => {
  const [searchData, setSearchData] = useState({
    source: '',
    destination: '',
    departureDate: '',
    airline: '',
    passengers: 1
  });
  const [flights, setFlights] = useState([]);
  const [loading, setLoading] = useState(false);
  const [bookingLoading, setBookingLoading] = useState(null);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState(''); // 'success' or 'error'
  const [showPassengerForm, setShowPassengerForm] = useState(false);
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [selectedSeatClass, setSelectedSeatClass] = useState('ECONOMY');

  useEffect(() => {
    loadAllFlights();
  }, []);

  const loadAllFlights = async () => {
    try {
      const response = await flightAPI.getAllFlights();
      setFlights(response.data.filter(flight => flight.status !== 'COMPLETED'));
    } catch (error) {
      console.error('Error loading flights:', error);
    }
  };

  const handleChange = (e) => {
    setSearchData({
      ...searchData,
      [e.target.name]: e.target.value
    });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    setMessageType('');

    try {
      const response = await flightAPI.searchFlights(searchData);
      setFlights(response.data);
      if (response.data.length === 0) {
        setMessage('No flights found for your search criteria.');
        setMessageType('error');
      }
    } catch (error) {
      setMessage('Error searching flights. Please try again.');
      setMessageType('error');
    } finally {
      setLoading(false);
    }
  };

  const handleBookingClick = (flightId, seatClass = 'ECONOMY') => {
    const flight = flights.find(f => f.id === flightId);
    setSelectedFlight(flight);
    setSelectedSeatClass(seatClass);
    setShowPassengerForm(true);
    setMessage('');
    setMessageType('');
  };

  const handlePassengerFormSubmit = async (passengerData) => {
    setBookingLoading(selectedFlight.id);

    try {
      const bookingData = {
        flightId: selectedFlight.id,
        passengers: passengerData.passengers,
        seatClass: selectedSeatClass,

        contactPhone: passengerData.contactPhone
      };

      const response = await bookingAPI.bookFlight(bookingData);
      const booking = response.data;
      
      setShowPassengerForm(false);
      
      // Initialize Razorpay payment
      const options = {
        key: 'rzp_test_i08Pxd8COI3D2q', // Your actual Razorpay key
        amount: booking.totalAmount * 100, // Amount in paise
        currency: 'INR',
        name: 'Flight Booking System',
        description: `Flight booking for ${booking.bookingReference}`,
        order_id: booking.razorpayOrderId,
        handler: async function (response) {
          try {
            await paymentAPI.verifyPayment({
              orderId: response.razorpay_order_id,
              paymentId: response.razorpay_payment_id,
              signature: response.razorpay_signature,
              bookingReference: booking.bookingReference
            });
            setMessage('Payment successful! Flight booked successfully.');
            setMessageType('success');
            loadAllFlights();
          } catch (error) {
            setMessage('Payment verification failed!');
            setMessageType('error');
          }
        },
        modal: {
          ondismiss: function() {
            setMessage('Payment cancelled by user.');
            setMessageType('error');
          }
        },
        prefill: {
          name: passengerData.passengers[0]?.name || 'Customer Name',
          email: 'customer@gmail.com',
          contact: passengerData.contactPhone || '8887724405'
        },
        theme: {
          color: '#667eea'
        }
      };
      
      if (window.Razorpay) {
        const rzp = new window.Razorpay(options);
        rzp.open();
      } else {
        setMessage('Payment gateway not available. Please try again later.');
        setMessageType('error');
      }
      
    } catch (error) {
      setMessage(error.response?.data?.error || 'Booking failed. Please try again.');
      setMessageType('error');
      setShowPassengerForm(false);
    } finally {
      setBookingLoading(null);
    }
  };

  const handlePassengerFormCancel = () => {
    setShowPassengerForm(false);
    setSelectedFlight(null);
    setBookingLoading(null);
  };

  return (
    <div className="fade-in">
      <h2 className="slide-in-left">Search Flights</h2>
      
      {message && (
        <div className={`alert ${messageType === 'success' ? 'alert-success' : 'alert-danger'}`}>
          <i className={`fas ${messageType === 'success' ? 'fa-check-circle' : 'fa-exclamation-triangle'} me-2`}></i>
          {message}
        </div>
      )}

      <div className="card mb-4 shadow-sm card-interactive">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0 bounce"><i className="fas fa-search me-2"></i>Search Flights</h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleSearch}>
            <div className="row">
              <div className="col-md-3 mb-3">
                <label className="form-label">From</label>
                <input
                  type="text"
                  className="form-control"
                  name="source"
                  value={searchData.source}
                  onChange={handleChange}
                  placeholder="Source city"
                />
              </div>
              <div className="col-md-3 mb-3">
                <label className="form-label">To</label>
                <input
                  type="text"
                  className="form-control"
                  name="destination"
                  value={searchData.destination}
                  onChange={handleChange}
                  placeholder="Destination city"
                />
              </div>
              <div className="col-md-2 mb-3">
                <label className="form-label">Date</label>
                <input
                  type="date"
                  className="form-control"
                  name="departureDate"
                  value={searchData.departureDate}
                  onChange={handleChange}
                  min={new Date().toISOString().split('T')[0]}
                />
              </div>
              <div className="col-md-2 mb-3">
                <label className="form-label">Passengers</label>
                <select
                  className="form-control"
                  name="passengers"
                  value={searchData.passengers}
                  onChange={handleChange}
                >
                  {[1,2,3,4,5,6].map(num => (
                    <option key={num} value={num}>{num}</option>
                  ))}
                </select>
              </div>
              <div className="col-md-2 mb-3">
                <label className="form-label">&nbsp;</label>
                <button type="submit" className="btn btn-primary w-100 btn-lg btn-interactive" disabled={loading}>
                  {loading ? (
                    <><div className="spinner-custom d-inline-block me-2" style={{width: '20px', height: '20px'}}></div>Searching...</>
                  ) : (
                    <><i className="fas fa-search me-2"></i>Search Flights</>
                  )}
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      <div className="row">
        {flights.filter(flight => flight.status !== 'COMPLETED').map((flight, index) => (
          <div key={flight.id} className="col-md-6 mb-4 fade-in" style={{animationDelay: `${index * 0.1}s`}}>
            <div className="card h-100 shadow-sm border-0 card-interactive">
              <div className="card-header bg-gradient" style={{background: 'linear-gradient(45deg, #007bff, #0056b3)'}}>
                <h5 className="card-title text-white mb-0">
                  <i className="fas fa-plane me-2"></i>
                  {flight.airline} - {flight.flightNumber}
                </h5>
              </div>
              <div className="card-body">
                <div className="row mb-3">
                  <div className="col-6 text-center">
                    <div className="text-muted small">FROM</div>
                    <div className="h6 text-primary">{flight.source}</div>
                    {flight.sourceAirport && (
                      <div className="small text-muted">{flight.sourceAirport}</div>
                    )}
                  </div>
                  <div className="col-6 text-center">
                    <div className="text-muted small">TO</div>
                    <div className="h6 text-primary">{flight.destination}</div>
                    {flight.destinationAirport && (
                      <div className="small text-muted">{flight.destinationAirport}</div>
                    )}
                  </div>
                </div>
                <div className="row mb-3">
                  <div className="col-6">
                    <small className="text-muted"><i className="fas fa-plane-departure me-1"></i>Departure</small>
                    <div className="small">{new Date(flight.departureTime).toLocaleString()}</div>
                  </div>
                  <div className="col-6">
                    <small className="text-muted"><i className="fas fa-plane-arrival me-1"></i>Arrival</small>
                    <div className="small">{new Date(flight.arrivalTime).toLocaleString()}</div>
                  </div>
                </div>
                <div className="row mb-3">
                  <div className="col-6 text-center">
                    <div className="text-muted small">ECONOMY</div>
                    <div className="h6 text-success">₹{flight.economyPrice || flight.price}</div>
                    <div className="small text-muted">{flight.availableEconomySeats || flight.availableSeats} seats</div>
                  </div>
                  <div className="col-6 text-center">
                    <div className="text-muted small">BUSINESS</div>
                    <div className="h6 text-success">₹{flight.businessPrice || (flight.price * 2)}</div>
                    <div className="small text-muted">{flight.availableBusinessSeats || 0} seats</div>
                  </div>
                </div>
                <div className="d-grid gap-2">
                  <button 
                    className="btn btn-success btn-interactive"
                    onClick={() => handleBookingClick(flight.id, 'ECONOMY')}
                    disabled={(flight.availableEconomySeats || flight.availableSeats) < searchData.passengers}
                  >
                    <i className="fas fa-plane me-2"></i>Book Economy
                  </button>
                  <button 
                    className="btn btn-warning btn-interactive"
                    onClick={() => handleBookingClick(flight.id, 'BUSINESS')}
                    disabled={(flight.availableBusinessSeats || 0) < searchData.passengers}
                  >
                    <i className="fas fa-crown me-2"></i>Book Business
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
      
      {showPassengerForm && (
        <PassengerForm
          numberOfPassengers={searchData.passengers}
          onSubmit={handlePassengerFormSubmit}
          onCancel={handlePassengerFormCancel}
          loading={bookingLoading !== null}
        />
      )}
    </div>
  );
};

export default FlightSearch;