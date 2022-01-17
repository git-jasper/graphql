package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.FindUserInput;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.security.model.Authority;
import com.jpr.maintenance.security.model.SecuredDataFetcher;
import com.jpr.maintenance.service.UserService;
import com.jpr.maintenance.validation.model.User;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.serviceCall;
import static com.jpr.maintenance.graphql.exception.ThrowableHandlerProvider.handlerFunction;
import static com.jpr.maintenance.util.ReflectionUtil.deserializeToPojo;

@RequiredArgsConstructor
@Configuration
public class UserDataFetchers {
    private final UserService userService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserOutput>>> getUser() {
        return new DataFetcherWrapper<>(
            "Query",
            "findByUser",
            new SecuredDataFetcher<>(Authority.USER,
                dataFetchingEnvironment ->
                    deserializeToPojo(dataFetchingEnvironment.getArgument("findUserInput"), FindUserInput.class)
                        .flatMap(e -> serviceCall(e, userService::findByUserName))
                        .map(m -> GraphQLUtils.<UserOutput>successFun().apply(m))
                        .onErrorResume(handlerFunction())
                        .toFuture()
            )
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserOutput>>> createUser() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createUser",
            dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("userInput"), UserInput.class)
                    .flatMap(User::of)
                    .flatMap(UserEntity::of)
                    .flatMap(e -> serviceCall(e, userService::save))
                    .map(m -> GraphQLUtils.<UserOutput>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<Boolean>>> deleteUser() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteUser",
            dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return userService.deleteById(Long.valueOf(id))
                    .map(m -> GraphQLUtils.<Boolean>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture();
            }
        );
    }
}
