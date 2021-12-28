package com.jpr.maintenance.model;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PasswordTest {

    @Test
    void ofOk() {
        Mono<Password> result = Password.of("secret");

        StepVerifier
            .create(result)
            .expectNextMatches(p -> !p.password().equals("secret") && p.salt() != null)
            .expectComplete()
            .verify();
    }

    @Test
    void ofConsistent() {
        String plainPassword = "secret";
        Mono<Password> first = Password.of(plainPassword);
        Mono<Password> second = first
            .flatMap(p -> Password.of(plainPassword, p.salt()));

        var zipped = first.zipWith(second);

        StepVerifier
            .create(zipped)
            .expectNextMatches(t -> t.getT1().password().equals(t.getT2().password()))
            .expectComplete()
            .verify();
    }
}