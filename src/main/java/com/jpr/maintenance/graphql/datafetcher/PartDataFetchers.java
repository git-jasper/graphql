package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.service.PartService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.PartInput;
import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
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
public class PartDataFetchers {
    private final PartService partService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<PartEntity>>> createPart() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createPart",
            dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("partInput"), PartInput.class)
                    .flatMap(e -> serviceCall(e, partService::save))
                    .map(m -> GraphQLUtils.<PartEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
        );
    }
}
