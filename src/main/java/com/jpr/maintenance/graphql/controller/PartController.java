package com.jpr.maintenance.graphql.controller;

import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.service.PartService;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.PartInput;
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
public class PartController {

    private final PartService partService;

    @MutationMapping
    public Mono<DataFetcherResult<PartEntity>> createPart(@Argument final PartInput partInput) {
        return Mono.just(partInput)
            .flatMap(e -> serviceCall(e, partService::save))
            .map(m -> GraphQLUtils.<PartEntity>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }
}
