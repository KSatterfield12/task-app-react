import { useState } from "react";
import "../styles/registerForm.css";

export default function RegisterForm({ onRegister }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      setSuccess("");
      return;
    }

    fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username,
        password,
        confirmPassword,
        role: "user"
      })
    })
      .then(res => {
        if (res.ok) {
          setSuccess("Account created successfully! \n You can now log in.");
          setError("");
          onRegister();
        } else if (res.status === 409) {
          setError("That username is already taken. \n Please choose a different one.");
          setSuccess("");
        } else {
          setError("Registration failed. \n Please try again.");
          setSuccess("");
        }
      })
      .catch(() => {
        setError("Server error. \n Please try again later.");
        setSuccess("");
      });
  };

  return (
    <div className="login-page">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Create Account</h2>

        {success && (
          <p className="success-message">
            {success}
          </p>
        )}

        {error && (
          <p className="error-message">
            {error.split('\n').map((line, index) => (
              <span key={index}>
                {line}
                <br />
              </span>
            ))}
          </p>
        )}

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={e => setConfirmPassword(e.target.value)}
          required
        />

        <button type="submit">Register</button>
      </form>

      <button
        type="button"
        onClick={onRegister}
        className="back-to-login"
      >
        Back to Login
      </button>

      <footer className="login-footer">
        <p>&copy; 2025 Kami Satterfield. All rights reserved.</p>
      </footer>
    </div>
  );
}