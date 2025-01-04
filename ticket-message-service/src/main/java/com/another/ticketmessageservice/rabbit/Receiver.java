package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import com.another.ticketmessageservice.service.FileWriteService;
import com.another.ticketmessageservice.write.FileWriter;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final EmailIntegrationConfig emailIntegrationConfig;
    private final FileWriteService fileWriteService;

    @Autowired
    public Receiver(EmailIntegrationConfig emailIntegrationConfig, FileWriteService fileWriteService) {
        this.emailIntegrationConfig = emailIntegrationConfig;
        this.fileWriteService = fileWriteService;
    }

    @RabbitListener(queues = "MessageEmailGetTaskInWork")
    public void receiveMailGetTaskInWork(@Payload Task task) throws MessagingException {
        emailIntegrationConfig.sendTaskMessage(task, task.getUsers().getEmail());
    }

    @RabbitListener(queues = "MessageSetStatusTask")
    public void receiveSetStatusTask(@Payload Task task) {
        fileWriteService.writeFileData(task, "set_status.log");
    }

    @RabbitListener(queues = "MessageCreateTask")
    public void receiveCreateTask(@Payload Task task) {
        fileWriteService.writeFileData(task, "create_task.log");
    }
}
