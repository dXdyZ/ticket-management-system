package com.another.reportservice.controller;

import com.another.reportservice.entity.reportEntity.TaskPeriodReportEntity;
import com.another.reportservice.service.reportService.ReportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskReportController {
    private final ReportTaskService reportTaskService;

    @Autowired
    public TaskReportController(ReportTaskService reportTaskService) {
        this.reportTaskService = reportTaskService;
    }

    @GetMapping("/{start}/{end}")
    public TaskPeriodReportEntity getTaskReportByPeriod(@PathVariable String start,
                                                        @PathVariable String end,
                                                        @RequestParam(name = "username", required = false) String username)
            throws ChangeSetPersister.NotFoundException, ExecutionException, InterruptedException {
        return reportTaskService.getReportNumberOfCreateTaskPeriod(start, end, username);
    }
}
