package com.another.ticket.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CLIENT, ROLE_PERFORMER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
