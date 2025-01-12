package com.another.reportservice.service;

import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.reportEntity.ProcessingTaskReportEntity;
import com.another.reportservice.entity.reportEntity.TaskPeriodReportEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExcelReportServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ExcelReportServiceTest.class);
    @Autowired
    private ExcelReportService excelReportService;

    @Test
    void createTaskReportExcel() throws IOException {
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
                        .completedTime("1")
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime("1")
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime("1")
                        .build());
                add(ProcessingTaskReportEntity.builder()
                        .topic("hello")
                        .closed(LocalDateTime.now())
                        .status(Status.OPEN)
                        .createDate(LocalDateTime.now())
                        .taskId(1L)
                        .workerUsername("hello")
                        .offToWorkTime(LocalDateTime.now())
                        .completedTime("1")
                        .build());
            }
        };
        //excelReportService.createTaskReportExcel(list, "Hello");
        log.info("test 123123: {}", list.getClass());
    }
}