package com.kami.tasklistserver.dao;

import com.kami.tasklistserver.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    // No-auth task view
    List<Task> getAllViewableTasks();

    // Authenticated view to individual tasks
    Optional<Task> getTaskById(Long id);

    //Create a task auth user
    Task createTask(Task task);

    //Update task details as auth user
    Task updateTask(Task task);

    //Mark task complete as auth user
    boolean markTaskCompleted(Long id);

    // Unmark task as incomplete with confirmation
    boolean unmarkTaskCompleted(Long id);

    // Check and auto-revert tasks older than 7 days
    void revertExpiredCompletions();

    //Delete task admin-only access
    boolean deleteTask(Long id);}