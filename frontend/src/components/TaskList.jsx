import React from 'react';

export default function TaskList({ tasks, onDelete, onComplete, onUnmark, userRole, onSelectTask }) {
  if (!tasks.length) return <p>No tasks found.</p>;

  return (
    <div className="task-list">
      {tasks.map(task => (
        <div
          key={task.id}
          className="task-item"
          onClick={() => onSelectTask(task)}
          title="Click to edit"
        >
          <div className="task-details">
            <span className="task-id">ID: #{task.id}</span>
            <span className="task-title">{task.title}</span>
            <span className="task-description">{task.description}</span>
          </div>

          <div className="task-actions">
            <div className="action-buttons">
              {task.completed ? (
                <button
                  className="unmark-button"
                  title="Unmark Task"
                  onClick={(e) => {
                    e.stopPropagation();
                    if (window.confirm("Unmark this task as complete?")) {
                      onUnmark(task.id);
                    }
                  }}
                >
                  ⏪
                </button>
              ) : (
                <button
                  className="complete-button"
                  title="Mark Complete"
                  onClick={(e) => {
                    e.stopPropagation();
                    onComplete(task.id);
                  }}
                >
                  ✅
                </button>
              )}

              {userRole === "ADMIN" && (
                <button
                  className="delete-button"
                  title="Delete Task"
                  onClick={(e) => {
                    e.stopPropagation();
                    onDelete(task.id);
                  }}
                >
                  🗑️
                </button>
              )}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}