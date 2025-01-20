package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import com.another.ticketmessageservice.service.FileWriteAndReadService;
import com.another.ticketmessageservice.service.StatusLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    private final RabbitSenderMessage rabbitSenderMessage;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    public Receiver(EmailIntegrationConfig emailIntegrationConfig, FileWriteAndReadService fileWriteService,
                    StatusLogService statusLogService, RabbitSenderMessage rabbitSenderMessage) {
        this.emailIntegrationConfig = emailIntegrationConfig;
        this.fileWriteAndReadService = fileWriteService;
        this.statusLogService = statusLogService;
        this.rabbitSenderMessage = rabbitSenderMessage;
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
    public void receiveMailGetTaskInWork(Message message) {
        MessageProperties properties = message.getMessageProperties();
        try {
            if (properties.getHeaders().containsKey("CHAT_ID")) {
                rabbitSenderMessage.sendBotMessage(message);
            } else {
                Task task = objectMapper.readValue(message.getBody(), Task.class);
                emailIntegrationConfig.sendTaskMessage(task, task.getUsers().getEmail());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
