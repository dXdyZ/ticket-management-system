package com.another.ticket.rabbit;

import com.another.ticket.entity.Status;
import com.another.ticket.entity.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMessageTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMessageTest.class);
    @Autowired
    private RabbitMessage rabbitMessage;

    @Test
    void sendCreateTask() {
        List<Task> tasks = new ArrayList<>() {
            {
                add(Task.builder()
                        .id(1L)
                        .topic("hello tested rabbit")
                        .createDate(LocalDateTime.now())
                        .status(Status.OPEN)
                        .build());
            }};
        rabbitMessage.sendCreateTask(null);
    }

}