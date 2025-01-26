package com.another.ticket.service;

import com.another.ticket.component.DateTimeMapper;
import com.another.ticket.entity.*;
import com.another.ticket.entity.DTO.CommentDTO;
import com.another.ticket.exception.ValidStatusException;
import com.another.ticket.rabbit.RabbitMessage;
import com.another.ticket.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final RabbitMessage rabbitMessage;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskService taskService,
                          UserService userService, RabbitMessage rabbitMessage) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
        this.rabbitMessage = rabbitMessage;
    }

    public void addComment(Long taskId, String commentText, Principal principal) throws NoSuchElementException, ValidStatusException {
        Task task = taskService.getTaskById(taskId);
        if (task.getStatus() != Status.OPEN) {
            commentRepository.save(Comment.builder()
                    .task(task)
                    .commentText(commentText)
                    .creatDate(LocalDateTime.now())
                    .users(userService.getUserByPrincipal(principal))
                    .build());
            createCommentMessage(task, commentText, principal);
        } else {
            throw new ValidStatusException("Task is not at work");
        }
    }

    public void createCommentMessage(Task task, String commentText, Principal principal) {
        CommentDTO commentDTO = CommentDTO.builder()
                .commentText(commentText)
                .username(principal.getName())
                .createDate(DateTimeMapper.mapToString(LocalDateTime.now()))
                .build();
        if (principal.getName().equals(task.getUsers().getUsername())) {
            commentDTO.setEmail(task.getWorkUser().getEmail());
            commentDTO.setChatId(Optional.ofNullable(task.getWorkUser().getUserBot())
                    .map(UserBot::getId)
                    .orElse(null));
            rabbitMessage.sendCommentMessage(commentDTO);
        } else {
            commentDTO.setEmail(task.getUsers().getEmail());
            commentDTO.setChatId(Optional.ofNullable(task.getUsers().getUserBot())
                    .map(UserBot::getId)
                    .orElse(null));
            rabbitMessage.sendCommentMessage(commentDTO);
        }
    }


    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Comment not found: " + id)
        );
    }
}
