package com.jpr.maintenance.graphql.controller;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GraphQLController {
    private final GraphQL graphQL;

    @PostMapping(path = "/graphql")
    public Mono<Map<String, Object>> graphql(@RequestBody GraphQLRequest graphQLRequest) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
            .query(graphQLRequest.getQuery())
            .variables(graphQLRequest.getVariables())
            .operationName(graphQLRequest.getOperationName())
            .build();

        ExecutionResult result = graphQL.execute(executionInput);

        return Mono.just(result.toSpecification());
    }
}
