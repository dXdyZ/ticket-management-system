package com.example.telegrambot.tg_bot;

import com.example.telegrambot.entity.Priority;
import com.example.telegrambot.entity.Status;
import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.Users;
import com.example.telegrambot.telegram_bot.PushTelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class PushTelegramBotTest {

    @Autowired
    private PushTelegramBot pushTelegramBot;

    @Test
    void confirmTask() {
    }
}
