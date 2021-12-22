package com.jpr.maintenance.graphql.datafetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.service.MotorcycleService;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.handler.ThrowableHandlerProvider;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.validation.errors.InputValidationException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.jpr.maintenance.graphql.GraphQLUtils.*;
import static com.jpr.maintenance.graphql.GraphQLUtils.successFun;
import static com.jpr.maintenance.graphql.handler.ThrowableHandlerProvider.handlerFunction;
import static com.jpr.maintenance.reflection.ObjectMapper.toObject;
import static com.jpr.maintenance.validation.errors.InputValidationError.INVALID_FIELD;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Configuration
public class MotorcycleDataFetchers {
    private final MotorcycleService motorcycleService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

//    @Bean
//    public DataFetcherWrapper<CompletableFuture<DataFetcherResult<MotorcycleEntity>>> createMotorcycle() {
//        return new DataFetcherWrapper<>(
//            "Mutation",
//            "createMotorcycle",
//            dataFetchingEnvironment ->
//                toObject(dataFetchingEnvironment.getArgument("motorcycleInput"), MotorcycleInput.class)
//                    .flatMap(MotorcycleEntity::of)
//                    .map(e -> saveEntity(e, motorcycleService::save))
//                    .fold(
//                        errorFutureFun(),
//                        successFutureFun()
//                    )
//        );
//    }

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

    private <T> Mono<T> deserializeToPojo(Map<String, Object> map, Class<T> clazz) {
        try {
            return Mono.just(OBJECT_MAPPER.convertValue(map, clazz));
        } catch (Exception ex) {
            return Mono.error(ex);
        }
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
