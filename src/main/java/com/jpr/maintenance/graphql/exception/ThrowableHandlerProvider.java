package com.jpr.maintenance.graphql.exception;

public class ThrowableHandlerProvider {

    private static final ThrowableHandler HANDLER = new DefaultHandler()
        .nextOf(new DataAccessExceptionHandler());

    private ThrowableHandlerProvider() {}

    public static ThrowableHandler getHandlerChain() {
        return HANDLER;
    }
}
