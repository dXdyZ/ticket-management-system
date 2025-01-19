package com.another.ticketmessageservice.repository;

import com.another.ticketmessageservice.entity.bd_entity.BotUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotUserRepository extends CrudRepository<BotUser, Long> {
}
