package com.another.ticketmessageservice.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Long id;
    private String username;

    private String email;

    //Убираем это поле из ответов
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Task> task;

    private Role role;

    private Date createData;
}
