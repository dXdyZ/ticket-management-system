package com.another.reportservice.service.reportService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportTaskServiceTest {
    @Autowired
    private ReportTaskService reportTaskService;

    @Test
    void getAndSendProcessingTaskReport() {
        reportTaskService.getAndSendProcessingTaskReport("2025.02.12", "2025.05.23", "ttttilinn85@gmail.com");
    }
}