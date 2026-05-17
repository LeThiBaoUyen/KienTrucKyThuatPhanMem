// components/Login.jsx
import { useState } from "react";

export default function Login({ onLogin }) {
  const [userId, setUserId] = useState("");

  const handleLogin = () => {
    onLogin({
      id: userId,
      username: "User " + userId,
    });
  };

  return (
    <div>
      <h2>Login</h2>
      <input
        placeholder="Enter user ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />
      <button onClick={handleLogin}>Login</button>
    </div>
  );
}