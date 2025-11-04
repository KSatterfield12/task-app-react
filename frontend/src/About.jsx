import React from 'react';
import './styles/about.css';

const About = () => {
  return (
    <div className="about-page">
      <h2>About the Household Tasklist Application</h2>
      <p>
        The Household Tasklist Application is a secure, role-based task management system designed to help families and shared households stay organized. Built with <strong>React</strong> on the frontend and <strong>Spring Boot</strong> on the backend, it offers a clean, responsive interface for managing daily responsibilities.
      </p>

      <h3>Key Features</h3>
      <ul>
        <li>Create, update, complete, and delete tasks</li>
        <li>Role-based access control for Admins and Users</li>
        <li>JWT authentication for secure login and session management</li>
        <li>Responsive design with accessibility considerations</li>
        <li>Environment-based configuration for flexible deployment</li>
      </ul>

      <h3>Architecture Overview</h3>
      <p>
        The backend is structured with a service/repository architecture and includes custom security filters, handlers, and token utilities. The frontend communicates with the REST API using secure tokens and provides intuitive form toggles, smooth transitions, and real-time feedback.
      </p>

      <h3>Technology Stack</h3>
      <ul>
        <li><strong>Frontend:</strong> React (Vite), CSS Modules</li>
        <li><strong>Backend:</strong> Spring Boot, Java</li>
        <li><strong>Database:</strong> PostgreSQL (for testing)</li>
        <li><strong>Security:</strong> JWT, Spring Security</li>
        <li><strong>Testing:</strong> JUnit 5, Mockito</li>
      </ul>

      <p>
        Whether you're managing chores, errands, or shared responsibilities, this application provides a reliable and user-friendly way to stay on top of your household tasks.
      </p>
    </div>
  );
};

export default About;