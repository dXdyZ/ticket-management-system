package com.another.ticketmessageservice.entity.report_entity;

import com.another.ticketmessageservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingTaskReportEntity {
    private Long taskId;
    private String topic;
    private Status status;
    private String workerUsername;
    private LocalDateTime createDate;
    private LocalDateTime offToWorkTime;
    private LocalDateTime closed;
    private Integer completedTime;
}
