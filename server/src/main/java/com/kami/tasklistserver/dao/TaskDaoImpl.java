package com.kami.tasklistserver.dao;

import com.kami.tasklistserver.model.Task;
import com.kami.tasklistserver.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskDaoImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllViewableTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public boolean markTaskCompleted(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(true);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean unmarkTaskCompleted(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(false);
            task.setCompletedAt(null);
            taskRepository.save(task);
            return true;
        }
        return false;
    }

    @Override
    public void revertExpiredCompletions() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (task.isCompleted() && task.getCompletedAt() != null) {
                Duration duration = Duration.between(task.getCompletedAt(), LocalDateTime.now());
                if (duration.toDays() >= 7) {
                    task.setCompleted(false);
                    task.setCompletedAt(null);
                    taskRepository.save(task);
                }
            }
        }
    }

    @Override
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}