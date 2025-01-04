package com.another.ticket.rabbit;

import com.another.ticket.entity.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessage {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.name.emailGetTaskInWork}")
    private String sendMailGetTaskInWork;

    @Value("${queue.name.CreateTask}")
    private String sendCreateTask;

    @Value("${queue.name.SetStatusTask}")
    private String sendSetStatusTask;

    @Autowired
    public RabbitMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMailGetTaskInWork(Task task) {
        rabbitTemplate.convertAndSend(sendMailGetTaskInWork, task);
    }

    public void sendCreateTask(Task task) {
        rabbitTemplate.convertAndSend(sendCreateTask, task);
    }

    public void sendSetStatusTask(Task task) {
        rabbitTemplate.convertAndSend(sendSetStatusTask, task);
    }
}
