package com.jpr.maintenance.security.config;

import graphql.ExecutionInput;
import graphql.Internal;
import graphql.spring.web.reactive.ExecutionInputCustomizer;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Primary
@Component
@Internal
public class SecurityExecutionInputCustomizer implements ExecutionInputCustomizer {

    @Override
    public Mono<ExecutionInput> customizeExecutionInput(ExecutionInput executionInput, ServerWebExchange serverWebExchange) {
        return serverWebExchange.getSession().map(webSession -> {
            SecurityContext securityContext = webSession.getAttribute("SPRING_SECURITY_CONTEXT");
            if (securityContext != null) {
                return executionInput.transform(b -> b.graphQLContext(Map.of(SecurityContext.class, securityContext)));
            }
            return executionInput;
        });
    }
}