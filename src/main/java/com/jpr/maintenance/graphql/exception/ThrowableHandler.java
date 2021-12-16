package com.jpr.maintenance.graphql.exception;

import graphql.execution.DataFetcherResult;

public abstract class ThrowableHandler {

    protected ThrowableHandler next;

    public ThrowableHandler nextOf(ThrowableHandler other) {
        other.next = this;
        return other;
    }

    public abstract <T> DataFetcherResult<T> handle(Throwable throwable);


}
