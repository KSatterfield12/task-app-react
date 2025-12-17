import React from 'react';
import './styles/about.css';

const About = () => {
  return (
    <div className="content-page">
      <div className="about-page">
        <h2>About the Household Tasklist App</h2>
        <p>
          A secure, role-based system built with <strong>React</strong> and <strong>Spring Boot</strong> to help households stay organized. It offers a clean, responsive interface for managing daily responsibilities.
        </p>

        <h3>Key Features</h3>
        <ul>
          <li>Task creation and completion</li>
          <li>Role-based access control</li>
          <li>Secure JWT authentication</li>
        </ul>

        <h3>Technology Stack</h3>
        <ul>
          <li><strong>Frontend:</strong> React (Vite)</li>
          <li><strong>Backend:</strong> Spring Boot</li>
          <li><strong>Database:</strong> PostgreSQL</li>
        </ul>

        <p>
          Designed with accessibility, responsive UI, and flexible deployment, the app makes household task management simple and reliable.
        </p>
      </div>
    </div>
  );
};

export default About;