package com.another.ticketmessageservice.tg_bot;

import com.another.ticketmessageservice.entity.bd_entity.BotUser;
import com.another.ticketmessageservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PushTelegramBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String LOGIN = "/login";

    private final Map<Long, Boolean> awaitingLogin = new ConcurrentHashMap<>();

    private final UserService userService;

    @Autowired
    public PushTelegramBot(@Value("${telegram.bot.token}") String botToken,
                           UserService userService) {
        super(botToken);
        this.userService = userService;
    }

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();



        if (awaitingLogin.getOrDefault(chatId, false)) {
            loginCommand(chatId, message, update.getMessage().getMessageId());
            awaitingLogin.remove(chatId);
            return;
        }

        switch (message) {
            case START -> {
                String username = update.getMessage().getChat().getUserName();
                startCommand(chatId, username);
            }
            case LOGIN -> {
                sendMessage(chatId, "Please send your login and password in one message in the form username:password");
                awaitingLogin.put(chatId, true);
            }
        }
    }

    public void loginCommand(Long chatId, String message, Integer messageId) {
        String[] userDate = message.split(":");
        BotUser botUser = userService.saveUser(userDate[0], userDate[1], chatId);
        sendMessage(chatId, "Login successful");
        deleteMessage(chatId, messageId);
    }

    public void startCommand(Long charId, String username) {
        String text = "Welcome to the bot " + username + "\n" +
                "Here you can confirm the acceptance of the application for work \n" +
                "Commands to control the bot \n" +
                "/login - authorization user if have account in the system \n";
        sendMessage(charId, text);
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
