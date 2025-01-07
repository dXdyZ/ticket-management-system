package com.another.ticketmessageservice.service;

import com.another.ticketmessageservice.entity.Priority;
import com.another.ticketmessageservice.entity.Status;
import com.another.ticketmessageservice.entity.Task;
import com.another.ticketmessageservice.entity.Users;
import com.another.ticketmessageservice.repository.StatusLogRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatusLogServiceTest {
    private static final Logger log = LoggerFactory.getLogger(StatusLogServiceTest.class);
    @Autowired
    private StatusLogService statusLogService;

    @Autowired
    private StatusLogRepository statusLogRepository;
    @Test
    void saveOrUpdateStatusLog() {
        statusLogService.saveOrUpdateStatusLog(Task.builder()
                        .id(1L)
                        .topic("hello bd")
                        .users(new Users())
                        .description("hello bd")
                        .priority(Priority.MID)
                        .createDate(now())
                        .status(Status.AWAITING_RESPONSE)
                .build());
        log.info("data from bd: {}", statusLogRepository.findByTask_Id(1L));
    }

    @Test
    void test() {
        log.info("get data from bd: {}", statusLogRepository.findAll());
    }
}