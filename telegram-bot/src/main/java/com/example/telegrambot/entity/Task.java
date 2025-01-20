package com.example.telegrambot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private Long id;
    private String topic;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

    private LocalDateTime inJobDate;

    private LocalDateTime closeDate;

    private byte[] investments;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "work_user_id")
    private Users workUser;
}
