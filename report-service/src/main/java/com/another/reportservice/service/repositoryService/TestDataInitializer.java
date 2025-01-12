package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.*;
import com.another.reportservice.repository.TaskRepository;
import com.another.reportservice.repository.UserRepository;
import net.andreinc.mockneat.MockNeat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
@Component
public class TestDataInitializer implements CommandLineRunner {

    private final MockNeat mock = MockNeat.threadLocal();
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TestDataInitializer(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        // Генерация пользователей
        List<Users> usersList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Users user = Users.builder()
                    .username(mock.names().full().get())
                    .email(mock.emails().get())
                    .password(mock.strings().size(10).get())
                    .role(mock.from(Role.class).get())
                    .createData(LocalDate.now().minusDays(mock.ints().range(0, 365).get()))
                    .build();
            usersList.add(user);
        }
        userRepository.saveAll(usersList);

        // Генерация задач
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Users createUser = mock.from(usersList).get();
            Users workUser = mock.from(usersList).get();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createDate = now.minusDays(mock.ints().range(1, 30).get());
            LocalDateTime inJobDate = createDate.plusDays(mock.ints().range(1, 5).get());
            LocalDateTime closeDate = inJobDate.plusDays(mock.ints().range(1, 3).get());

            Task task = Task.builder()
                    .topic(mock.strings().size(10).get())
                    .description(mock.strings().size(50).get())
                    .priority(mock.from(Priority.class).get())
                    .status(mock.from(Status.class).get())
                    .createDate(createDate)
                    .inJobDate(inJobDate)
                    .closeDate(closeDate)
                    .users(createUser)
                    .workUser(workUser)
                    .build();
            tasks.add(task);
        }
        taskRepository.saveAll(tasks);

        // Связывание задач с пользователями
        usersList.forEach(user -> {
            List<Task> userTasks = mock.from(tasks).list(5).get();
            user.setTask(userTasks);
        });

        userRepository.saveAll(usersList);

        System.out.println("Тестовые данные успешно сгенерированы!");
    }
}
 */
