import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { createTask } from "./services/taskService";
import "./styles/actionForms.css";

export default function TaskForm({ token, onTaskCreated }) {
  const [formData, setFormData] = useState({ title: "", description: "" });
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.title.trim() || !formData.description.trim()) {
      alert("Both title and description are required.");
      return;
    }

    try {
      const newTask = await createTask(formData, token);
      onTaskCreated?.(newTask);
      setFormData({ title: "", description: "" }); // Reset form after creation
    } catch (err) {
      console.error("Error creating task:", err);
      alert("Failed to create task.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="task-form">
      <h3>New Task</h3>

      <div className="form-field">
        <label htmlFor="title">Title:</label>
        <input
          type="text"
          id="title"
          name="title"
          value={formData.title}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-field">
        <label htmlFor="description">Description:</label>
        <textarea
          id="description"
          name="description"
          value={formData.description}
          onChange={handleChange}
          rows={4}
          required
        />
      </div>

      <button type="submit">Create Task</button>
    </form>
  );
}