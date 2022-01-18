package com.jpr.maintenance.security.config;

import com.jpr.maintenance.security.model.AnonymousAuthentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
            .flatMap(ex -> Mono.justOrEmpty(ex.getRequest().getHeaders().get("Authorization")))
            .map(h -> (Authentication) new UsernamePasswordAuthenticationToken(h.get(0), "pass"))
            .switchIfEmpty(Mono.defer(() -> Mono.just(new AnonymousAuthentication())));
    }
}
