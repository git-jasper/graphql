package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.UserMotorcyclePartEntity;
import com.jpr.maintenance.database.service.UserMotorcyclePartService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
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
public class UserMotorcyclePartDataFetchers {
    private final UserMotorcyclePartService userMotorcyclePartService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<UserMotorcyclePartEntity>>> createUserMotorcyclePart() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createUserMotorcyclePart",
            dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("userMotorcyclePartInput"), UserMotorcyclePartInput.class)
                    .flatMap(e -> serviceCall(e, userMotorcyclePartService::save))
                    .map(m -> GraphQLUtils.<UserMotorcyclePartEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
        );
    }
}
