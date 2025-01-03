package com.another.ticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MiniController {

    @GetMapping("/conf-task/{id}")
    public String confirmationTask(@PathVariable String id) {
        return "confirmation_task";
    }
}
