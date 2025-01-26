package com.example.telegrambot.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {
    private static final String LOGIN = "/login";
    private static final String START = "/start";

    private final TelegramBotService bot;
    private final UserTgBotService userTgBotService;

    @Autowired
    public CommandHandler(TelegramBotService bot, UserTgBotService userTgBotService) {
        this.bot = bot;
        this.userTgBotService = userTgBotService;
    }

    public void commandHandler(String message, Long chatId, Integer messageId) {

        userTgBotService.isAwaitingLogin(chatId, message, messageId);

        switch (message) {
            case START -> bot.sendMessage(chatId, """
                    Welcome to the bot\s
                    Here you can confirm the acceptance of the application for work\s
                    Commands to control the bot\s
                    /login - authorization user if have account in the system\s
                    /register - take link for register
                    """);
            case LOGIN -> {
                bot.sendMessage(chatId, "Please send your login and password in one message in the form username:password");
                userTgBotService.addUserState(chatId);
            }
        }
    }
}

