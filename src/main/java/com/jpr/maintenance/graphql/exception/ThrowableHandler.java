package com.jpr.maintenance.graphql.exception;

import graphql.execution.DataFetcherResult;

public abstract class ThrowableHandler {

    protected ThrowableHandler next;

    public ThrowableHandler next(ThrowableHandler next) {
        this.next = next;
        return this;
    }

    public abstract <T> DataFetcherResult<T> handle(Throwable throwable);


}
