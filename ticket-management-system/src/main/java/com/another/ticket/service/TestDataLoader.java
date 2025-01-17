package com.another.ticket.service;

import com.another.ticket.entity.*;
import com.another.ticket.repository.TaskRepository;
import com.another.ticket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataLoader(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Создание пользователей
        Users client = Users.builder()
                .username("client")
                .email("admin@admin.com")
                .password(passwordEncoder.encode("client"))
                .role(Role.ROLE_CLIENT)
                .createData(LocalDateTime.now())
                .build();

        Users performer = Users.builder()
                .username("performer")
                .email("performer@admin.com")
                .password(passwordEncoder.encode("performer"))
                .role(Role.ROLE_PERFORMER)
                .createData(LocalDateTime.now())
                .build();

        Users admin = Users.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ROLE_ADMIN)
                        .createData(LocalDateTime.now())
                        .build();

        userRepository.saveAll(List.of(client, performer, admin));

        // Создание задач с различными датами
        List<Task> tasks = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 50; i++) {
            LocalDateTime createDate = LocalDateTime.now().plusDays(random.nextInt(365));
            LocalDateTime inJobDate = createDate.plusDays(random.nextInt(10) + 1);
            LocalDateTime closeDate = inJobDate.plusDays(random.nextInt(10) + 1);

            Task task = Task.builder()
                    .topic("Task " + i)
                    .description("Description for task " + i)
                    .priority(Priority.values()[random.nextInt(Priority.values().length)])
                    .status(Status.CLOSED)
                    .createDate(createDate)
                    .inJobDate(inJobDate)
                    .closeDate(closeDate)
                    .users(client)
                    .workUser(performer)
                    .build();

            tasks.add(task);
        }

        taskRepository.saveAll(tasks);
    }
}
*/