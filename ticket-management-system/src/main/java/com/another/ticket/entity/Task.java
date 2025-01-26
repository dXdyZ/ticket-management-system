package com.another.ticket.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Comment> comment;
}
