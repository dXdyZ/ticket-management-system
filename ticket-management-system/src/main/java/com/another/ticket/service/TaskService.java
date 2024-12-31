package com.another.ticket.service;

import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import com.another.ticket.entity.DTO.TaskDTO;
import com.another.ticket.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
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

    //Ограничить доступ в security всем кроме исполнителей
    @Transactional
    public Task getInWorkTask(Long id, Principal principal) throws ChangeSetPersister.NotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        task.setWorkUser(userService.getUserByPrincipal(principal));
        task.setStatus(Status.AWAITING_RESPONSE);
        return taskRepository.save(task);
    }

    @Transactional
    public Task setStatus(Long id, String status) throws ChangeSetPersister.NotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        if (status.equalsIgnoreCase("CLOSE")) {
            task.setStatus(Status.CLOSED);
            taskRepository.save(task);
            return task;
        }
        if (status.equalsIgnoreCase("IN JOB")) {
            task.setStatus(Status.IN_JOB);
            taskRepository.save(task);
            return task;
        }
        return task;
    }

    //Ограничить доступ в security всем кроме клиентов
    @Transactional
    public Task createTask(TaskDTO bidDTO, Principal principal) {
        return taskRepository.save(Task.builder()
                .topic(bidDTO.getTopic())
                .users(userService.getUserByPrincipal(principal))
                .description(bidDTO.getDescription())
                .priority(bidDTO.getPriority())
                .status(Status.OPEN)
                .createDat(new Date())
                .build());

    }

    public Task getById(Long id) throws ChangeSetPersister.NotFoundException {
        return taskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public List<Task> getAllTaskByUser(Principal principal) {
        return taskRepository.findAllByUsers_Id(userService.getUserByPrincipal(principal).getId());
    }
}
