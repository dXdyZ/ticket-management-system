package com.example.telegrambot.telegram_bot;

import com.example.telegrambot.entity.BotState;
import com.example.telegrambot.service.UserProfileCache;
import com.example.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserTgBotService {
    private final TelegramBotService telegramBotService;
    private final UserService userService;

    private final Map<Long, BotState> awaitingLogin = new ConcurrentHashMap<>();
    private final UserProfileCache userProfileCache;

    @Autowired
    public UserTgBotService(TelegramBotService telegramBotService, UserService userService, UserProfileCache userProfileCache) {
        this.telegramBotService = telegramBotService;
        this.userService = userService;
        this.userProfileCache = userProfileCache;
    }

    public void getUserProfile(Long chatId) {
        telegramBotService.sendMessage(chatId,
                StringConverter.convertProfileUser(userProfileCache.getUserProfileByChatId(chatId)));
        userProfileCache.removeUserProfileByChatId(chatId);
    }

    public void isAwaitingLogin(Long chatId, String message, Integer messageId) {
        if (awaitingLogin.getOrDefault(chatId, BotState.START) == BotState.AWAIT_LOGIN) {
            loginCommand(chatId, message, messageId);
            awaitingLogin.remove(chatId);
        }
    }

    public void loginCommand(Long chatId, String message, Integer messageId) {
        String[] userDate = message.split(":");
        if (userDate.length == 2) {
            try {
                userService.setBotChatId(chatId, userDate[0]);
                telegramBotService.sendMessage(chatId, "Login successful");
                telegramBotService.deleteMessage(chatId, messageId);
            } catch (NoSuchElementException e) {
                telegramBotService.sendMessage(chatId, e.getMessage());
                telegramBotService.sendMessage(chatId, "please register by link");
            }
        } else {
            telegramBotService.sendMessage(chatId, "Incorrect data entry, try again");
        }
    }

    public void addUserState(Long chatId) {
        awaitingLogin.put(chatId, BotState.AWAIT_LOGIN);
    }
}
