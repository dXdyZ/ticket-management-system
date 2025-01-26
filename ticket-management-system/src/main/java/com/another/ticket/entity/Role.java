package com.another.ticket.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Role implements GrantedAuthority, Serializable {
    ROLE_CLIENT, ROLE_PERFORMER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
