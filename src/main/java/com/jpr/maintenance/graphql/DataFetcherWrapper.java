package com.jpr.maintenance.graphql;

import com.jpr.maintenance.security.model.SecuredDataFetcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DataFetcherWrapper<T> {
    private final String parentType;
    private final String fieldName;
    private final SecuredDataFetcher<T> dataFetcher;
}
