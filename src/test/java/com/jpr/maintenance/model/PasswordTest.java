package com.jpr.maintenance.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PasswordTest {

    @Test
    void ofOk() {
        Password result = Password.of("secret").get();

        assertNotEquals("secret", result.password());
        assertNotNull(result.salt());
    }

    @Test
    void ofConsistent() {
        String plainPassword = "secret";
        Password first = Password.of(plainPassword).get();
        Password second = Password.of(plainPassword, first.salt()).get();

        assertEquals(first, second);
    }
}