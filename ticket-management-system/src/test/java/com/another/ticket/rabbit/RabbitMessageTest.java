package com.another.ticket.rabbit;

import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMessageTest {

    @Autowired
    private RabbitMessage rabbitMessage;

    @Test
    void sendCreateTask() {
        List<Task> tasks = new ArrayList<>() {
            {
                add(Task.builder()
                        .id(1L)
                        .topic("hello tested rabbit")
                        .createDate(new Date())
                        .status(Status.OPEN)
                        .build());
            }};
        rabbitMessage.sendCreateTask(null);
    }
}