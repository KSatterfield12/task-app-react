import { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import LoginForm from "./components/LoginForm";
import Tasks from "./Tasks";
import About from './About';
import "./styles/index.css";
import logoIcon from "./assets/taskIcon.png";
import { fetchTasks } from "./services/taskService";
import useAutoLogout from "./services/useAutoLogout";
import { logout } from "./services/authService";

function getUserRoleFromToken(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const authClaim = payload.auth || "";
    if (authClaim.includes("ROLE_ADMIN")) return "ADMIN";
    return "USER";
  } catch (err) {
    console.error("Failed to decode token:", err);
    return null;
  }
}

export default function App() {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [tasks, setTasks] = useState([]);
  const [search, setSearch] = useState("");
  const [userRole, setUserRole] = useState(null);
  const [showWarning, setShowWarning] = useState(false);

  useAutoLogout(
    () => {
      localStorage.removeItem("token");
      setToken(null);
    },
    () => {
      setShowWarning(true);
    }
  );

  const refreshTasks = async () => {
    try {
      const data = await fetchTasks();
      setTasks(data);
    } catch (error) {
      console.error("Failed to fetch tasks:", error);
    }
  };

  useEffect(() => {
    if (token) {
      const role = getUserRoleFromToken(token);
      setUserRole(role);
      refreshTasks();
    }
  }, [token]);

  const handleTaskCreated = (newTask) => {
    setTasks(prev => [...prev, newTask]);
  };

  const handleTaskDeleted = (id) => {
    setTasks(prev => prev.filter(task => task.id !== id));
  };

  const toggleTheme = () => {
    document.body.classList.toggle("dark-mode");
  };

  if (!token) {
    return (
      <LoginForm
        onLogin={(t) => {
          localStorage.setItem("token", t);
          setToken(t);
          setShowWarning(false);
        }}
      />
    );
  }


  return (
    <>
      {showWarning && (
        <div className="inactivity-modal">
          <div className="modal-content">
            <p>Are you still here?</p>
            <button onClick={() => setShowWarning(false)}>
              Yes, I'm here
            </button>
          </div>
        </div>
      )}

    <Router>
      <div className="container">
        <header>
          <div className="header-left">
            <img src={logoIcon} alt="Logo" className="header-logo" />
            <nav>
              <Link to="/">Home</Link>
              <Link to="/tasks">Tasks</Link>
              <Link to="/about">About</Link>
            </nav>
          </div>

          <div className="header-right">
            <button className="theme-toggle-button" onClick={toggleTheme}>
              Change Theme
            </button>
            <button
              className="logout-button"
              onClick={() => {
                localStorage.removeItem("token");
                setToken(null);
                document.body.classList.remove("dark-mode");
              }}
            >
              Logout
            </button>
          </div>
        </header>

        <Routes>
          <Route
            path="/"
            element={
              <main>
                <section className="home-intro">
                  <h1 className="home-title">Welcome to the Task List App</h1>
                  <div className="home-divider"></div>
                  <h3>Use the navigation above to manage your tasks and explore more!</h3>

                  <div className="chore-icons">
                    <img src="/dishes.png" alt="Dishes" />
                    <img src="/laundry.png" alt="Laundry" />
                    <img src="/vacuum.png" alt="Vacuuming" />
                    <img src="/trash.png" alt="Trash" />
                    <img src="/mopping.png" alt="Mopping" />
                    <img src="/broom.png" alt="Sweeping" />
                  </div>
                </section>
              </main>
            }
          />
          <Route
            path="/tasks"
            element={
              <Tasks
                tasks={tasks}
                onDelete={handleTaskDeleted}
                onTaskCreated={handleTaskCreated}
                userRole={userRole}
                token={token}
                search={search}
                setSearch={setSearch}
                toggleTheme={toggleTheme}
                refreshTasks={refreshTasks}
              />
            }
          />

          <Route path="/about" element={<About />} />

        </Routes>

        <footer>
          <p>&copy; 2025 Kami Satterfield. All rights reserved.</p>
        </footer>
      </div>
    </Router>
    </>
  );
}