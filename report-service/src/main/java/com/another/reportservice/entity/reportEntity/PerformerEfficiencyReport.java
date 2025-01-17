package com.another.reportservice.entity.reportEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformerEfficiencyReport {
    private Long successfulImplementation;
    private Long hiredTask;
    private Double AVGCompletionTime;
    private Integer userRating;
}
