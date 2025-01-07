package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitSenderMessage {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitSenderMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
