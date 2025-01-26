package com.another.ticket.controller;

import com.another.ticket.entity.DTO.TaskDTO;
import com.another.ticket.entity.Task;
import com.another.ticket.exception.ValidStatusException;
import com.another.ticket.service.CommentService;
import com.another.ticket.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final CommentService commentService;
    private final TaskService taskService;

    @Autowired
    public TaskController(CommentService commentService, TaskService taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }


    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO bidDTO, Principal principal) {
        return new ResponseEntity<>(taskService.createTask(bidDTO, principal),
                CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getByFilter(@RequestParam(name = "status", required = false) List<String> status,
                                  @RequestParam(name = "priority", required = false) List<String> priority,
                                  @RequestParam(name = "username", required = false) String username,
                                  @RequestParam(name = "createDate", required = false) String createDate,
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

    @PatchMapping("/take-work/{id}")
    public ResponseEntity<?> takeTaskInWork(@PathVariable Long id, Principal principal) {
        try {
            Task task = taskService.takeInWorkTask(id, principal);
            if (task != null) {
                return ResponseEntity.ok(taskService.takeInWorkTask(id, principal));
            } else return new ResponseEntity<>("Task busy", HttpStatus.CONFLICT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Task> setStatus(@PathVariable Long id,
                                          @RequestBody String status, Principal principal) {
        try {
            return ResponseEntity.ok(taskService.setStatus(id, status, principal));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/taskAcceptanceConfirmation/{id}")
    public void taskAcceptanceConfirmation(@PathVariable Long id, Principal principal) {
        taskService.taskAcceptanceConfirmation(id, principal);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/my")
    public List<Task> getAllByUserTask(Principal principal) {
        return taskService.getAllTaskByUser(principal);
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String commentText, Principal principal) {
        try {
            commentService.addComment(id, commentText, principal);
            return new ResponseEntity<>(CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ValidStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/report/period/{start}/{end}")
    public void getTaskReportForPeriod(@PathVariable String start,
                                       @PathVariable String end,
                                       @RequestParam(value = "username", required = false) String username,
                                       Principal principal) {
        taskService.getTaskReportForPeriod(start, end, username, principal);
    }

    @GetMapping("/report/processing/{start}/{end}")
    public void getTaskProcessingReport(@PathVariable String start,
                                        @PathVariable String end, Principal principal) {
        taskService.getReportTaskProcessing(start, end, principal);
    }
}









