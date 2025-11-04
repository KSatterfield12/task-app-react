import { useState } from "react";
import "./styles/actionForms.css";

export default function UpdateTaskForm({ token, onTaskUpdated, onCloseForm }) {
  const [taskId, setTaskId] = useState("");
  const [taskData, setTaskData] = useState(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const fetchTaskById = async () => {
    setError("");
    setSuccess("");

    try {
      const BASE_URL = import.meta.env.VITE_API_URL;

      const response = await fetch(`${BASE_URL}/api/tasks/${taskId}`, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Task not found.");
      }

      const data = await response.json();
      setTaskData(data);
      setTitle(data.title || "");
      setDescription(data.description || "");
    } catch (err) {
      setError(err.message);
      setTaskData(null);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!taskId.trim()) {
      setError("Task ID is required.");
      return;
    }

    if (!title.trim() && !description.trim()) {
      setError("You must update either the title or the description.");
      return;
    }

    try {
      const BASE_URL = import.meta.env.VITE_API_URL;

      const response = await fetch(`${BASE_URL}/api/tasks/${taskId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          id: taskId,
          ...(title.trim() && { title }),
          ...(description.trim() && { description }),
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to update task.");
      }

      const updatedTask = await response.json();
      setSuccess(`Task "${updatedTask.title}" updated successfully.`);

      // Clear form and hide it
      setTaskId("");
      setTitle("");
      setDescription("");
      setTaskData(null);

      onTaskUpdated?.();
      onCloseForm?.();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <h3>Update Task</h3>

      <label>
        Enter Task ID:
        <input
          type="text"
          value={taskId}
          onChange={(e) => setTaskId(e.target.value)}
        />
      </label>

      <button type="button" onClick={fetchTaskById}>
        Load Task
      </button>

      {taskData && (
        <>
          <p><strong>Current Title:</strong> {taskData.title}</p>
          <p><strong>Current Description:</strong> {taskData.description}</p>

          <label>
            New Title:
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Leave blank to keep current title"
            />
          </label>

          <label>
            New Description:
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Leave blank to keep current description"
            />
          </label>

          <button type="submit">Submit Changes</button>
        </>
      )}

      {error && <p className="error-message">{error}</p>}
      {success && <p className="success-message">{success}</p>}
    </form>
  );
}