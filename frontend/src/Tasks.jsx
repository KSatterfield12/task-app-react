import { useState } from "react";
import TaskList from "./components/TaskList";
import TaskForm from "./TaskForm";
import UpdateTaskForm from './UpdateTaskForm';
import { markTaskCompleted, unmarkTaskCompleted, deleteTask } from "./services/taskService";
import "./styles/tasks.css";

export default function Tasks({ tasks, onDelete, onTaskCreated, userRole, token, search, setSearch, refreshTasks }) {
const [showAll, setShowAll] = useState(false);
const [showCompletedOnly, setShowCompletedOnly] = useState(false);
const [showIncompleteOnly, setShowIncompleteOnly] = useState(false);
const [activeForm, setActiveForm] = useState(null);
const [selectedTask, setSelectedTask] = useState(null);

 const noTogglesActive = !showAll && !showCompletedOnly && !showIncompleteOnly;

 const filteredTasks = tasks
   .filter(task => {
     const matchesSearch = search
       ? (task.title || "").toLowerCase().includes(search.toLowerCase())
       : true;

     const matchesFilter = noTogglesActive
       ? true
       : (showAll ||
           (showCompletedOnly && task.completed) ||
           (showIncompleteOnly && !task.completed));

     return matchesSearch && matchesFilter;
   })
   .sort((a, b) => a.id - b.id); // Displays in numerical order

  // Handler to mark task complete
  const handleComplete = async (id) => {
    try {
      await markTaskCompleted(id);
      refreshTasks();
    } catch (error) {
      console.error("Error marking task complete:", error);
    }
  };

  // Handler to unmark task complete
  const handleUnmark = async (id) => {
    try {
      await unmarkTaskCompleted(id);
      refreshTasks();
    } catch (error) {
      console.error("Error unmarking task:", error);
    }
  };

  // Handler to delete a task
  const handleDelete = async (id) => {
    try {
      await deleteTask(id);
      refreshTasks();
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  };


  return (
    <main className="task-page">
      <div className="task-grid">
        {/* Left column: Search + Task List */}
        <div className="task-left">
          <div className="search-bar">
            <div className="search-input-wrapper">
              <input
                type="text"
                placeholder="Search the task title..."
                value={search}
                onChange={(e) => {
                  setSearch(e.target.value);
                  setShowAll(false);
                  setShowCompletedOnly(false);
                  setShowIncompleteOnly(false);
                }}
              />
              {search && (
                <div className="search-buttons">
                  <button
                    className="backspace-button"
                    onClick={() => setSearch(search.slice(0, -1))}
                    title="Backspace"
                  >
                    ⌫
                  </button>
                  <button
                    className="clear-button"
                    onClick={() => setSearch("")}
                    title="Clear search"
                  >
                    ❌
                  </button>
                </div>
              )}
            </div>
          </div>

          {/* Toggle row should be outside search-bar */}
          {!search && (
            <div className="toggle-row">
              <label className="toggle-container">
                <input
                  type="checkbox"
                  checked={showAll}
                  onChange={(e) => {
                    setShowAll(e.target.checked);
                    setShowCompletedOnly(false);
                    setShowIncompleteOnly(false);
                  }}
                />
                <span className="slider"></span>
                <span className="toggle-label">View All Tasks</span>
              </label>

              <label className="toggle-container">
                <input
                  type="checkbox"
                  checked={showCompletedOnly}
                  onChange={(e) => {
                    setShowCompletedOnly(e.target.checked);
                    setShowAll(false);
                    setShowIncompleteOnly(false);
                  }}
                />
                <span className="slider"></span>
                <span className="toggle-label">Completed Only</span>
              </label>

              <label className="toggle-container">
                <input
                  type="checkbox"
                  checked={showIncompleteOnly}
                  onChange={(e) => {
                    setShowIncompleteOnly(e.target.checked);
                    setShowAll(false);
                    setShowCompletedOnly(false);
                  }}
                />
                <span className="slider"></span>
                <span className="toggle-label">Incomplete Only</span>
              </label>
            </div>
          )}

          {/* Task list below toggles */}
          {(search || showAll || showCompletedOnly || showIncompleteOnly) ? (
            <TaskList
              tasks={filteredTasks}
              onDelete={handleDelete}
              onComplete={handleComplete}
              onUnmark={handleUnmark}
              userRole={userRole}
              onSelectTask={(task) => {
                setSelectedTask(task);
                setActiveForm('update');
              }}
            />
          ) : (
            <p className="task-placeholder">Use the search or toggle to view tasks.</p>
          )}
        </div>

        {/* Right column: New Task and Update Task Forms */}
         <div className="task-right">
               <h2 className="task-form-title">Task Actions</h2>

               {/* Toggle Create Form */}
               <button
                 onClick={() =>
                   setActiveForm(activeForm === 'create' ? null : 'create')
                 }
               >
                 {activeForm === 'create' ? 'Hide Create Form' : 'Create Task'}
               </button>

               {/* Toggle Update Form */}
               <button
                 onClick={() =>
                   setActiveForm(activeForm === 'update' ? null : 'update')
                 }
               >
                 {activeForm === 'update' ? 'Hide Update Form' : 'Update Task'}
               </button>

               {/* Render Forms Conditionally */}
               {activeForm === 'create' && (
                 <TaskForm token={token} onTaskCreated={onTaskCreated} />
               )}
               {activeForm === 'update' && (
                   <UpdateTaskForm token={token}
                    onTaskUpdated={refreshTasks}
                    onCloseForm={() => setActiveForm(null)}/>
               )}
             </div>
         </div>
    </main>
  );
}