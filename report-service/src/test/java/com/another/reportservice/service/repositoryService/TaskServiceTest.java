package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class TaskServiceTest {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceTest.class);
    @Autowired
    private TaskService taskService;

    @Test
    @Transactional
    void test() {
        List<Task> list = taskService.getTaskByClosedAndUsername("client");
        list.forEach(System.out::println);
    }
}