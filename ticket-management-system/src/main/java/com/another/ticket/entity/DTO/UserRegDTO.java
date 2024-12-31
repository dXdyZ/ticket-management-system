package com.another.ticket.entity.DTO;

import com.another.ticket.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegDTO {
    private String username;
    private String email;
    private String password;
    private Role role;
}
