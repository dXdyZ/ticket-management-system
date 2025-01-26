package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.CommentDTO;
import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import com.another.ticketmessageservice.service.FileWriteAndReadService;
import com.another.ticketmessageservice.service.StatusLogService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;


@Component
public class Receiver {
    private final EmailIntegrationConfig emailIntegrationConfig;
    private final FileWriteAndReadService fileWriteAndReadService;
    private final StatusLogService statusLogService;
    private final RabbitSenderMessage rabbitSenderMessage;
    private final Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    @Autowired
    public Receiver(EmailIntegrationConfig emailIntegrationConfig, FileWriteAndReadService fileWriteService,
                    StatusLogService statusLogService, RabbitSenderMessage rabbitSenderMessage, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        this.emailIntegrationConfig = emailIntegrationConfig;
        this.fileWriteAndReadService = fileWriteService;
        this.statusLogService = statusLogService;
        this.rabbitSenderMessage = rabbitSenderMessage;
        this.jackson2JsonMessageConverter = jackson2JsonMessageConverter;
    }

    @RabbitListener(queues = "MessageSendMailReport")
    public void receiveMessageForSendMailReport(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        String userEmail = messageProperties.getHeader("USER_EMAIL");
        String topicReport = messageProperties.getHeader("TOPIC_REPORT");

        String pathToFile = (String) jackson2JsonMessageConverter.fromMessage(message);
        try {
            emailIntegrationConfig.sendReport(fileWriteAndReadService.fileRead(pathToFile), userEmail, topicReport);
        } catch (FileNotFoundException ex) {
            emailIntegrationConfig.sendBugReportForUser(userEmail);
        }
    }

    @RabbitListener(queues = "MessageComment")
    public void receiveCommentMessage(Message message) {
        CommentDTO commentDTO = (CommentDTO) jackson2JsonMessageConverter.fromMessage(message);
        if (commentDTO.getChatId() != null) {
            rabbitSenderMessage.sendBotMessage(message);
        } else {
            emailIntegrationConfig.sendComment(commentDTO);
        }
    }

    @RabbitListener(queues = "MessageEmailGetTaskInWork")
    public void receiveMailGetTaskInWork(Message message) {
        MessageProperties properties = message.getMessageProperties();
        try {
            if (properties.getHeaders().containsKey("CHAT_ID")) {
                rabbitSenderMessage.sendBotMessage(message);
            } else {
                Task task = (Task) jackson2JsonMessageConverter.fromMessage(message);
                emailIntegrationConfig.sendTaskMessage(task, task.getUsers().getEmail());
            }
        } catch (MessageConversionException e) {
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
