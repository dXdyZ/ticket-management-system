package com.another.ticket.service;

import com.another.ticket.entity.Task;
import com.another.ticket.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TaskCacheProxyService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskCacheProxyService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Cacheable(value = "taskById", key = "#id")
    public Task getById(Long id) throws NoSuchElementException {
        return taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Task not found: " + id)
        );
    }
}
