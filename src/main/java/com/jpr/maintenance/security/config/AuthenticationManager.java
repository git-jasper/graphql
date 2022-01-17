package com.jpr.maintenance.security.config;

import com.jpr.maintenance.security.model.AnonymousAuthentication;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.security.model.Authority.USER;
import static java.util.Collections.singletonList;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
            .filter(a -> a.getPrincipal().equals("user"))
            .map(a -> (Authentication) new UsernamePasswordAuthenticationToken(a.getPrincipal(), null, singletonList(USER)))
            .switchIfEmpty(Mono.defer(() -> Mono.just(new AnonymousAuthentication())));
    }
}
