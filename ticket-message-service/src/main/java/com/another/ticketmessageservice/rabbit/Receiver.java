package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final EmailIntegrationConfig emailIntegrationConfig;

    public Receiver(EmailIntegrationConfig emailIntegrationConfig) {
        this.emailIntegrationConfig = emailIntegrationConfig;
    }

    @RabbitListener(queues = "MessageEmailGetTaskInWork")
    public void receiveMailGetTaskInWork(@Payload Task task) throws MessagingException {
        emailIntegrationConfig.sendTaskMessage(task, task.getUsers().getEmail());
    }
}
