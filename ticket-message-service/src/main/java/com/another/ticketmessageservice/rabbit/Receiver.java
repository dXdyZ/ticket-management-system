package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.entity.report_entity.ProcessingTaskReportEntity;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import com.another.ticketmessageservice.service.FileWriteService;
import com.another.ticketmessageservice.service.StatusLogService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Receiver {
    private final EmailIntegrationConfig emailIntegrationConfig;
    private final FileWriteService fileWriteService;
    private final StatusLogService statusLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public Receiver(EmailIntegrationConfig emailIntegrationConfig,
                    FileWriteService fileWriteService, StatusLogService statusLogService) {
        this.emailIntegrationConfig = emailIntegrationConfig;
        this.fileWriteService = fileWriteService;
        this.statusLogService = statusLogService;
    }

    @RabbitListener(queues = "MessageSendMailReport")
    public void receiveMessageForSendMailReport(Message message) {
        List<String> className = new ArrayList<>() {
            {
                add("ProcessingTaskReport");
                add("TaskPeriodReport");
                add("UserPeriodReport");
            }
        };
        MessageProperties messageProperties = message.getMessageProperties();
        String getHeaders = messageProperties.getHeader("OBJECT_NAME");
        String userEmail = messageProperties.getHeader("USER_EMAIL");
        try {
            if (className.get(0).equals(getHeaders)) {
                List<ProcessingTaskReportEntity> processingTaskReportEntityList =
                        objectMapper.readValue(message.getBody(),
                                new TypeReference<List<ProcessingTaskReportEntity>>() {});
                emailIntegrationConfig.sendReport(processingTaskReportEntityList, userEmail,
                        "Время обработок заявок", "emailProcessingTaskReport.html");
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
        fileWriteService.writeFileData(task, "set_status.log");
    }

    @RabbitListener(queues = "MessageCreateTask")
    public void receiveCreateTask(@Payload Task task) {
        fileWriteService.writeFileData(task, "create_task.log");
    }
}
