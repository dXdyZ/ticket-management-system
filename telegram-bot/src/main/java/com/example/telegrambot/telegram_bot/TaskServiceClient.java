package com.example.telegrambot.telegram_bot;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("ticket-management-system")
public interface TaskServiceClient {

    //Добавить заголовок авторизации
    @PostMapping("/tasks/taskAcceptanceConfirmation/{id}")
    void confirmTask(@PathVariable("id") Long id);
}
