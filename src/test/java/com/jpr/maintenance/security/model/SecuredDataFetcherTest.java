package com.jpr.maintenance.security.model;

import graphql.GraphQLContext;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.jpr.maintenance.validation.errors.InputValidationError.AUTHENTICATION_FAILED;
import static com.jpr.maintenance.validation.errors.InputValidationError.INSUFFICIENT_AUTHORITY;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecuredDataFetcherTest {

    DataFetcher<CompletableFuture<DataFetcherResult<String>>> mockFetcher = mock(DataFetcher.class);

    @Test
    void authenticationOK() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken("u", "p", singletonList(Authority.USER));
        DataFetcherResult<String> result = DataFetcherResult.<String>newResult().build();
        when(mockFetcher.get(any())).thenReturn(CompletableFuture.completedFuture(result));

        SecuredDataFetcher<String> dataFetcher = new SecuredDataFetcher<>(Authority.USER, mockFetcher);

        assertEquals(result, dataFetcher.get(withAuthentication(auth)).get());
    }

    @Test
    void insufficientAuthority() throws Exception {
        Authentication auth = new AnonymousAuthentication();

        SecuredDataFetcher<String> dataFetcher = new SecuredDataFetcher<>(Authority.USER, mockFetcher);

        assertEquals(INSUFFICIENT_AUTHORITY, dataFetcher.get(withAuthentication(auth)).get().getErrors().get(0).getErrorType());
    }

    @Test
    void failedAuthentication() throws Exception {
        Authentication auth = new FailedAuthentication();

        SecuredDataFetcher<String> dataFetcher = new SecuredDataFetcher<>(Authority.USER, mockFetcher);

        assertEquals(AUTHENTICATION_FAILED, dataFetcher.get(withAuthentication(auth)).get().getErrors().get(0).getErrorType());
    }

    private DataFetchingEnvironment withAuthentication(Authentication authentication) {
        return new DataFetchingEnvironmentImpl.Builder()
            .graphQLContext(GraphQLContext.of(
                Map.of(SecurityContext.class, new SecurityContextImpl(authentication))
            )).build();
    }
}