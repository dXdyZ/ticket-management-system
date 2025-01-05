package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.Task;
import com.another.reportservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getTaskBetweenDate(LocalDate start, LocalDate end) {
        return taskRepository.findAllByCreateDateBetween(start, end);
    }

    public List<Task> getTaskByUsername(String username) throws ChangeSetPersister.NotFoundException {
        return taskRepository.findAllByUsers(userService.getUserByUsername(username));
    }

    public List<Object[]> getTaskCountsByMonth(LocalDate start, LocalDate end) {
        return taskRepository.findTaskCountsByMonth(start, end);
    }

    public List<Object[]> getTaskCountsByPriority(LocalDate start, LocalDate end) {
        return taskRepository.findTaskCountsByPriority(start, end);
    }

    public List<Object[]> getTaskCountsByStatus(LocalDate start, LocalDate end) {
        return taskRepository.findTaskCountsByStatus(start, end);
    }
}
