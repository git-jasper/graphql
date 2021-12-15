package com.jpr.maintenance.graphql.exception;

public class ThrowableHandlerProvider {

    private static final ThrowableHandler HANDLER = new DataAccessExceptionHandler()
        .next(new DefaultHandler());

    private ThrowableHandlerProvider() {}

    public static ThrowableHandler getHandlerChain() {
        return HANDLER;
    }
}
