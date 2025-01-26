package com.example.telegrambot.service;

import com.example.telegrambot.entity.UserBot;
import com.example.telegrambot.entity.Users;
import com.example.telegrambot.repository.UserBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBotService {
    private final UserBotRepository userBotRepository;

    @Autowired
    public UserBotService(UserBotRepository userBotRepository) {
        this.userBotRepository = userBotRepository;
    }

    public void saveUserBot(UserBot userBot) {
        userBotRepository.save(userBot);
    }

    public Users getUserByChatId(Long chatId) {
        return userBotRepository.findById(chatId).orElse(null).getUsers();
    }
}
