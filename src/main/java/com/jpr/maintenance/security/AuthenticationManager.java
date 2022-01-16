package com.jpr.maintenance.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.util.Collections.singletonList;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
            .filter(a -> a.getPrincipal().equals("user"))
            .map(a -> new UsernamePasswordAuthenticationToken(a.getPrincipal(), null, singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    }
}
