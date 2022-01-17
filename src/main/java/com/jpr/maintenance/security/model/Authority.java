package com.jpr.maintenance.security.model;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    NONE("ROLE_NONE"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
