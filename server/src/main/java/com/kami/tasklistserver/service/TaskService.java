package com.kami.tasklistserver.service;

import com.kami.tasklistserver.model.Task;
import com.kami.tasklistserver.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Anyone authenticated can view all tasks
    @PreAuthorize("isAuthenticated()")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Anyone authenticated can view a specific task
    @PreAuthorize("isAuthenticated()")
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    // Only ADMIN or USER can create tasks
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Task createTask(Task task, Authentication authentication) {
        String username = authentication.getName();
        task.setCreatedBy(username);
        Task saved = taskRepository.save(task);
        log.info("User '{}' created task '{}'", username, task.getTitle());
        return saved;
    }

    // Only ADMIN or USER can update tasks
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Task updateTask(Task updatedTask, Authentication authentication) {
        Task existingTask = taskRepository.findById(updatedTask.getId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // Only update fields that are non-blank
        if (StringUtils.hasText(updatedTask.getTitle())) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (StringUtils.hasText(updatedTask.getDescription())) {
            existingTask.setDescription(updatedTask.getDescription());
        }

        log.info("User '{}' updated task '{}'", authentication.getName(), existingTask.getTitle());
        return taskRepository.save(existingTask);
    }

    // Only ADMIN or USER can mark tasks completed
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean markTaskCompleted(long id, Authentication authentication) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(true);
                    taskRepository.save(task);
                    log.info("User '{}' marked task '{}' as completed", authentication.getName(), task.getTitle());
                    return true;
                })
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean unmarkTaskCompleted(long id, Authentication authentication) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(false);
                    task.setCompletedAt(null);
                    taskRepository.save(task);
                    log.info("User '{}' unmarked task '{}' as completed", authentication.getName(), task.getTitle());
                    return true;
                })
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    // Only ADMIN can delete tasks
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteTask(long id, Authentication authentication) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found");
        }
        taskRepository.deleteById(id);
        log.info("User '{}' deleted task with ID '{}'", authentication.getName(), id);
        return true;
    }
}