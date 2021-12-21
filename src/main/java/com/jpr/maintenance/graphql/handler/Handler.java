package com.jpr.maintenance.graphql.handler;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface Handler<T,U> {
    Optional<U> handleMaybe(T payload);

    default U handle(T payload, Function<T,U> defaultFun) {
        return handleMaybe(payload)
                .orElseGet(() -> defaultFun.apply(payload));
    }

    default Handler<T,U> orElse(Handler<T,U> next) {
        return new ChainedHandler<>(this, next);
    }

    @RequiredArgsConstructor
    class ChainedHandler<T,U> implements Handler<T,U> {
        private final Handler<T,U> current;
        private final Handler<T,U> next;

        @Override
        public Optional<U> handleMaybe(T payload) {
            return current
                    .handleMaybe(payload)
                    .or(() -> next.handleMaybe(payload));
        }
    }
}
