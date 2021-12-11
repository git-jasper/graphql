package com.jpr.maintenance.database.model;

import com.jpr.maintenance.model.Password;
import com.jpr.maintenance.validation.model.User;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import io.vavr.control.Either;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.function.Function;

import static com.jpr.maintenance.validation.errors.InputValidationError.USER_ACCESS_ERROR;

@Data
@Builder
@Table("user")
public class UserEntity {
    @Id
    private Long id;
    private String username;
    private String password;
    private String salt;

    public Either<GraphQLError, UserEntity> verify(User user) { //TODO other class's responsibility?
        return Password.of(user.plainPassword(), this.getSalt())
            .filterOrElse(p -> this.password.equals(p.password()), error())
            .flatMap(p -> Either.right(this));
    }

    private Function<Password, GraphQLError> error() {
        return p -> GraphqlErrorBuilder
            .newError()
            .message(USER_ACCESS_ERROR.getErrorMessage("")) //TODO weird for errors without parameters
            .errorType(USER_ACCESS_ERROR)
            .build();
    }

    public static Either<GraphQLError, UserEntity> of(User user) {
        return Password.of(user.plainPassword())
            .flatMap(p -> Either.right(
                UserEntity.builder()
                    .username(user.username())
                    .password(p.password())
                    .salt(p.salt())
                    .build())
            );
    }
}
