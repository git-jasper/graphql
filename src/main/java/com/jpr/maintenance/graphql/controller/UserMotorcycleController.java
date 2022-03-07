package com.jpr.maintenance.graphql.controller;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.database.service.UserMotorcycleService;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.graphql.GraphQLUtils.serviceCall;
import static com.jpr.maintenance.graphql.exception.ThrowableHandlerProvider.handlerFunction;

@Controller
@RequiredArgsConstructor
public class UserMotorcycleController {

    private final UserMotorcycleService userMotorcycleService;

    @MutationMapping
    public Mono<DataFetcherResult<UserMotorcycleEntity>> createUserMotorcycle(@Argument final UserMotorcycleInput userMotorcycleInput) {
        return Mono.just(userMotorcycleInput)
            .flatMap(e -> serviceCall(e, userMotorcycleService::save))
            .map(m -> GraphQLUtils.<UserMotorcycleEntity>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }
}
