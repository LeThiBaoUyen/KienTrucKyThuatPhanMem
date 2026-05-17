import { useEffect, useState } from "react";
import { getTours } from "../services/api";

export default function TourList({ onSelect }) {
  const [tours, setTours] = useState([]);

  useEffect(() => {
    getTours()
      .then(res => setTours(res.data))
      .catch(() => alert("Cannot load tours"));
  }, []);

  return (
    <div>
      <h2>Tour List</h2>

      {tours.map(t => (
        <div key={t.id}>
          <p>{t.name} - ${t.price}</p>
          <button onClick={() => onSelect(t)}>Book</button>
        </div>
      ))}
    </div>
  );
}