package com.jpr.maintenance.graphql.controller;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.service.UserService;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.FindUserInput;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import static com.jpr.maintenance.graphql.GraphQLUtils.serviceCall;
import static com.jpr.maintenance.graphql.exception.ThrowableHandlerProvider.handlerFunction;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @QueryMapping
    public Mono<DataFetcherResult<UserOutput>> findByUser(@Argument final FindUserInput findUserInput) {
        return Mono.just(findUserInput)
            .flatMap(e -> serviceCall(e, userService::findByUserName))
            .map(m -> GraphQLUtils.<UserOutput>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }

    @MutationMapping
    public Mono<DataFetcherResult<UserOutput>> createUser(@Argument final UserInput userInput) {
        return Mono.just(userInput)
            .flatMap(User::of)
            .flatMap(UserEntity::of)
            .flatMap(e -> serviceCall(e, userService::save))
            .map(m -> GraphQLUtils.<UserOutput>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }

    @MutationMapping
    public Mono<DataFetcherResult<Boolean>> deleteUser(@Argument final String id) {
        return userService.deleteById(Long.valueOf(id))
            .map(m -> GraphQLUtils.<Boolean>successFun().apply(m))
            .onErrorResume(handlerFunction());
    }
}
