package com.another.reportservice.service.reportService;

import com.another.reportservice.entity.Priority;
import com.another.reportservice.entity.Status;
import com.another.reportservice.entity.Task;
import com.another.reportservice.entity.reportEntity.TaskPeriodReportEntity;
import com.another.reportservice.service.repositoryService.MapDate;
import com.another.reportservice.service.repositoryService.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ReportTaskService {
    private final TaskService taskService;

    @Autowired
    public ReportTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    public TaskPeriodReportEntity getReportNumberOfRegisterUserPeriod(String start, String end, String username) throws ChangeSetPersister.NotFoundException, ExecutionException, InterruptedException {
        List<LocalDate> dates = MapDate.mapDate(start, end);
        if (username == null) {
            List<Task> tasks = taskService.getTaskBetweenDate(dates.get(0), dates.get(1));
            return TaskPeriodReportEntity.builder()
                    .quantityTask(Long.valueOf(tasks.size() + 1))
                    .createTaskByMonth(getTaskCountsByMonth(dates.get(0), dates.get(1)))
                    .createTaskByPriority(getTaskCountsByPriority(dates.get(0), dates.get(1)))
                    .getNowTaskByStatus(getTaskCountsByStatus(dates.get(0), dates.get(1)))
                    .build();
        }
        List<Task> tasks = taskService.getTaskByUsername(username);
        return TaskPeriodReportEntity.builder()
                .quantityTask(Long.valueOf(tasks.size() + 1))
                .createTaskByMonth(getTaskCountsByMonth(dates.get(0), dates.get(1)))
                .createTaskByPriority(getTaskCountsByPriority(dates.get(0), dates.get(1)))
                .getNowTaskByStatus(getTaskCountsByStatus(dates.get(0), dates.get(1)))
                .build();
    }

    public Map<Month, Long> getTaskCountsByMonth(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = taskService.getTaskCountsByMonth(startDate, endDate);

        Map<Month, Long> tasksByMonth = new EnumMap<>(Month.class);
        for (Object[] result : results) {
            Integer monthNumber = ((Number) result[0]).intValue();
            Long count = ((Number) result[1]).longValue();
            tasksByMonth.put(Month.of(monthNumber), count);
        }
        return tasksByMonth;
    }

    public Map<Priority, Long> getTaskCountsByPriority(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = taskService.getTaskCountsByPriority(startDate, endDate);

        Map<Priority, Long> tasksByPriority = new EnumMap<>(Priority.class);
        for (Object[] result : results) {
            Priority priority = Priority.valueOf((String) result[0]);
            Long count = ((Number) result[1]).longValue();
            tasksByPriority.put(priority, count);
        }
        return tasksByPriority;
    }

    public Map<Status, Long> getTaskCountsByStatus(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = taskService.getTaskCountsByStatus(startDate, endDate);

        // Преобразуем результат в Map<Status, Long>
        Map<Status, Long> tasksByStatus = new EnumMap<>(Status.class);
        for (Object[] result : results) {
            Status status = Status.valueOf((String) result[0]);
            Long count = ((Number) result[1]).longValue();
            tasksByStatus.put(status, count);
        }
        return tasksByStatus;
    }
}
