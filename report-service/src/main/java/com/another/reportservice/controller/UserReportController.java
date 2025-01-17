package com.another.reportservice.controller;

import com.another.reportservice.custom_exception.FutureDateException;
import com.another.reportservice.service.reportService.ReportUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Slf4j
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
                                            @PathVariable String userEmail) throws FutureDateException {
        reportUserService.getReportNumberOfRegisterUserPeriod(start, end, userEmail);
    }

    @GetMapping("/efficiency/{username}/{userEmail}")
    public ResponseEntity<?> getReportPerformerEfficiencyUser(@PathVariable String username,
                                                           @PathVariable String userEmail) {
        try {
            reportUserService.getReportPerformerEfficiencyUser(URLDecoder.decode(username, StandardCharsets.UTF_8), userEmail);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
