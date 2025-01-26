package com.example.telegrambot.telegram_bot;

import com.example.telegrambot.entity.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentTgBotService {
    private final TelegramBotService telegramBotService;

    @Autowired
    public CommentTgBotService(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    public void sendCommentMessage(CommentDTO commentDTO) {
        telegramBotService.sendMessage(commentDTO.getChatId(), StringConverter.convertComment(commentDTO));
    }
}
