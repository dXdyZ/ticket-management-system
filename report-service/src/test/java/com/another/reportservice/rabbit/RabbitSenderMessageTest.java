package com.another.reportservice.rabbit;

import com.another.reportservice.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitSenderMessageTest {

    @Autowired
    private RabbitSenderMessage rabbitSenderMessage;

    @Test
    void sendMessageReport() {
        rabbitSenderMessage.sendMessageReport(new ArrayList<Task>(), "test", "hello@gmail.com");
    }
}