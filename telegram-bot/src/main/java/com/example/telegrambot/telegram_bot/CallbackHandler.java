package com.example.telegrambot.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallbackHandler {
    private final TaskTgBotService taskTgBotService;
    private final UserTgBotService userTgBotService;

    @Autowired
    public CallbackHandler(TaskTgBotService taskTgBotService, UserTgBotService userTgBotService) {
        this.taskTgBotService = taskTgBotService;
        this.userTgBotService = userTgBotService;
    }

    public void callbackHandler(String callback, Long chatId) {
        switch (callback) {
            case "CONFIRM" -> taskTgBotService.confirmTask(chatId);
            case "GET_USER_DATA" -> userTgBotService.getUserProfile(chatId);
        }
    }
}
