package com.example.telegrambot.service;

import com.example.telegrambot.entity.Users;
import com.example.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setBotChatId(Long chatId, String username) throws NoSuchElementException{
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found: " + username));
        users.setBotChatId(chatId);
        userRepository.save(users);
    }
}
