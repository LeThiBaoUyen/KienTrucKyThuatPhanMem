// src/App.jsx
import { useState } from "react";
import Login from "./components/Login";
import TourList from "./components/TourList";
import BookingForm from "./components/BookingForm";

function App() {
  const [user, setUser] = useState(null);
  const [selectedTour, setSelectedTour] = useState(null);

  if (!user) {
    return <Login onLogin={setUser} />;
  }

  return (
    <div>
      <h2>Welcome {user.username}</h2>

      {!selectedTour ? (
        <TourList onSelect={setSelectedTour} />
      ) : (
        <BookingForm user={user} tour={selectedTour} />
      )}
    </div>
  );
}

export default App;