package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import com.jpr.maintenance.model.Brand;
import com.jpr.maintenance.model.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Predicate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserRepositoryIT extends AbstractIntegrationTest {
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "secret";
    private static final String SALT = "salt";

    @Autowired
    private UserMotorcycleRepository userMotorcycleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init() {
        userRepository.deleteAll().block();
    }

    @Test
    @Order(1)
    void shouldPersistUser() {
        Mono<UserEntity> result = Password.of(PASSWORD, SALT)
            .map(p -> UserEntity.builder()
                .username(USER_NAME)
                .password(p.password())
                .salt(p.salt())
                .build()
            )
            .flatMap(userRepository::saveUser);

        Mono<Password> passwordMono = Password.of(PASSWORD, SALT);

        StepVerifier
            .create(result.zipWith(passwordMono))
            .expectNextMatches(t -> t.getT1().getUsername().equals(USER_NAME) && t.getT1().getPassword().equals(t.getT2().password()))
            .expectComplete()
            .verify();
    }

    /*
     * Note: this test requires the previous test to have been executed so there is a user present
     */
    @Test
    @Order(2)
    void shouldHaveMotorcycles() {
        userRepository.findByUserName(USER_NAME)
            .flatMapMany(u -> Flux.concat(
                userMotorcycleRepository.saveUserMotorcycle(new UserMotorcycleInput(u.getId(), 1L, "Blue")),
                userMotorcycleRepository.saveUserMotorcycle(new UserMotorcycleInput(u.getId(), 2L, "Red"))
            ))
            .blockLast(Duration.ofSeconds(2L));

        Mono<UserEntity> result = userRepository.findByUserName(USER_NAME);

        Predicate<UserEntity> predicate = u -> u.getMotorcycles().size() == 2
            && u.getMotorcycles().get(0).getColor().equals("Blue")
            && u.getMotorcycles().get(0).getMotorcycle().getBrand().equals(Brand.SUZUKI)
            && u.getMotorcycles().get(1).getColor().equals("Red")
            && u.getMotorcycles().get(1).getMotorcycle().getBrand().equals(Brand.DUCATI);

        StepVerifier
            .create(result)
            .expectNextMatches(predicate)
            .expectComplete()
            .verify();
    }
}
