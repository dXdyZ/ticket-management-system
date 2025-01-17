package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.Task;
import com.another.reportservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getTaskBetweenDate(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findAllByCreateDateBetween(start, end);
    }

    public List<Task> getTaskByUsername(String username, LocalDateTime start, LocalDateTime end) throws NoSuchElementException {
        return taskRepository.findAllByUsersAndCreateDateBetween(userService.getUserByUsername(username), start, end);
    }

    @Transactional
    public List<Task> getTaskByClosedAndUsername(String username) {
        return taskRepository.findAllByStatusAndUsers_Username(Status.CLOSED, username);
    }

    public List<Task> getByUsername(String username) {
        return taskRepository.findAllByUsers_Username(username);
    }
}
