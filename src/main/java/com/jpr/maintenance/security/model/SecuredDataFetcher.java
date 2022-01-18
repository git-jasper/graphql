package com.jpr.maintenance.security.model;

import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.graphql.GraphQLUtils.createError;
import static com.jpr.maintenance.validation.errors.InputValidationError.AUTHENTICATION_FAILED;
import static com.jpr.maintenance.validation.errors.InputValidationError.INSUFFICIENT_AUTHORITY;

public class SecuredDataFetcher<T> implements DataFetcher<CompletableFuture<DataFetcherResult<T>>> {

    private static final Authentication FAILED = new FailedAuthentication();

    private final Authority requiredAuthority;
    private final DataFetcher<CompletableFuture<DataFetcherResult<T>>> dataFetcher;

    public SecuredDataFetcher(Authority requiredAuthority, DataFetcher<CompletableFuture<DataFetcherResult<T>>> dataFetcher) {
        this.requiredAuthority = requiredAuthority;
        this.dataFetcher = dataFetcher;
    }

    @Override
    public CompletableFuture<DataFetcherResult<T>> get(DataFetchingEnvironment environment) throws Exception {
        SecurityContext context = environment.getGraphQlContext().get(SecurityContext.class);
        Authentication authentication = context.getAuthentication();
        if (FAILED.equals(authentication)) {
            return CompletableFuture.supplyAsync(() -> DataFetcherResult.<T>newResult()
                .error(createError(AUTHENTICATION_FAILED, ""))
                .build());
        }
        if (!authentication.getAuthorities().contains(requiredAuthority)) {
            return CompletableFuture.supplyAsync(() -> DataFetcherResult.<T>newResult()
                .error(createError(INSUFFICIENT_AUTHORITY, ""))
                .build());
        }
        return dataFetcher.get(environment);
    }
}
