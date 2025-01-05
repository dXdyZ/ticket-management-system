package com.another.ticketmessageservice.entity.bd_entity;

import com.another.ticketmessageservice.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "status_log")
public class StatusLog {
    @Id
    private String id;
    private Task task;
    private LocalDateTime setStatusOPEN;
    private List<LocalDateTime> setStatusAWATIN_RESPONSE;
    private List<LocalDateTime> setStatusIN_JOB;
    private LocalDateTime setStatusCLOSED;
}
