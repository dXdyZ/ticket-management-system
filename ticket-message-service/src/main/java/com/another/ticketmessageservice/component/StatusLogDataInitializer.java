package com.another.ticketmessageservice.component;

import com.another.ticketmessageservice.entity.*;
import com.another.ticketmessageservice.entity.bd_entity.StatusLog;
import com.another.ticketmessageservice.repository.StatusLogRepository;
import net.andreinc.mockneat.MockNeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
@Component
public class StatusLogDataInitializer implements CommandLineRunner {

    @Autowired
    private StatusLogRepository statusLogRepository;

    private static final int TOTAL_STATUS_LOGS = 1000;
    private static final int YEAR = 2025; // Укажите нужный год

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        MockNeat mockNeat = MockNeat.threadLocal();
        List<StatusLog> statusLogsList = new ArrayList<>(TOTAL_STATUS_LOGS);
        Random random = new Random();

        for (int i = 0; i < TOTAL_STATUS_LOGS; i++) {
            String id = mockNeat.strings().size(10).val(); // Генерация случайного ID

            // Генерация случайного месяца и дня
            int month = mockNeat.ints().range(1, 13).val(); // 1-12 месяц
            int day = mockNeat.ints().range(1, 29).val(); // 1-28 день

            LocalDate createDate = LocalDate.of(YEAR, month, day); // Генерация даты создания задачи

            // Создание объекта Users
            Users user = Users.builder()
                    .id(Long.valueOf(mockNeat.ints().range(1, 10000).val()))
                    .username(mockNeat.names().val())
                    .email(mockNeat.emails().val())
                    .password(mockNeat.strings().size(10).val())
                    .role(mockNeat.from(Role.class).val())
                    .createData(createDate)
                    .build();

            // Создание объекта Task
            Task task = Task.builder()
                    .id(Long.valueOf(mockNeat.ints().range(1, 10000).val()))
                    .topic(mockNeat.words().val())
                    .description(mockNeat.genders().val())
                    .priority(mockNeat.from(Priority.class).val())
                    .status(mockNeat.from(Status.class).val())
                    .createDate(createDate)
                    .investments(new byte[]{1, 2, 3, 4}) // Пример данных
                    .users(user)
                    .workUser(user) // Можно изменить на другого пользователя при необходимости
                    .build();

            // Генерация случайных дат для статусов вручную
            LocalDateTime setStatusOPEN = combineDateAndRandomTime(createDate);
            List<LocalDateTime> setStatusAWATIN_RESPONSE = new ArrayList<>();
            int awaitingResponses = mockNeat.ints().range(1, 4).val(); // 1-3 даты
            for (int j = 0; j < awaitingResponses; j++) {
                setStatusAWATIN_RESPONSE.add(combineDateAndRandomTime(setStatusOPEN.toLocalDate()));
            }

            List<LocalDateTime> setStatusIN_JOB = new ArrayList<>();
            int inJobDates = mockNeat.ints().range(1, 4).val(); // 1-3 даты
            for (int j = 0; j < inJobDates; j++) {
                setStatusIN_JOB.add(combineDateAndRandomTime(setStatusAWATIN_RESPONSE.get(setStatusAWATIN_RESPONSE.size() - 1).toLocalDate()));
            }

            LocalDateTime setStatusCLOSED = combineDateAndRandomTime(setStatusIN_JOB.get(setStatusIN_JOB.size() - 1).toLocalDate());

            // Создание объекта StatusLog
            StatusLog statusLog = StatusLog.builder()
                    .id(id)
                    .task(task)
                    .setStatusOPEN(setStatusOPEN)
                    .setStatusAWATIN_RESPONSE(setStatusAWATIN_RESPONSE)
                    .setStatusIN_JOB(setStatusIN_JOB)
                    .setStatusCLOSED(setStatusCLOSED)
                    .build();

            statusLogsList.add(statusLog);

            // Пакетное сохранение каждые 1000 объектов
            if (statusLogsList.size() == 1000) {
                statusLogRepository.saveAll(statusLogsList);
                statusLogsList.clear();
                System.out.println("Вставлено " + (i + 1) + " записей StatusLog...");
            }
        }

        // Сохранение оставшихся записей
        if (!statusLogsList.isEmpty()) {
            statusLogRepository.saveAll(statusLogsList);
            System.out.println("Вставлены оставшиеся записи StatusLog...");
        }

        System.out.println("Инициализация данных StatusLog завершена.");
    }

    private LocalDateTime combineDateAndRandomTime(LocalDate date) {
        Random random = new Random();
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        return LocalDateTime.of(date, LocalTime.of(hour, minute, second));
    }
}
 */
