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

    @Autowired
    public InitializationRabbitQueues(RabbitAdmin rabbitAdmin,
                                      @Qualifier("sendMailGetTaskInWork") Queue queueEmailTaskInWork,
                                      @Qualifier("sendMailCreateTask") Queue queueCreateTask,
                                      @Qualifier("sendMailSetStatusTask") Queue queueSetStatusTask,
                                      @Qualifier("getStatusLogQueue") Queue queueGetStatusLogData,
                                      @Qualifier("sendMailReport") Queue queueSendMailReport) {
        this.rabbitAdmin = rabbitAdmin;
        this.queueEmailTaskInWork = queueEmailTaskInWork;
        this.queueCreateTask = queueCreateTask;
        this.queueSetStatusTask = queueSetStatusTask;
        this.queueGetStatusLogData = queueGetStatusLogData;
        this.queueSendMailReport = queueSendMailReport;
    }

    @PostConstruct
    public void declareQueue() {
        rabbitAdmin.declareQueue(queueEmailTaskInWork);
        rabbitAdmin.declareQueue(queueCreateTask);
        rabbitAdmin.declareQueue(queueSetStatusTask);
        rabbitAdmin.declareQueue(queueGetStatusLogData);
        rabbitAdmin.declareQueue(queueSendMailReport);
    }
}
