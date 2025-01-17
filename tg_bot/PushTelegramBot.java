package com.another.ticketmessageservice.tg_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class PushTelegramBot extends TelegramLongPollingBot {

    public PushTelegramBot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("message from telegram: {}", update.getMessage().hasText());
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("You said: " + messageText);

            try {
                execute(message);
                log.info("Message sent to chatId: {}", chatId);
            } catch (TelegramApiException e) {
                log.error("telegram error messages: {}", e.getMessage());
            }
        }
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
