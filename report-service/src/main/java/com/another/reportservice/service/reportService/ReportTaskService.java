package com.another.reportservice.service.reportService;

import com.another.reportservice.entity.Priority;
import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.reportEntity.ProcessingTaskReportEntity;
import com.another.reportservice.entity.reportEntity.TaskPeriodReportEntity;
import com.another.reportservice.rabbit.RabbitSenderMessage;
import com.another.reportservice.service.ExcelReportService;
import com.another.reportservice.service.repositoryService.MapDate;
import com.another.reportservice.service.repositoryService.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportTaskService {
    private final TaskService taskService;
    private final RabbitSenderMessage rabbitSenderMessage;
    private final ExcelReportService excelReportService;

    @Autowired
    public ReportTaskService(TaskService taskService, RabbitSenderMessage rabbitSenderMessage, ExcelReportService excelReportService) {
        this.taskService = taskService;
        this.rabbitSenderMessage = rabbitSenderMessage;
        this.excelReportService = excelReportService;
    }

    public void getAndSendProcessingTaskReport(String start, String end, String userEmail) {
        List<LocalDateTime> date = MapDate.mapDate(start, end);
        List<Task> tasks = taskService.getTaskBetweenDate(date.get(0), date.get(1));
        rabbitSenderMessage.sendMessageReport(excelReportService.createTaskProcessingReportExcel(getProcessingTask(tasks),
                        "Время_обработки_заявок_за_период:" + start +  "_" + end),
                "Время обработок заявок", userEmail);

    }

    private List<ProcessingTaskReportEntity> getProcessingTask(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    ProcessingTaskReportEntity pr = new ProcessingTaskReportEntity();
                    pr.setTaskId(task.getId());
                    pr.setTopic(task.getTopic());
                    pr.setStatus(task.getStatus());
                    pr.setWorkerUsername(task.getWorkUser().getUsername());
                    pr.setCreateDate(task.getCreateDate());
                    pr.setOffToWorkTime(task.getInJobDate());
                    pr.setClosed(task.getCloseDate());
                    pr.setCompletedTime(String.format("%02d:%02d",
                            Duration.between(pr.getOffToWorkTime(), pr.getClosed()).toHours(),
                            Duration.between(pr.getOffToWorkTime(), pr.getClosed()).toMinutes() % 60));
                    return pr;
                })
                .toList();
    }

    public void getReportNumberOfCreateTaskPeriod(String start, String end, String username, String userEmail) throws ChangeSetPersister.NotFoundException {
        List<LocalDateTime> dates = MapDate.mapDate(start, end);
        if (username == null) {
            List<Task> tasks = taskService.getTaskBetweenDate(dates.get(0), dates.get(1));
            rabbitSenderMessage.sendMessageReport(excelReportService.createTaskPeriodReportExcel(TaskPeriodReportEntity.builder()
                    .quantityTask(Long.valueOf(tasks.size() + 1))
                    .createTaskByMonth(getTaskCountsByMonth(tasks))
                    .createTaskByPriority(getTaskCountsByPriority(tasks))
                    .getNowTaskByStatus(getTaskCountsByStatus(tasks))
                    .build(), "Количество_заявок_за_период " + start + "_" + end),
                    "Количество заявок за период", userEmail);
        } else {
            List<Task> tasks = taskService.getTaskByUsername(username, dates.get(0), dates.get(1));
            rabbitSenderMessage.sendMessageReport(excelReportService.createTaskPeriodReportExcel(TaskPeriodReportEntity.builder()
                            .quantityTask(Long.valueOf(tasks.size() + 1))
                            .createTaskByMonth(getTaskCountsByMonth(tasks))
                            .createTaskByPriority(getTaskCountsByPriority(tasks))
                            .getNowTaskByStatus(getTaskCountsByStatus(tasks))
                            .build(), "Количество_заявок_за_период_пользователя_" + username),
                    "Количество заявок за период пользователя: " + username, userEmail);
        }
    }

    private Map<Month, Long> getTaskCountsByMonth(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getCreateDate().getMonth(),
                        Collectors.counting()
                ));
    }

    private Map<Priority, Long> getTaskCountsByPriority(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getPriority,
                        Collectors.counting()
                ));
    }

    private Map<Status, Long> getTaskCountsByStatus(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getStatus,
                        Collectors.counting()
                ));
    }
}
