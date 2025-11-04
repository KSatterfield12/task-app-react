package com.kami.tasklistserver.repository;

import com.kami.tasklistserver.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find all completed tasks
    List<Task> findByCompletedTrue();

    // Find all incomplete tasks
    List<Task> findByCompletedFalse();

    // Search tasks by title keyword (case-insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
}