package com.jpr.maintenance.graphql;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.*;
import static com.jpr.maintenance.reflection.ObjectMapper.toObject;

@RequiredArgsConstructor
@Configuration
public class GraphQLDataFetchers {
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
    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<MotorcycleEntity>>> createTaskDetails() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createMotorcycle",
            dataFetchingEnvironment ->
                toObject(dataFetchingEnvironment.getArgument("motorcycleInput"), MotorcycleInput.class)
                    .flatMap(MotorcycleEntity::of)
                    .flatMap(e -> saveEntity(e, motorcycleService::save, dataFetchingEnvironment))
                    .fold(
                        errorFun(),
                        successFun()
                    )
        );
    }

    @Bean
    public DataFetcherWrapper<CompletableFuture<Void>> deleteTaskDetails() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteMotorcycle",
            dataFetchingEnvironment -> {
                String task_id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.deleteById(Long.valueOf(task_id)).toFuture();
            }
        );
    }
}
