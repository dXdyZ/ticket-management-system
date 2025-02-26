package com.another.ticket.service;

import com.another.ticket.entity.DTO.RequestReportDTO;
import com.another.ticket.entity.DTO.TaskDTO;
import com.another.ticket.entity.Priority;
import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import com.another.ticket.rabbit.RabbitMessage;
import com.another.ticket.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final UserBotService userBotService;
    private final RabbitMessage rabbitMessage;
    private final TaskCacheProxyService taskCacheService;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService, UserBotService userBotService,
                       RabbitMessage rabbitMessage, TaskCacheProxyService taskCacheService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.userBotService = userBotService;
        this.rabbitMessage = rabbitMessage;
        this.taskCacheService = taskCacheService;
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
    @Cacheable(value = "tasksFilters",
            key = "{#status ?: T(java.util.Collections).emptyList(), " +
                    "#priority ?: T(java.util.Collections).emptyList(), " +
                    "#username ?: '', " +
                    "#startDate ?: '', " +
                    "#endDate ?: ''}")
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


    @Caching(
            evict = {
                    @CacheEvict(value = "tasksFilters", allEntries = true),
                    @CacheEvict(value = "taskById", key = "#id")
            },
            put = {
                    @CachePut(value = "taskById", key = "#id")
            }
    )
    @Transactional
    public Task takeInWorkTask(Long id, Principal principal) throws NoSuchElementException {
        Task task = getTaskById(id);
        if (task.getStatus().equals(Status.OPEN)) {
            task.setWorkUser(userService.getUserByPrincipal(principal));
            task.setStatus(Status.AWAITING_RESPONSE);
            rabbitMessage.sendMailGetTaskInWork(task, userBotService.getChatId(task.getUsers().getUsername()));
            return taskRepository.save(task);
        }
        return null;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "tasksFilters", allEntries = true), //Все фильтры зависятот статуса
            },
            put = {
                    @CachePut(value = "taskById", key = "#id") //Обновление кеша по id
            }
    )
    public Task setStatus(Long id, String status, Principal principal) throws NoSuchElementException {
        Task task = getTaskById(id);
        if (task.getWorkUser().getUsername().equalsIgnoreCase(principal.getName()) ||
                task.getUsers().getUsername().equalsIgnoreCase(principal.getName())) {
            if (status.equalsIgnoreCase("CLOSE")) {
                task.setStatus(Status.CLOSED);
                task.setCloseDate(LocalDateTime.now());
                rabbitMessage.sendSetStatusTask(taskRepository.save(task));
                return task;
            }
            if (status.equalsIgnoreCase("IN JOB")) {
                task.setStatus(Status.IN_JOB);
                if (task.getInJobDate() == null) {
                    task.setInJobDate(LocalDateTime.now());
                }
                rabbitMessage.sendSetStatusTask(taskRepository.save(task));
                return task;
            }
            return task;
        }
        return null;
    }

    @Transactional
    public void taskAcceptanceConfirmation(Long id, Principal principal) {
        try {
            setStatus(id, "in job", principal);
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    //Ограничить доступ в security всем кроме клиентов
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "tasksFilters", allEntries = true) //фильтры могут включать новую задачу
            },
            put = {
                    @CachePut(value = "taskById", key = "#result.id")
            }
    )
    public Task createTask(TaskDTO bidDTO, Principal principal) {
        Task task = taskRepository.save(Task.builder()
                .topic(bidDTO.getTopic())
                .users(userService.getUserByPrincipal(principal))
                .description(bidDTO.getDescription())
                .priority(bidDTO.getPriority())
                .status(Status.OPEN)
                .createDate(LocalDateTime.now())
                .build());
        rabbitMessage.sendCreateTask(task);
        return task;
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
            if (stat.equalsIgnoreCase("high")) {
                priorities.add(Priority.HIGH);
            }
        }
        return !priorities.isEmpty() ? priorities : null;
    }

    public Task getTaskById(Long id) throws NoSuchElementException {
        return taskCacheService.getById(id);
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

    public void getTaskReportForPeriod(String start, String end, String username, Principal principal) {
        RequestReportDTO requestReportDTO = RequestReportDTO.builder()
                .start(start)
                .end(end)
                .email(userService.getUserByPrincipal(principal).getEmail())
                .build();
        if (username != null) requestReportDTO.setUsername(username);
        rabbitMessage.sendRequestReportMessage(requestReportDTO, "task_period");
    }

    public void getReportTaskProcessing(String start, String end, Principal principal) {
        rabbitMessage.sendRequestReportMessage(RequestReportDTO.builder()
                        .email(userService.getUserByPrincipal(principal).getEmail())
                        .end(end)
                        .start(start)
                .build(), "task_processing");
    }
}
