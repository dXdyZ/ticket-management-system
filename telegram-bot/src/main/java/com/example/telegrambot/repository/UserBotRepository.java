package com.example.telegrambot.repository;

import com.example.telegrambot.entity.UserBot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBotRepository extends CrudRepository<UserBot, Long> {
}
