package com.jpr.maintenance.database.model;

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

//    public static Either<GraphQLError, UserEntity> of (UserInput userInput) {}
}
