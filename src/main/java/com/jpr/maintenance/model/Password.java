package com.jpr.maintenance.model;

import graphql.GraphQLError;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static com.jpr.maintenance.graphql.GraphQLUtils.createLeft;
import static com.jpr.maintenance.validation.errors.InputValidationError.FAILED_TO_INSTANTIATE_PASSWORD;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

@EqualsAndHashCode
public class Password {

    private final static SecureRandom SECURE_RANDOM = new SecureRandom();
    private final String password;
    private final String salt;

    private Password(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    public String password() {
        return password;
    }

    public String salt() {
        return salt;
    }

    public static Either<GraphQLError, Password> of(String plainPassword) {
        final byte[] salt = generateSalt();
        return generateHash(plainPassword, salt)
            .map(p -> new Password(toBase64(p), toBase64(salt)));
    }

    public static Either<GraphQLError, Password> of(String plainPassword, String salt) {
        return generateHash(plainPassword, fromBase64(salt))
            .map(p -> new Password(toBase64(p), salt));
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    private static Either<GraphQLError, byte[]> generateHash(String plainPassword, byte[] salt) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-512");
            instance.update(salt);
            return Either.right(instance.digest(plainPassword.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return createLeft(FAILED_TO_INSTANTIATE_PASSWORD, "");
        }
    }

    private static String toBase64(byte[] bytes) {
        return getEncoder().encodeToString(bytes);
    }

    private static byte[] fromBase64(String base64String) {
        return getDecoder().decode(base64String);
    }
}
