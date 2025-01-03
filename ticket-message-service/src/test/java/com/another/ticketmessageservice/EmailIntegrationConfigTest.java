package com.another.ticketmessageservice;

import com.another.ticketmessageservice.entity.*;
import com.another.ticketmessageservice.mail.EmailIntegrationConfig;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class EmailIntegrationConfigTest {
    @Autowired
    private EmailIntegrationConfig emailIntegrationConfig;

    @Test
    void sendTaskMessage() throws MessagingException {
        Task task = Task.builder()
                .id(1L)
                .topic("hello")
                .users(Users.builder()
                        .id(1L)
                        .email("hello@gmail.com")
                        .username("hello")
                        .role(Role.ROLE_CLIENT)
                        .createData(new Date())
                        .build())
                .workUser(Users.builder()
                        .id(2L)
                        .email("worker@gmail.com")
                        .username("workerUser")
                        .role(Role.ROLE_PERFORMER)
                        .createData(new Date())
                        .build())
                .createDate(new Date())
                .description("hello")
                .status(Status.OPEN)
                .priority(Priority.LOW)
                .build();
        emailIntegrationConfig.sendTaskMessage(task, "ttttilinn85@gmail.com");
    }
}