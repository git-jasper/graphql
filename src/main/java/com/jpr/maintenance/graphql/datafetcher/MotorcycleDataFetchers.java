package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.saveEntity;
import static com.jpr.maintenance.graphql.datafetcher.DataFetcherUtil.deserializeToPojo;
import static com.jpr.maintenance.graphql.handler.ThrowableHandlerProvider.handlerFunction;

@RequiredArgsConstructor
@Configuration
public class MotorcycleDataFetchers {
    private final MotorcycleService motorcycleService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<MotorcycleEntity>>> getMotorcycleById() {
        return new DataFetcherWrapper<>(
            "Query",
            "motorcycleById",
            dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.findById(Long.valueOf(id))
                    .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture();
            }
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<MotorcycleEntity>>> createMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createMotorcycle",
            dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("motorcycleInput"), MotorcycleInput.class)
                    .flatMap(MotorcycleEntity::ofReactive)
                    .flatMap(e -> saveEntity(e, motorcycleService::save))
                    .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<Boolean>>> deleteMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteMotorcycle",
            dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.deleteById(Long.valueOf(id))
                    .map(m -> GraphQLUtils.<Boolean>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture();
            }
        );
    }
}
