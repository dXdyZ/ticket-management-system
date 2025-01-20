package com.example.telegrambot.tg_bot;

import com.example.telegrambot.entity.Priority;
import com.example.telegrambot.entity.Status;
import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PushTelegramBotTest {

    @Autowired
    private PushTelegramBot pushTelegramBot;

    @Test
    void confirmTask() {
        pushTelegramBot.confirmTaskMessage(1978636991L, Task.builder()
                        .id(1L)
                        .topic("hello")
                        .users(Users.builder()
                                .id(1L)
                                .username("hello")
                                .build())
                        .description("hello12321")
                        .status(Status.OPEN)
                        .priority(Priority.LOW)
                        .createDate(LocalDateTime.now())
                .build());
    }
}