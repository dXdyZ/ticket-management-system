package com.example.telegrambot.service;

import com.example.telegrambot.entity.UserBot;
import com.example.telegrambot.entity.Users;
import com.example.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserBotService userBotService;

    @Autowired
    public UserService(UserRepository userRepository, UserBotService userBotService) {
        this.userRepository = userRepository;
        this.userBotService = userBotService;
    }

    @Transactional
    public void setBotChatId(Long chatId, String username) throws NoSuchElementException{
        userBotService.saveUserBot(UserBot.builder()
                        .id(chatId)
                        .users(userRepository.findByUsername(username)
                                .orElseThrow(() -> new NoSuchElementException("User not found: " + username)))
                .build());
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
