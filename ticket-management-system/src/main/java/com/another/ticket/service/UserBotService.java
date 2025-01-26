package com.another.ticket.service;

import com.another.ticket.entity.UserBot;
import com.another.ticket.repository.UserBotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserBotService {
    private final UserBotRepository userBotRepository;

    @Autowired
    public UserBotService(UserBotRepository userBotRepository) {
        this.userBotRepository = userBotRepository;
    }

    public UserBot getById(Long id) {
        return userBotRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User by chat id not found: " + id)
        );
    }

    public void createUserBot(UserBot userBot) {
        userBotRepository.save(userBot);
    }

    public Long getChatId(String username) {
        return userBotRepository.findByUsers_Username(username)
                .map(UserBot::getId)
                .orElse(null);
    }
}
