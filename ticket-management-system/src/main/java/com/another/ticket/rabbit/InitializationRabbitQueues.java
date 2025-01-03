package com.another.ticket.rabbit;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializationRabbitQueues {
    private final RabbitAdmin rabbitAdmin;
    private final Queue queueEmailTaskInWork;

    @Autowired
    public InitializationRabbitQueues(RabbitAdmin rabbitAdmin,
                                      Queue queueEmailTaskInWork) {
        this.rabbitAdmin = rabbitAdmin;
        this.queueEmailTaskInWork = queueEmailTaskInWork;
    }

    @PostConstruct
    public void declareQueue() {
        rabbitAdmin.declareQueue(queueEmailTaskInWork);
    }
}
