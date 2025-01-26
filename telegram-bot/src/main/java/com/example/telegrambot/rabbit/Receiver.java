package com.example.telegrambot.rabbit;

import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.dto.CommentDTO;
import com.example.telegrambot.telegram_bot.CommentTgBotService;
import com.example.telegrambot.telegram_bot.TaskTgBotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Receiver {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final TaskTgBotService taskTgBotService;
    private final CommentTgBotService commentTgBotService;

    @Autowired
    public Receiver(TaskTgBotService taskTgBotService, CommentTgBotService commentTgBotService) {
        this.taskTgBotService = taskTgBotService;
        this.commentTgBotService = commentTgBotService;
    }

    @RabbitListener(queues = "MessageSendBot")
    public void receiveUserMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Long chatId = properties.getHeader("CHAT_ID");
        try {
            Task task = objectMapper.readValue(message.getBody(), Task.class);
            taskTgBotService.confirmTaskMessage(chatId, task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "MessageComment")
    public void receiveCommentMessage(@Payload CommentDTO commentDTO) {
        commentTgBotService.sendCommentMessage(commentDTO);
    }
}
