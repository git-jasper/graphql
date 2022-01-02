package com.jpr.maintenance.database.model;

import com.jpr.maintenance.model.Password;
import com.jpr.maintenance.validation.errors.InputValidationException;
import com.jpr.maintenance.validation.model.User;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.jpr.maintenance.validation.errors.InputValidationError.USER_ACCESS_ERROR;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Table("user")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private String salt;
    private List<UserMotorcycleEntity> motorcycles;

    // for Jackson
    public UserEntity() {}

    // for builder
    public UserEntity(Long id, String username, String password, String salt, List<UserMotorcycleEntity> motorcycles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.motorcycles = motorcycles;
    }

    public Mono<UserEntity> verify(User user) { //TODO other class's responsibility?
        return Password.of(user.plainPassword(), this.getSalt())
            .filter(p -> this.password.equals(p.password()))
            .map(p -> this)
            .switchIfEmpty(Mono.error(new InputValidationException(USER_ACCESS_ERROR, "")));
    }

    public static Mono<UserEntity> of(User user) {
        return Password.of(user.plainPassword())
            .map(p ->
                UserEntity.builder()
                    .username(user.username())
                    .password(p.password())
                    .salt(p.salt())
                    .build()
            );
    }
}
