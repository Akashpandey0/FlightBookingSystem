import React, { useState, useEffect } from 'react';
import { bookingAPI } from '../services/api';

const BookingHistory = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [cancelLoading, setCancelLoading] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadBookings();
  }, []);

  const loadBookings = async () => {
    try {
      const response = await bookingAPI.getMyBookings();
      setBookings(response.data);
    } catch (error) {
      setMessage('Error loading bookings.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (bookingReference) => {
    setCancelLoading(bookingReference);
    setMessage('');

    try {
      const response = await bookingAPI.cancelBooking(bookingReference);
      const refundAmount = response.data.refundAmount;
      setMessage(`Booking cancelled successfully! Refund amount: ₹${refundAmount}`);
      loadBookings(); // Refresh bookings
    } catch (error) {
      setMessage(error.response?.data?.error || 'Cancellation failed.');
    } finally {
      setCancelLoading(null);
    }
  };

  const handleDownloadTicket = async (bookingReference) => {
    try {
      const response = await bookingAPI.downloadTicket(bookingReference);
      const blob = new Blob([response.data], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `ticket-${bookingReference}.txt`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      setMessage('Failed to download ticket.');
    }
  };

  if (loading) {
    return <div className="text-center">Loading bookings...</div>;
  }

  return (
    <div className="fade-in">
      <h2 className="slide-in-left">My Bookings</h2>
      
      {message && (
        <div className={`alert ${message.includes('successfully') ? 'alert-success' : 'alert-danger'}`}>
          {message}
        </div>
      )}

      {bookings.length === 0 ? (
        <div className="alert alert-info">No bookings found.</div>
      ) : (
        <>
          <div className="row">
            {bookings.filter(booking => booking.status !== 'CANCELLED' || booking.paymentStatus !== 'FAILED').map((booking, index) => (
              <div key={booking.id} className="col-md-6 mb-4 fade-in" style={{animationDelay: `${index * 0.1}s`}}>
                <div className="card h-100 shadow-sm border-0 card-interactive">
                  <div className="card-header" style={{background: booking.status === 'CONFIRMED' ? 'linear-gradient(45deg, #28a745, #20c997)' : 'linear-gradient(45deg, #dc3545, #fd7e14)'}}>
                    <h6 className="text-white mb-0">
                      <i className="fas fa-ticket-alt me-2"></i>
                      Booking #{booking.bookingReference}
                    </h6>
                  </div>
                  <div className="card-body">
                    <div className="d-flex justify-content-between align-items-center mb-3">
                      <span className={`badge fs-6 ${
                        booking.status === 'CONFIRMED' ? 'bg-success' : 
                        booking.status === 'CANCELLED' ? 'bg-danger' : 'bg-secondary'
                      }`}>
                        <i className={`fas ${booking.status === 'CONFIRMED' ? 'fa-check' : 'fa-times'} me-1`}></i>
                        {booking.status}
                      </span>
                    </div>
                    <p className="card-text">
                      <strong>Flight:</strong> {booking.flight.airline} - {booking.flight.flightNumber}<br/>
                      <strong>Route:</strong> {booking.flight.source} → {booking.flight.destination}<br/>
                      <strong>Departure:</strong> {new Date(booking.flight.departureTime).toLocaleString()}<br/>
                      <strong>Passengers:</strong> {booking.numberOfPassengers}<br/>
                      {booking.passengerDetails && (
                        <><strong>Passenger Details:</strong><br/>
                        <small className="text-muted">{booking.passengerDetails}</small><br/>
                        </>
                      )}
                      <strong>Total Amount:</strong> ₹{booking.totalAmount}<br/>
                      {booking.refundAmount && (
                        <><strong>Refund Amount:</strong> <span className="text-success">₹{booking.refundAmount}</span><br/></>
                      )}
                      <strong>Booking Date:</strong> {new Date(booking.bookingDate).toLocaleString()}<br/>
                      {booking.refundDate && (
                        <><strong>Refund Date:</strong> {new Date(booking.refundDate).toLocaleString()}<br/></>
                      )}
                      <strong>Payment Status:</strong> 
                      <span className={`badge ms-1 ${
                        booking.paymentStatus === 'PAID' ? 'bg-success' : 
                        booking.paymentStatus === 'REFUNDED' ? 'bg-info' : 'bg-warning'
                      }`}>
                        {booking.paymentStatus}
                      </span>
                    </p>
                    {booking.status === 'CONFIRMED' && (
                      <div className="d-grid gap-2">
                        <button 
                          className="btn btn-success btn-interactive"
                          onClick={() => handleDownloadTicket(booking.bookingReference)}
                        >
                          <i className="fas fa-download me-2"></i>Download Ticket
                        </button>
                        <button 
                          className="btn btn-outline-danger btn-interactive"
                          onClick={() => handleCancel(booking.bookingReference)}
                          disabled={cancelLoading === booking.bookingReference}
                        >
                          {cancelLoading === booking.bookingReference ? (
                            <><div className="spinner-custom d-inline-block me-2" style={{width: '16px', height: '16px'}}></div>Cancelling...</>
                          ) : (
                            <><i className="fas fa-times me-2"></i>Cancel Booking</>
                          )}
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
          {bookings.filter(booking => booking.status === 'CANCELLED' && booking.paymentStatus === 'FAILED').length > 0 && (
            <div className="alert alert-info mt-3">
              <i className="fas fa-info-circle me-2"></i>
              Some bookings with failed payments are not displayed.
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default BookingHistory;