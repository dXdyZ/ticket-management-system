package com.another.reportservice.controller;

import com.another.reportservice.entity.reportEntity.UserPeriodReportEntity;
import com.another.reportservice.service.reportService.ReportUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

@RestController
public class UserReportController {
    private final ReportUserService reportUserService;

    @Autowired
    public UserReportController(ReportUserService reportUserService) {
        this.reportUserService = reportUserService;
    }

    @GetMapping("/{start}/{end}")
    public UserPeriodReportEntity getReport(@PathVariable String start,
                                            @PathVariable String end) throws ParseException, ExecutionException, InterruptedException {
        return reportUserService.getReportNumberOfRegisterUserPeriod(start, end);
    }
}
