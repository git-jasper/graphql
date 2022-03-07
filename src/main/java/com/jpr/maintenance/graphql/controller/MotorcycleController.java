package com.jpr.maintenance.graphql.controller;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.graphql.GraphQLUtils.saveEntity;
import static com.jpr.maintenance.graphql.exception.ThrowableHandlerProvider.handlerFunction;

@Controller
@RequiredArgsConstructor
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @QueryMapping
    public Mono<DataFetcherResult<MotorcycleEntity>> motorcycleById(@Argument final String id) {
        return motorcycleService.findById(Long.valueOf(id))
            .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }

    @MutationMapping
    public Mono<DataFetcherResult<MotorcycleEntity>> createMotorcycle(@Argument final MotorcycleInput motorcycleInput) {
        return Mono.just(motorcycleInput)
            .flatMap(MotorcycleEntity::of)
            .flatMap(e -> saveEntity(e, motorcycleService::save))
            .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }

    @MutationMapping
    public Mono<DataFetcherResult<Boolean>> deleteMotorcycle(@Argument final String id) {
        return motorcycleService.deleteById(Long.valueOf(id))
            .map(m -> GraphQLUtils.<Boolean>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }
}
