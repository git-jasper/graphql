package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.errorFutureFun;
import static com.jpr.maintenance.graphql.GraphQLUtils.saveEntity;
import static com.jpr.maintenance.graphql.GraphQLUtils.successFutureFun;
import static com.jpr.maintenance.reflection.ObjectMapper.toObject;

@RequiredArgsConstructor
@Configuration
public class MotorcycleDataFetchers {
    private final MotorcycleService motorcycleService;

    @Bean
    public DataFetcherWrapper<CompletableFuture<MotorcycleEntity>> getMotorcycleById() {
        return new DataFetcherWrapper<>(
            "Query",
            "motorcycleById",
            dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.findById(Long.valueOf(id)).toFuture();
            }
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<MotorcycleEntity>>> createMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createMotorcycle",
            dataFetchingEnvironment ->
                toObject(dataFetchingEnvironment.getArgument("motorcycleInput"), MotorcycleInput.class)
                    .flatMap(MotorcycleEntity::of)
                    .flatMap(e -> saveEntity(e, motorcycleService::save, dataFetchingEnvironment))
                    .fold(
                        errorFutureFun(),
                        successFutureFun()
                    )
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<Boolean>> deleteMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteMotorcycle",
            dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.deleteById(Long.valueOf(id)).toFuture();
            }
        );
    }
}
