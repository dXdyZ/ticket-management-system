package com.another.ticketmessageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String topic;
    private String description;

    private Priority priority;

    private Status status;

    private Date createDate;

    private byte[] investments;

    private Users users;

    private Users workUser;
}
