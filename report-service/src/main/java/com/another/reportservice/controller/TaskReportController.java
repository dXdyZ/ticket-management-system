package com.another.reportservice.controller;

import com.another.reportservice.service.reportService.ReportTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/tasks")
public class TaskReportController {
    private final ReportTaskService reportTaskService;

    @Autowired
    public TaskReportController(ReportTaskService reportTaskService) {
        this.reportTaskService = reportTaskService;
    }

    @GetMapping("/period/{start}/{end}/{userEmail}")
    public void getTaskReportByPeriod(@PathVariable String start,
                                                        @PathVariable String userEmail,
                                                        @PathVariable String end,
                                                        @RequestParam(name = "username", required = false) String username)
            throws ChangeSetPersister.NotFoundException, ExecutionException, InterruptedException {
        reportTaskService.getReportNumberOfCreateTaskPeriod(start, end, username, userEmail);
    }

    @GetMapping("/processing/{start}/{end}/{userEmail}")
    public void getTaskProcessingReport(@PathVariable String start,
                                    @PathVariable String end,
                                    @PathVariable String userEmail) {
        reportTaskService.getAndSendProcessingTaskReport(start, end, userEmail);
    }
}
