package com.another.ticket.rabbit;

import com.another.ticket.entity.DTO.CommentDTO;
import com.another.ticket.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class RabbitMessage {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.name.emailGetTaskInWork}")
    private String sendMailGetTaskInWork;

    @Value("${queue.name.CreateTask}")
    private String sendCreateTask;

    @Value("${queue.name.SetStatusTask}")
    private String sendSetStatusTask;

    @Value("${queue.name.SendCommentMessage}")
    private String sendCommentMessage;

    @Autowired
    public RabbitMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMailGetTaskInWork(Task task, Long chatId) {
        if (chatId == null) {
            rabbitTemplate.convertAndSend(sendMailGetTaskInWork, task);
        } else {
            rabbitTemplate.convertAndSend(sendMailGetTaskInWork, task,
                    message -> {
                        MessageProperties properties = message.getMessageProperties();
                        properties.setHeader("CHAT_ID", chatId);
                        return message;
                    });
        }
    }

    public void sendCommentMessage(CommentDTO commentDTO) {
        rabbitTemplate.convertAndSend(sendCommentMessage, commentDTO);
    }

    public void sendCreateTask(Task task) {
        rabbitTemplate.convertAndSend(sendCreateTask, task);
    }

    public void sendSetStatusTask(Task task) {
        rabbitTemplate.convertAndSend(sendSetStatusTask, task);
    }
}
