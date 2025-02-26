package com.another.ticket.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestReportDTO {
    private String username;
    private String email;
    private Long chatId;
    private String start;
    private String end;
}
