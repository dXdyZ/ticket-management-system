package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.*;
import com.another.reportservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.andreinc.mockneat.MockNeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository usersRepository;

    private static final int TOTAL_USERS = 10000;
    private static final int YEAR = 2025; // Укажите нужный год

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        MockNeat mockNeat = MockNeat.threadLocal();

        List<Users> usersList = new ArrayList<>(1000);
        Random random = new Random();

        for (int i = 0; i < TOTAL_USERS; i++) {
            String username = mockNeat.names().val();
            String email = mockNeat.emails().val();
            String password = mockNeat.strings().size(10).val();

            // Случайный выбор роли
            Role role = mockNeat.from(Role.class).val();

            // Генерация случайного месяца и дня
            int month = mockNeat.ints().range(1, 13).val(); // 1-12
            int day = mockNeat.ints().range(1, 29).val(); // 1-28 для избежания некорректных дат
            LocalDate localDate = LocalDate.of(YEAR, month, day);
            LocalDate localDateForTask = LocalDate.of(YEAR, month, day);

            // Создание пользователя без задач
            Users user = Users.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .role(role)
                    .createData(localDate)
                    .build();

            // Создание и привязка задач
            List<Task> tasks = generateTasks(mockNeat, localDateForTask, user);
            user.setTask(tasks);

            usersList.add(user);

            // Пакетное сохранение каждые 1000 пользователей
            if (usersList.size() == 1000) {
                usersRepository.saveAll(usersList);
                usersList.clear();
                System.out.println("Вставлено " + (i + 1) + " пользователей...");
            }
        }

        // Сохранение оставшихся пользователей
        if (!usersList.isEmpty()) {
            usersRepository.saveAll(usersList);
            System.out.println("Вставлено оставшиеся пользователи...");
        }

        System.out.println("Инициализация данных завершена.");
    }

    private List<Task> generateTasks(MockNeat mockNeat, LocalDate date, Users user) {
        int taskCount = mockNeat.ints().range(0, 5).val(); // 0-4 задачи
        List<Task> tasks = new ArrayList<>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            String taskName = mockNeat.words().val();
            String taskDescription = mockNeat.genders().val();
            Priority priority = mockNeat.from(Priority.class).val();
            Status status = mockNeat.from(Status.class).val();

            Task task = Task.builder()
                    .createDate(date)
                    .topic(taskName)
                    .priority(priority)
                    .status(status)
                    .description(taskDescription)
                    .users(user) // Привязка задачи к пользователю
                    .build();
            tasks.add(task);
        }
        return tasks;
    }
}
*/
