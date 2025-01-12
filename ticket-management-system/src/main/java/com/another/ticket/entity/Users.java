package com.another.ticket.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 40, message = "User name must maximum 40 character")
    private String username;

    @NotNull
    @Email
    private String email;

    //Убираем это поле из ответов
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> task;

    @Enumerated(EnumType.STRING)
    private Role role;


    private LocalDateTime createData;
}
