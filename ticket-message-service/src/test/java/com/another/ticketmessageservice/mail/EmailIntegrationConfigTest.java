package com.another.ticketmessageservice.mail;

import com.another.ticketmessageservice.entity.Status;
import com.another.ticketmessageservice.entity.report_entity.ProcessingTaskReportEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EmailIntegrationConfigTest {
    @Autowired
    private EmailIntegrationConfig emailIntegrationConfig;

    @Test
    void sendReport() {
        List<ProcessingTaskReportEntity> list = new ArrayList<>() {
            {
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime(1)
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime(1)
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime(1)
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime(1)
                        .build());
            }
        };
        emailIntegrationConfig.sendReport(list, "ttttilinn85@gmail.com", "hello", "emailProcessingTaskReport.html");
    }
}