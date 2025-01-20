package com.another.ticketmessageservice.rabbit;

import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitSenderMessage {
    private final RabbitTemplate rabbitTemplate;

    @Value("${telegram.queue.SendMessageBot}")
    private String messageSendBot;

    @Autowired
    public RabbitSenderMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBotMessage(Message message) {
        rabbitTemplate.send(messageSendBot, message);
    }
}
