package com.jpr.maintenance.database.repository;

import com.jpr.maintenance.database.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import org.springframework.r2dbc.core.FetchSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntityTemplateUserRepositoryImplTest {

    private final R2dbcEntityTemplate template = mock(R2dbcEntityTemplate.class);
    private final DatabaseClient client = mock(DatabaseClient.class);
    private final GenericExecuteSpec spec = mock(GenericExecuteSpec.class);
    private final FetchSpec fetchSpec = mock(FetchSpec.class);

    private final EntityTemplateUserRepositoryImpl repository = new EntityTemplateUserRepositoryImpl(template);

    @Test
    void saveUser() {
        when(template.getDatabaseClient()).thenReturn(client);
        when(client.sql(anyString())).thenReturn(spec);
        when(spec.bind(anyString(), any())).thenReturn(spec);
        when(spec.fetch()).thenReturn(fetchSpec);
        when(fetchSpec.first()).thenReturn(Mono.just(emptyMap()));

        Mono<UserEntity> result = repository.saveUser(UserEntity.builder().build());

        StepVerifier
            .create(result)
            .expectError(IllegalArgumentException.class)
            .verify();
    }
}