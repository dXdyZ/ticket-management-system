package com.another.ticket.service;

import com.another.ticket.entity.Priority;
import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import com.another.ticket.entity.DTO.TaskDTO;
import com.another.ticket.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getTaskByStatus(List<String> status) {
        List<Status> statusList = mapStringInStatus(status);
        if (statusList != null) {
            return taskRepository.findAllByStatusIn(statusList, Pageable.ofSize(10));
        } else return null;
    }

    public List<Task> getTakByPriority(List<String> priority) {
        List<Priority> priorityList = mapStringInPriority(priority);
        if (priorityList != null) {
            return taskRepository.findAllByPriorityIn(priorityList, Pageable.ofSize(10));
        } else return null;
    }

    public List<Task> getTaskByClient(String username) {
        if (userService.existsUserByUsername(username)) {
            return taskRepository.findAllByUsers_Username(username, Pageable.ofSize(10));
        } else return null;
    }

    public List<Task> getByCreateDate(String startDate, String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            if (endDate != null) {
                Date startMapDate = simpleDateFormat.parse(startDate);
                Date endMapDate = simpleDateFormat.parse(endDate);
                return taskRepository.findAllByCreateDateBetween(startMapDate, endMapDate, Pageable.ofSize(10));
            } else {
                Date startMapDate = simpleDateFormat.parse(startDate);
                return taskRepository.findAllByCreateDateBetween(startMapDate, startMapDate, Pageable.ofSize(10));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<Task> getTasksFiltered(List<String> status, List<String> priority,
                                       String username, String startDate, String endDate) throws RuntimeException {
        Specification<Task> spec = Specification.where(null);
        if (status != null && !status.isEmpty()) {
            spec = spec.and(TaskSpecifications.hasStatusIn(status));
        }
        if (priority != null && !priority.isEmpty()) {
            spec = spec.and(TaskSpecifications.hasPriorityIn(priority));
        }
        if (username != null) {
            spec = spec.and(TaskSpecifications.hasUsername(username));
        }
        if (startDate != null && endDate != null) {
            Date startMapDate;
            Date endMapDate;
            try {
                startMapDate = simpleDateFormat.parse(startDate);
                endMapDate = simpleDateFormat.parse(endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            spec = spec.and(TaskSpecifications.hasDateRange(startMapDate, endMapDate));
        }
        return taskRepository.findAll(spec, Pageable.ofSize(10)).getContent();
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
                .createDate(new Date())
                .build());
    }

    public Task getById(Long id) throws ChangeSetPersister.NotFoundException {
        return taskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

    }

    public List<Task> getAllTaskByUser(Principal principal) {
        return taskRepository.findAllByUsers_Id(userService.getUserByPrincipal(principal).getId());
    }


    private List<Priority> mapStringInPriority(List<String> priority) {
        List<Priority> priorities = new ArrayList<>();
        for (String stat : priority) {
            if (stat.equalsIgnoreCase("low")) {
                priorities.add(Priority.LOW);
            }
            if (stat.equalsIgnoreCase("mid")) {
                priorities.add(Priority.MID);
            }
            if (stat.equalsIgnoreCase("HIGH")) {
                priorities.add(Priority.HIGH);
            }
        }
        return !priorities.isEmpty() ? priorities : null;
    }

    private List<Status> mapStringInStatus(List<String> status) {
        List<Status> statusList = new ArrayList<>();
        for (String stat : status) {
            if (stat.equalsIgnoreCase("open")) {
                statusList.add(Status.OPEN);
            }
            if (stat.equalsIgnoreCase("in job")) {
                statusList.add(Status.IN_JOB);
            }
            if (stat.equalsIgnoreCase("closed")) {
                statusList.add(Status.CLOSED);
            }
            if (stat.equalsIgnoreCase("awaiting response")) {
                statusList.add(Status.AWAITING_RESPONSE);
            }
        }
        return !statusList.isEmpty() ? statusList : null;
    }
}
