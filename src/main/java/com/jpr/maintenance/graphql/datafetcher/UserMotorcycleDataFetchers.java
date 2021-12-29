package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.database.service.UserMotorcycleService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
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
public class UserMotorcycleDataFetchers {
    private final UserMotorcycleService userMotorcycleService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserMotorcycleEntity>>> createUserMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createUserMotorcycle",
            dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("userMotorcycleInput"), UserMotorcycleInput.class)
                    .flatMap(e -> serviceCall(e, userMotorcycleService::save))
                    .map(m -> GraphQLUtils.<UserMotorcycleEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
        );
    }
}
