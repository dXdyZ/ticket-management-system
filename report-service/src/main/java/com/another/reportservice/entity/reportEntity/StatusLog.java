package com.another.reportservice.entity.reportEntity;


import com.another.reportservice.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusLog {
    private String id;
    private Task task;
    private LocalDateTime setStatusOPEN;
    private List<LocalDateTime> setStatusAWATIN_RESPONSE;
    private List<LocalDateTime> setStatusIN_JOB;
    private LocalDateTime setStatusCLOSED;
}
