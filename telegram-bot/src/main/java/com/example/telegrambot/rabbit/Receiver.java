package com.example.telegrambot.rabbit;

import com.example.telegrambot.entity.Task;
import com.example.telegrambot.tg_bot.PushTelegramBot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Receiver {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final PushTelegramBot pushTelegramBot;

    @Autowired
    public Receiver(PushTelegramBot pushTelegramBot) {
        this.pushTelegramBot = pushTelegramBot;
    }

    @RabbitListener(queues = "MessageSendBot")
    public void sendUserMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Long chatId = properties.getHeader("CHAT_ID");
        try {
            Task task = objectMapper.readValue(message.getBody(), Task.class);
            pushTelegramBot.confirmTaskMessage(chatId, task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
