package com.jpr.maintenance.graphql.datafetcher;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.graphql.DataFetcherWrapper;
import com.jpr.maintenance.graphql.GraphQLUtils;
import com.jpr.maintenance.graphql.model.MotorcycleInput;
import com.jpr.maintenance.security.model.Authority;
import com.jpr.maintenance.security.model.SecuredDataFetcher;
import com.jpr.maintenance.service.MotorcycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jpr.maintenance.graphql.GraphQLUtils.saveEntity;
import static com.jpr.maintenance.graphql.exception.ThrowableHandlerProvider.handlerFunction;
import static com.jpr.maintenance.util.ReflectionUtil.deserializeToPojo;

@RequiredArgsConstructor
@Configuration
public class MotorcycleDataFetchers {
    private final MotorcycleService motorcycleService;

    @Bean
    public DataFetcherWrapper<MotorcycleEntity> getMotorcycleById() {
        return new DataFetcherWrapper<>(
            "Query",
            "motorcycleById",
            new SecuredDataFetcher<>(Authority.USER, dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.findById(Long.valueOf(id))
                    .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture();
            })
        );
    }

    @Bean
    public DataFetcherWrapper<MotorcycleEntity> createMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "createMotorcycle",
            new SecuredDataFetcher<>(Authority.USER, dataFetchingEnvironment ->
                deserializeToPojo(dataFetchingEnvironment.getArgument("motorcycleInput"), MotorcycleInput.class)
                    .flatMap(MotorcycleEntity::of)
                    .flatMap(e -> saveEntity(e, motorcycleService::save))
                    .map(m -> GraphQLUtils.<MotorcycleEntity>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture()
            )
        );
    }

    @Bean
    public DataFetcherWrapper<Boolean> deleteMotorcycle() {
        return new DataFetcherWrapper<>(
            "Mutation",
            "deleteMotorcycle",
            new SecuredDataFetcher<>(Authority.USER, dataFetchingEnvironment -> {
                String id = dataFetchingEnvironment.getArgument("id");
                return motorcycleService.deleteById(Long.valueOf(id))
                    .map(m -> GraphQLUtils.<Boolean>successFun().apply(m))
                    .onErrorResume(handlerFunction())
                    .toFuture();
            })
        );
    }
}
