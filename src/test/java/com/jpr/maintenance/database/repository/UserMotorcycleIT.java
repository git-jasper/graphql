package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.MotorcycleEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.model.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserMotorcycleIT extends AbstractIntegrationTest {
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "secret";
    private static final String SALT = "salt";
    private UserEntity user;

    @Autowired
    private UserMotorcycleRepository userMotorcycleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init() {
        user = Password.of(PASSWORD, SALT)
            .map(p -> UserEntity.builder()
                .username(USER_NAME)
                .password(p.password())
                .salt(p.salt())
                .build()
            )
            .flatMap(userRepository::saveUser)
            .block();
    }

    @Test
    @Order(1)
    void shouldPersistUserMotorcycle() {
        Mono<UserMotorcycleEntity> result = userMotorcycleRepository.saveUserMotorcycle(user.getId(), 1L, "red");
        Predicate<UserMotorcycleEntity> predicate = e -> {
            MotorcycleEntity motorcycle = e.getMotorcycle();
            return e.getUser() != null
                && e.getColor().equals("red")
                && motorcycle.getId().equals(1L)
                && motorcycle.getName().equals("GSX-1300R")
                && motorcycle.getBrand().equals(Brand.SUZUKI)
                && motorcycle.getEngineSize() == 1340;
        };

        StepVerifier
            .create(result)
            .expectNextMatches(predicate)
            .expectComplete()
            .verify();
    }

    @Test
    @Order(2)
    void shouldHaveSecondMotorcycleWithOtherMotorcycleAlreadyInDb() {
        Mono<UserMotorcycleEntity> result = userMotorcycleRepository.saveUserMotorcycle(user.getId(), 2L, "blue");

        Predicate<UserMotorcycleEntity> predicate = e -> {
            MotorcycleEntity motorcycle = e.getMotorcycle();
            return e.getUser() != null
                && e.getColor().equals("blue")
                && motorcycle.getId().equals(2L)
                && motorcycle.getName().equals("999R")
                && motorcycle.getBrand().equals(Brand.DUCATI)
                && motorcycle.getEngineSize() == 999;
        };

        StepVerifier
            .create(result)
            .expectNextMatches(predicate)
            .expectComplete()
            .verify();
    }
}
