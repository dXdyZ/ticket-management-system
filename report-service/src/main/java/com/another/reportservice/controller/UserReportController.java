package com.another.reportservice.controller;

import com.another.reportservice.service.reportService.ReportUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/users")
public class UserReportController {
    private final ReportUserService reportUserService;

    @Autowired
    public UserReportController(ReportUserService reportUserService) {
        this.reportUserService = reportUserService;
    }

    @GetMapping("/period/{start}/{end}/{userEmail}")
    public void getReportCreateUserPeriod(@PathVariable String start,
                                            @PathVariable String end,
                                            @PathVariable String userEmail) {
        reportUserService.getReportNumberOfRegisterUserPeriod(start, end, userEmail);
    }

    @GetMapping("/efficiency/{username}/{userEmail}")
    public void getReportPerformerEfficiencyUser(@PathVariable String username,
                                                 @PathVariable String userEmail) {
        reportUserService.getReportPerformerEfficiencyUser(URLDecoder.decode(username, StandardCharsets.UTF_8), userEmail);
    }
}
