package com.another.reportservice.service.reportService;


import com.another.reportservice.custom_exception.FutureDateException;
import com.another.reportservice.entity.Role;
import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.Users;
import com.another.reportservice.entity.reportEntity.PerformerEfficiencyReport;
import com.another.reportservice.entity.reportEntity.UserPeriodReportEntity;
import com.another.reportservice.rabbit.RabbitSenderMessage;
import com.another.reportservice.service.ExcelReportService;
import com.another.reportservice.service.repositoryService.MapDate;
import com.another.reportservice.service.repositoryService.TaskService;
import com.another.reportservice.service.repositoryService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportUserService {
    private final UserService userService;
    private final TaskService taskService;
    private final RabbitSenderMessage rabbitSenderMessage;
    private final ExcelReportService excelReportService;

    @Autowired
    public ReportUserService(UserService userService, TaskService taskService,
                             RabbitSenderMessage rabbitSenderMessage, ExcelReportService excelReportService) {
        this.userService = userService;
        this.taskService = taskService;
        this.rabbitSenderMessage = rabbitSenderMessage;
        this.excelReportService = excelReportService;
    }

    public void getReportNumberOfRegisterUserPeriod(String start, String end, String userEmail) throws FutureDateException {
        List<LocalDateTime> dates = MapDate.mapDate(start, end);
        List<Users> users = userService.getUserBetweenDate(dates.get(0).toLocalDate(), dates.get(1).toLocalDate());
        rabbitSenderMessage.sendMessageReport(excelReportService.createUserPeriodReportExcel(UserPeriodReportEntity.builder()
                .quantityUsers((long) (users.size() + 1))
                .registerUserByMonth(partitionUserByMonth(users))
                .registerUserByRoleMonth(partitionUserByRole(users))
                .build(), "Количество_зарегистрированных_пользователй_за:" + start + "_" + end),
                "Количество зарегистрированных пользователй за период", userEmail);
    }

    public void getReportPerformerEfficiencyUser(String username, String userEmail) throws NoSuchElementException {
        Users users = userService.getUserByUsername(username);
        List<Task> tasksByClosed = taskService.getTaskByClosedAndUsername(username);
        List<Task> taskByUser = taskService.getByUsername(username);
        double avgCompTime = getAVGCompletionTime(tasksByClosed).orElseThrow(() -> new NoSuchElementException("User has no completed tasks"));
        rabbitSenderMessage.sendMessageReport(excelReportService.createPerformerEfficiencyReportExcel(PerformerEfficiencyReport.builder()
                        .successfulImplementation((long) (tasksByClosed.size() - 1))
                        .AVGCompletionTime(avgCompTime)
                        .hiredTask((long) (taskByUser.size() - 1))
                        .userRating(getUserRating(avgCompTime, (tasksByClosed.size() - 1)))
                        .build(), "Эффективность_пользователя_" + username),
                "Эффективность пользователя " + username, userEmail);

    }

    private Integer getUserRating(double avgCompTime, int successImplTask) {
        double baseRating =  successImplTask / avgCompTime;

        double minRating = 0.1;
        double maxRating = 10;

        return (int) Math.min(10.0, Math.max(1.0, 1 + (baseRating - minRating) / (maxRating - minRating) * 9));
    }

    private OptionalDouble getAVGCompletionTime(List<Task> tasks) {
        return tasks.stream()
                .map(task -> Duration.between(task.getInJobDate(), task.getCloseDate()).toHours()
                        + (Duration.between(task.getInJobDate(), task.getCloseDate()).toMinutes() % 60) / 60.0)
                .mapToDouble(Double::doubleValue)
                .average();
    }

    private Map<Month, Long> partitionUserByMonth(List<Users> users) {
        return users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getCreateData().getMonth(),
                        Collectors.counting()
                ));
    }

    private Map<Role, Long> partitionUserByRole(List<Users> users) {
        return users.stream()
                .collect(Collectors.groupingBy(
                        Users::getRole,
                        Collectors.counting()
                ));
    }
}









