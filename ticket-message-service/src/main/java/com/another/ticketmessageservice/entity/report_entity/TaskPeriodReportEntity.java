package com.another.ticketmessageservice.entity.report_entity;

import com.another.ticketmessageservice.entity.Priority;
import com.another.ticketmessageservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPeriodReportEntity {
    private Long quantityTask;
    private Map<Month, Long> createTaskByMonth;
    private Map<Priority, Long> createTaskByPriority;
    private Map<Status, Long> getNowTaskByStatus;
}
