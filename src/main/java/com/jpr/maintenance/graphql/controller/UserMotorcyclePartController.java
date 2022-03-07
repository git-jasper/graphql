package com.jpr.maintenance.graphql.controller;

import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import com.jpr.maintenance.database.service.UserMotorcyclePartService;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
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
public class UserMotorcyclePartController {

    private final UserMotorcyclePartService userMotorcyclePartService;

    @MutationMapping
    public Mono<DataFetcherResult<UserMotorcyclePartEntity>> createUserMotorcyclePart(@Argument final UserMotorcyclePartInput userMotorcyclePartInput) {
        return Mono.just(userMotorcyclePartInput)
            .flatMap(e -> serviceCall(e, userMotorcyclePartService::save))
            .map(m -> GraphQLUtils.<UserMotorcyclePartEntity>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }
}
