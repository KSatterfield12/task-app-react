const backendUrl = "http://localhost:8080";

// Fetch all tasks
export async function fetchTasks() {
  const res = await fetch(`${backendUrl}/api/tasks`, {
    headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
  });
  if (!res.ok) throw new Error("Failed to fetch tasks");
  return res.json();
}

// Create a new task
export async function createTask(task, token) {
  const res = await fetch(`${backendUrl}/api/tasks`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(task)
  });
  if (!res.ok) throw new Error("Failed to create task");
  return res.json();
}

// Mark task as complete
export async function markTaskCompleted(id) {
  const res = await fetch(`${backendUrl}/api/tasks/${id}/complete`, {
    method: "PATCH",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
  if (!res.ok) throw new Error("Failed to mark task as complete");
  return res.status === 204 ? {} : res.json();
}

// Unmark task as complete
export async function unmarkTaskCompleted(id) {
  const res = await fetch(`${backendUrl}/api/tasks/${id}/uncomplete`, {
    method: "PATCH",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
  if (!res.ok) throw new Error("Failed to unmark task as complete");
  return res.status === 204 ? {} : res.json();
}

// Update an existing task
export async function updateTask(task, token) {
  const res = await fetch(`${backendUrl}/api/tasks/${task.id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(task)
  });
  if (!res.ok) throw new Error("Failed to update task");
  return res.json();
}

// Admin only delete task
export async function deleteTask(id) {
  const res = await fetch(`${backendUrl}/api/tasks/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
  if (!res.ok) throw new Error("Failed to delete task");
  return res.status === 204 ? {} : res.json();
}