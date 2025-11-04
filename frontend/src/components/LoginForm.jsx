import { useState } from "react";
import { login } from "../services/authService";
import RegisterForm from "./RegisterForm";
import "../styles/loginForm.css";

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [showRegister, setShowRegister] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const { token } = await login(username, password);
      onLogin(token);
    } catch (err) {
      setError("Username or password is incorrect.\n Please try again.");
    }
  };

  if (showRegister) {
    return (
      <RegisterForm onRegister={() => setShowRegister(false)} />
    );
  }

  return (
    <div className="login-page">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Task List App Login</h2>
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
        <button type="submit">Login</button>
      </form>

      <div className="register-prompt">
        <p>New here?</p>
        <button className="register-button" onClick={() => setShowRegister(true)}>
          Create Account
        </button>
      </div>

      <footer className="login-footer">
        <p>&copy; 2025 Kami Satterfield. All rights reserved.</p>
      </footer>
    </div>
  );
  }