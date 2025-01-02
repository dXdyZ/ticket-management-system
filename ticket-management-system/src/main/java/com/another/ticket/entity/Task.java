package com.another.ticket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 120, message = "Topic name must maximum 120 character")
    private String topic;

    @NotNull
    @Size(min = 10, message = "Description must minimum 10 character")
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.DATE)
    private Date createDate;

    private byte[] investments;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "work_user_id")
    private Users workUser;
}
