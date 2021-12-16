package com.jpr.maintenance.graphql.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThrowableHandlerProviderTest {

    @Test
    void getHandlerChain() {
        ThrowableHandler handler = ThrowableHandlerProvider.getHandlerChain();

        assertTrue(handler instanceof DataAccessExceptionHandler);
        assertTrue(handler.next instanceof DefaultHandler);
    }
}