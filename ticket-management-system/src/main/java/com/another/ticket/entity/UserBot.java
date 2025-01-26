package com.another.ticket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @OneToOne
    private Users users;

    private String token;
}
