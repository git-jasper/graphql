package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.AbstractIntegrationTest;
import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.model.UserMotorcycleEntity;
import com.jpr.maintenance.graphql.model.UserMotorcycleInput;
import com.jpr.maintenance.graphql.model.UserMotorcyclePartInput;
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

import java.time.Duration;
import java.util.List;
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
    private PartRepository partRepository;
    @Autowired
    private UserMotorcyclePartRepository userMotorcyclePartRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void init() {
        userRepository.deleteAll().block();
        partRepository.deleteAll().block();
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
        UserEntity user = userRepository.findByUserName(USER_NAME).block();
        UserMotorcycleEntity userMotorcycle1 = userMotorcycleRepository.saveUserMotorcycle(new UserMotorcycleInput(user.getId(), 1L, "Blue")).block();
        userMotorcycleRepository.saveUserMotorcycle(new UserMotorcycleInput(user.getId(), 2L, "Red")).block();

        partRepository.saveAll(
                List.of(
                    PartEntity.builder()
                        .brand("DID")
                        .partNr("525VXGB-120")
                        .description("Gold X-Ring Chain with Connecting Link")
                        .build(),
                    PartEntity.builder()
                        .brand("AXCES")
                        .partNr("NB 350")
                        .description("AOR45 Silencer - 14\"")
                        .build()
                ))
            .flatMap(p -> userMotorcyclePartRepository.saveUserMotorcyclePart(new UserMotorcyclePartInput(userMotorcycle1.getId(), p.getId())))
            .blockLast(Duration.ofSeconds(2L));

        Mono<UserEntity> result = userRepository.findByUserName(USER_NAME);

        Predicate<UserEntity> predicate = u -> u.getMotorcycles().size() == 2
            && u.getMotorcycles().get(0).getColor().equals("Blue")
            && u.getMotorcycles().get(0).getMotorcycle().getBrand().equals(Brand.SUZUKI)
            && u.getMotorcycles().get(0).getParts().size() == 2
            && u.getMotorcycles().get(0).getParts().get(0).getBrand().equals("DID")
            && u.getMotorcycles().get(0).getParts().get(1).getBrand().equals("AXCES")
            && u.getMotorcycles().get(1).getColor().equals("Red")
            && u.getMotorcycles().get(1).getMotorcycle().getBrand().equals(Brand.DUCATI)
            && u.getMotorcycles().get(1).getParts().size() == 0;

        StepVerifier
            .create(result)
            .expectNextMatches(predicate)
            .expectComplete()
            .verify();
    }
}
