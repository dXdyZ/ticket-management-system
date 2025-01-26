package com.example.telegrambot.telegram_bot;

import com.example.telegrambot.entity.Task;
import com.example.telegrambot.entity.Users;
import com.example.telegrambot.entity.dto.UserProfileDTO;
import com.example.telegrambot.service.UserBotService;
import com.example.telegrambot.service.UserProfileCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskTgBotService {
    public final TelegramBotService telegramBotService;
    private final UserProfileCache userProfileCache;
    private final TaskServiceClient taskServiceClient;


    private final Map<Long, Long> taskMap = new ConcurrentHashMap<>();
    private final UserBotService userBotService;

    @Autowired
    public TaskTgBotService(TelegramBotService telegramBotService, UserProfileCache userProfileCache,
                            TaskServiceClient taskServiceClient, UserBotService userBotService) {
        this.telegramBotService = telegramBotService;
        this.userProfileCache = userProfileCache;
        this.taskServiceClient = taskServiceClient;
        this.userBotService = userBotService;
    }


    //Добавить заголовок авторизации
    public void confirmTask(Long chatId) {
        Users users = userBotService.getUserByChatId(chatId);
        taskServiceClient.confirmTask(taskMap.get(chatId));
        telegramBotService.sendMessage(chatId, "confirm task");
    }


    public void confirmTaskMessage(Long chatId, Task task) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), StringConverter.convertTask(task));

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        InlineKeyboardButton getWorkUserInformation = new InlineKeyboardButton();
        getWorkUserInformation.setText("user information");
        getWorkUserInformation.setCallbackData("GET_USER_DATA");
        confirmButton.setText("confirm");
        confirmButton.setCallbackData("CONFIRM");

        taskMap.put(chatId, task.getId());
        userProfileCache.saveUserProfile(chatId, UserProfileDTO.builder()
                .email(task.getWorkUser().getEmail())
                .username(task.getWorkUser().getUsername())
                .createData(task.getWorkUser().getCreateData())
                .build());

        keyboard.setKeyboard(List.of(
                Arrays.asList(confirmButton, getWorkUserInformation)
        ));
        sendMessage.setReplyMarkup(keyboard);

        telegramBotService.execute(sendMessage);
    }
}
