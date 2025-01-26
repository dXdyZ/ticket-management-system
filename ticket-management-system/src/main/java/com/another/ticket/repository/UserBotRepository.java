package com.another.ticket.repository;

import com.another.ticket.entity.UserBot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBotRepository extends CrudRepository<UserBot, Long> {
    Optional<UserBot> findByUsers_Username(String usersUsername);
}
