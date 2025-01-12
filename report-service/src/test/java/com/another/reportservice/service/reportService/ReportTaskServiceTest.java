package com.another.reportservice.service.reportService;

import com.another.reportservice.entity.Priority;
import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.Users;
import com.another.reportservice.entity.reportEntity.StatusLog;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class ReportTaskServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ReportTaskServiceTest.class);
    @Autowired
    private ReportTaskService reportTaskService;

    @Test
    void getAndSendProcessingTaskReport() {
        reportTaskService.getAndSendProcessingTaskReport("2025.02.12", "2025.05.23", "ttttilinn85@gmail.com");
    }

    @Test
    void testProcessingTaskMap() {
        List<StatusLog> ls = new ArrayList<>() {
            {
                add(StatusLog.builder()
                        .id("12")
                        .task(new Task().builder()
                                .id(1L)
                                .users(Users.builder()
                                        .username("hello")
                                        .id(1L)
                                        .build())
                                .workUser(Users.builder()
                                        .id(2L)
                                        .username("hello")
                                        .build())
                                .priority(Priority.LOW)
                                .status(Status.OPEN)
                                .topic("hello")
                                .build())
                        .setStatusIN_JOB(List.of(LocalDateTime.of(2026, 2, 5, 12, 56)))
                        .setStatusCLOSED(LocalDateTime.of(2026, 2, 11, 12, 56))
                        .setStatusAWATIN_RESPONSE(List.of(LocalDateTime.now()))
                        .build());
            }
        };

    }
}