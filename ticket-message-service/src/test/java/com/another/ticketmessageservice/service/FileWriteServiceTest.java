package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileWriteServiceTest {

    @Autowired
    private FileWriteService fileWriteService;

    @Test
    void writeFileData() {
        fileWriteService.writeFileData(Task.builder()
                        .id(1L)
                        .topic("hello test method")
                        .users(Users.builder()
                                .id(1L)
                                .username("hello")
                                .build())
                        .description("hello weqwewq")
                        .createDate(new Date())
                .build(), "create_task.log");
    }
}