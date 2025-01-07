package com.another.ticketmessageservice.controller;

import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import com.another.ticketmessageservice.service.StatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get-status-data")
public class GetStatusLogDataController {
    private final StatusLogService statusLogService;

    @Autowired
    public GetStatusLogDataController(StatusLogService statusLogService) {
        this.statusLogService = statusLogService;
    }

    @GetMapping("/{start}/{end}")
    public List<StatusLog> getStatusLogData(@PathVariable String start,
                                            @PathVariable String end) {
        return statusLogService.getStatusLogByTaskTimeProcessing(start, end);
    }
}
