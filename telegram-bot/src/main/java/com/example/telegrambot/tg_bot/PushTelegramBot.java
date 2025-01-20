package com.example.telegrambot.tg_bot;

import com.example.telegrambot.entity.BotState;
import com.example.telegrambot.entity.Task;
import com.example.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PushTelegramBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";

    private final Map<Long, BotState> awaitingLogin = new ConcurrentHashMap<>();
    private final Map<Long, Long> taskMap = new ConcurrentHashMap<>();

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Autowired
    public PushTelegramBot(@Value("${telegram.bot.token}") String botToken,
                           UserService userService, RestTemplate restTemplate) {
        super(botToken);
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            log.info("chat id: {}", chatId);

            if (awaitingLogin.getOrDefault(chatId, BotState.START) == BotState.AWAIT_LOGIN) {
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
                    awaitingLogin.put(chatId, BotState.AWAIT_LOGIN);
                }
                case REGISTER -> registerCommand(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callback) {
                case "CONFIRM" -> confirmTask(chatId);
            }
        }
    }

    public void confirmTask(Long chatId) {
        restTemplate.getForObject(
                "http://localhost:9191/tasks/taskAcceptanceConfirmation/" + taskMap.get(chatId), Void.class);
    }

    public void confirmTaskMessage(Long chatId, Task task) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), mapTaskInString(task));

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("confirm");
        button.setCallbackData("CONFIRM");

        taskMap.put(chatId, task.getId());

        keyboard.setKeyboard(Collections.singletonList(Collections.singletonList(button)));
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String mapTaskInString(Task task) {
        return "Задача ожидает подтверждения\n" +
                "id: " + task.getId() + "\n" +
                "topic: " + task.getTopic() + "\n" +
                "description: " + task.getDescription() + "\n" +
                "priority: " + task.getPriority().toString() + "\n" +
                "create date: " + task.getCreateDate() + "\n" +
                "create user: " + task.getUsers().getUsername() + "\n";
    }

    public void loginCommand(Long chatId, String message, Integer messageId) {
        String[] userDate = message.split(":");
        if (userDate.length == 2) {
            try {
                userService.setBotChatId(chatId, userDate[0]);
                sendMessage(chatId, "Login successful");
                deleteMessage(chatId, messageId);
            } catch (NoSuchElementException e) {
                sendMessage(chatId, e.getMessage());
                registerCommand(chatId);
            }
        } else {
            sendMessage(chatId, "Incorrect data entry, try again");
        }
    }

    public void registerCommand(Long chatId) {
        sendMessage(chatId, "Please register using this link");
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
