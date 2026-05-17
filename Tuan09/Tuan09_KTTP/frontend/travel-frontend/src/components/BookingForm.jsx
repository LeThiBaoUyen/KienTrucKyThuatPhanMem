// components/BookingForm.jsx
import { useState } from "react";
import { bookTour } from "../services/api";

export default function BookingForm({ user, tour }) {
  const [result, setResult] = useState(null);

  const handleBooking = async () => {
    try {
      const res = await bookTour({
        userId: user.id,
        tourId: tour.id,
      });

      setResult(res.data);
    } catch (err) {
      setResult({
        status: "ERROR",
        message: "Cannot connect to server",
      });
    }
  };

  return (
    <div>
      <h2>Booking</h2>
      <p>User: {user.username}</p>
      <p>Tour: {tour.name}</p>

      <button onClick={handleBooking}>Confirm Booking</button>

      {result && (
        <div>
          <h3>Result:</h3>
          <p>Status: {result.status}</p>
          <p>Message: {result.message}</p>
          <p>Booking ID: {result.bookingId}</p>
        </div>
      )}
    </div>
  );
}