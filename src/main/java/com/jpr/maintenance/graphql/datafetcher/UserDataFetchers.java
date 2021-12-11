package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.service.UserService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.model.User;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.errorFutureFun;
import static com.jpr.maintenance.graphql.GraphQLUtils.foldToFutureFun;
import static com.jpr.maintenance.graphql.GraphQLUtils.serviceCallEither;
import static com.jpr.maintenance.graphql.GraphQLUtils.successFutureFun;
import static com.jpr.maintenance.reflection.ObjectMapper.toObject;

@RequiredArgsConstructor
@Configuration
public class UserDataFetchers {
    private final UserService userService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserOutput>>> getUser() {
        return new DataFetcherWrapper<>(
            "Query",
            "userByInput",
            dataFetchingEnvironment ->
                toObject(dataFetchingEnvironment.getArgument("userInput"), UserInput.class)
                    .flatMap(User::of)
                    .flatMap(e -> serviceCallEither(e, userService::findByUser, dataFetchingEnvironment))
                    .fold(
                        errorFutureFun(),
                        foldToFutureFun()
                    )
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserOutput>>> createUser() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createUser",
            dataFetchingEnvironment ->
                toObject(dataFetchingEnvironment.getArgument("userInput"), UserInput.class)
                    .flatMap(User::of)
                    .flatMap(UserEntity::of)
                    .flatMap(e -> GraphQLUtils.serviceCall(e, userService::save, dataFetchingEnvironment))
                    .fold(
                        errorFutureFun(),
                        successFutureFun()
                    )
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<Boolean>>> deleteUser() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteUser",
            dataFetchingEnvironment ->
                toObject(dataFetchingEnvironment.getArgument("userInput"), UserInput.class)
                    .flatMap(User::of)
                    .flatMap(e -> GraphQLUtils.serviceCall(e, userService::deleteByUser, dataFetchingEnvironment))
                    .fold(
                        errorFutureFun(),
                        successFutureFun()
                    )
        );
    }
}
