package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.entity.bd_entity.BotUser;
import com.another.ticketmessageservice.repository.BotUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final BotUserRepository botUserRepository;

    @Autowired
    public UserService(BotUserRepository botUserRepository) {
        this.botUserRepository = botUserRepository;
    }

    public BotUser saveUser(String username, String password, Long chatId) {
        return botUserRepository.save(BotUser.builder()
                        .id(chatId)
                        .username(username)
                        .password(password)
                .build());
    }
}
