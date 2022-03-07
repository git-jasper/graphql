package com.jpr.maintenance.security;


import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static com.jpr.maintenance.security.Authority.NONE;

@Slf4j
@EqualsAndHashCode
public class AnonymousAuthentication implements Authentication {

    public AnonymousAuthentication() {
      log.info("****** INIT ANONYMOUS AUTHENTICATION");
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(NONE);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "anonymous";
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
    }

    @Override
    public String getName() {
        return "anonymous";
    }
}
