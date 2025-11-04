package com.kami.tasklistserver.controller;

import com.kami.tasklistserver.model.Task;
import com.kami.tasklistserver.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        Task created = taskService.createTask(task, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable long id, @RequestBody Task task, Authentication authentication) {
        task.setId(id);
        return ResponseEntity.ok(taskService.updateTask(task, authentication));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable long id, Authentication authentication) {
        taskService.markTaskCompleted(id, authentication);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{id}/uncomplete")
    public ResponseEntity<Task> uncompleteTask(@PathVariable long id, Authentication authentication) {
        taskService.unmarkTaskCompleted(id, authentication);
        return ResponseEntity.ok(taskService.getTaskById(id));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable long id, Authentication authentication) {
        taskService.deleteTask(id, authentication);
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }
}