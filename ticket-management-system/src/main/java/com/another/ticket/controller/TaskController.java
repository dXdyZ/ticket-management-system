package com.another.ticket.controller;

import com.another.ticket.entity.Task;
import com.another.ticket.entity.DTO.TaskDTO;
import com.another.ticket.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody TaskDTO bidDTO, Principal principal) {
        return taskService.createTask(bidDTO, principal);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getByFilter(@RequestParam(name = "status", required = false) List<String> status,
                                  @RequestParam(name = "priority", required = false) List<String> priority,
                                  @RequestParam(name = "username", required = false) String username,
                                  @RequestParam(name = "creteDate", required = false) String createDate,
                                  @RequestParam(name = "endDate", required = false) String endDate){
        if(status == null) {
            status = List.of();
        }
        if (priority == null) {
            priority = List.of();
        }
        try {
            return ResponseEntity.ok(taskService.getTasksFiltered(status, priority, username, createDate, endDate));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("invalid date format", HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/get-work/{id}")
    public ResponseEntity<Task> getTaskInWork(@PathVariable Long id, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.getInWorkTask(id, principal));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Task> setStatus(@PathVariable Long id,
                                          @RequestBody String status) {
        try {
            return ResponseEntity.ok(taskService.setStatus(id, status));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.getById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my")
    public List<Task> getAllByUserTask(Principal principal) {
        return taskService.getAllTaskByUser(principal);
    }
}









