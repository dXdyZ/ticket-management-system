package com.another.reportservice.rabbit;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitSenderMessage {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitSenderMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${queue.name.SendMailReport}")
    private String mailReport;

    public void sendMessageReport(Object report, String objectName, String userEmail) {
        Map<String, Object> headers = new HashMap<>() {
            {
                put("OBJECT_NAME", objectName);
                put("USER_EMAIL", userEmail);
            }
        };
        rabbitTemplate.convertAndSend(mailReport, report,
                message -> {
                    MessageProperties properties =
                            message.getMessageProperties();
                    properties.setHeaders(headers);
                    return message;
                });
    }
}
