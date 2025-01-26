package com.example.telegrambot.telegram_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class PushTelegramBot extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;

    @Autowired
    public PushTelegramBot(@Value("${telegram.bot.token}") String botToken, CommandHandler commandHandler,
            CallbackHandler callbackHandler) {
        super(botToken);
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
    }

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            Integer messageId = update.getMessage().getMessageId();

            commandHandler.commandHandler(update.getMessage().getText(), chatId, messageId);
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            callbackHandler.callbackHandler(update.getCallbackQuery().getData(), chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
