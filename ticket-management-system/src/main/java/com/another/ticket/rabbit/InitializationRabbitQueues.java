package com.another.ticket.rabbit;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializationRabbitQueues {
    private final RabbitAdmin rabbitAdmin;
    private final Queue queueEmailTaskInWork;
    private final Queue queueCreateTask;
    private final Queue queueSetStatusTask;
    private final Queue queueGetStatusLogData;
    private final Queue queueSendMailReport;
    private final Queue queueSendMessageBot;
    private final Queue queueSendCommentMessage;
    private final Queue queueRequestReport;

    @Autowired
    public InitializationRabbitQueues(RabbitAdmin rabbitAdmin,
                                      @Qualifier("sendMailGetTaskInWork") Queue queueEmailTaskInWork,
                                      @Qualifier("sendMailCreateTask") Queue queueCreateTask,
                                      @Qualifier("sendMailSetStatusTask") Queue queueSetStatusTask,
                                      @Qualifier("getStatusLogQueue") Queue queueGetStatusLogData,
                                      @Qualifier("sendMailReport") Queue queueSendMailReport,
                                      @Qualifier("sendBotMessage") Queue queueSendMessageBot,
                                      @Qualifier("sendMessageComment") Queue queueSendCommentMessage,
                                      @Qualifier("sendMessageRequestReport") Queue queueRequestReport) {
        this.rabbitAdmin = rabbitAdmin;
        this.queueEmailTaskInWork = queueEmailTaskInWork;
        this.queueCreateTask = queueCreateTask;
        this.queueSetStatusTask = queueSetStatusTask;
        this.queueGetStatusLogData = queueGetStatusLogData;
        this.queueSendMailReport = queueSendMailReport;
        this.queueSendMessageBot = queueSendMessageBot;
        this.queueSendCommentMessage = queueSendCommentMessage;
        this.queueRequestReport = queueRequestReport;
    }

    @PostConstruct
    public void declareQueue() {
        rabbitAdmin.declareQueue(queueEmailTaskInWork);
        rabbitAdmin.declareQueue(queueCreateTask);
        rabbitAdmin.declareQueue(queueSetStatusTask);
        rabbitAdmin.declareQueue(queueGetStatusLogData);
        rabbitAdmin.declareQueue(queueSendMailReport);
        rabbitAdmin.declareQueue(queueSendMessageBot);
        rabbitAdmin.declareQueue(queueSendCommentMessage);
        rabbitAdmin.declareQueue(queueRequestReport);
    }
}
