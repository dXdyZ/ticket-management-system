package com.another.ticket.rabbit;

import com.another.ticket.entity.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class RabbitMessageTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMessageTest.class);
    @Autowired
    private RabbitMessage rabbitMessage;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void sendCreateTask() {
        Task task = Task.builder()
                .id(1L)
                .topic("hello 123")
                .createDate(LocalDateTime.now())
                .status(Status.OPEN)
                .users(Users.builder()
                        .id(1L)
                        .botChatId(12312L)
                        .email("hello@hello.com")
                        .username("hello")
                        .password("hello")
                        .role(Role.ROLE_CLIENT)
                        .build())
                .description("qw123")
                .priority(Priority.HIGH)
                .build();
        rabbitMessage.sendMailGetTaskInWork(task, 1234412L);
    }
}