package com.jpr.maintenance.database.model;

import com.jpr.maintenance.graphql.model.UserInput;
import com.jpr.maintenance.validation.ValidationService;
import graphql.GraphQLError;
import io.vavr.control.Either;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("user")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;

    public static Either<GraphQLError, UserEntity> of(UserInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> Either.right(
                UserEntity.builder()
                    .username(input.username())
                    .password(input.password()) // TODO hash+salt
                    .build())
            );
    }
}
