import { bookTour } from "../services/api";

export default function Booking({ user, tour }) {

  const handleBook = async () => {
    const res = await bookTour({
      userId: user.id,
      tourId: tour.id
    });

    alert(res.data.message);
  };

  return (
    <div>
      <h3>{tour.name}</h3>
      <button onClick={handleBook}>Confirm</button>
    </div>
  );
}