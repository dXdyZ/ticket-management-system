package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.entity.Users;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileWriteServiceTest {

    private static final Logger log = LoggerFactory.getLogger(FileWriteServiceTest.class);
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
                        .createDate(now())
                .build(), "create_task.log");
    }
}