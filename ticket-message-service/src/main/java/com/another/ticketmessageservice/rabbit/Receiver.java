package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import com.another.ticketmessageservice.service.FileWriteAndReadService;
import com.another.ticketmessageservice.service.StatusLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;


@Component
public class Receiver {
    private final EmailIntegrationConfig emailIntegrationConfig;
    private final FileWriteAndReadService fileWriteAndReadService;
    private final StatusLogService statusLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public Receiver(EmailIntegrationConfig emailIntegrationConfig,
                    FileWriteAndReadService fileWriteService, StatusLogService statusLogService) {
        this.emailIntegrationConfig = emailIntegrationConfig;
        this.fileWriteAndReadService = fileWriteService;
        this.statusLogService = statusLogService;
    }

    @RabbitListener(queues = "MessageSendMailReport")
    public void receiveMessageForSendMailReport(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        String userEmail = messageProperties.getHeader("USER_EMAIL");
        String topicReport = messageProperties.getHeader("TOPIC_REPORT");
        try {
            String pathToFile = objectMapper.readValue(message.getBody(), String.class);
            try {
                emailIntegrationConfig.sendReport(fileWriteAndReadService.fileRead(pathToFile), userEmail, topicReport);
            } catch (FileNotFoundException ex) {
                emailIntegrationConfig.sendBugReportForUser(userEmail);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RabbitListener(queues = "MessageEmailGetTaskInWork")
    public void receiveMailGetTaskInWork(@Payload Task task) throws MessagingException {
        emailIntegrationConfig.sendTaskMessage(task, task.getUsers().getEmail());
    }

    @RabbitListener(queues = "MessageSetStatusTask")
    public void receiveSetStatusTask(@Payload Task task) {
        statusLogService.saveOrUpdateStatusLog(task);
        fileWriteAndReadService.writeFileData(task, "set_status.log");
    }

    @RabbitListener(queues = "MessageCreateTask")
    public void receiveCreateTask(@Payload Task task) {
        fileWriteAndReadService.writeFileData(task, "create_task.log");
    }
}
