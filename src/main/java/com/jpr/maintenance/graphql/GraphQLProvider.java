package com.jpr.maintenance.graphql;

import com.jpr.maintenance.tailrecursion.TailCall;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.jpr.maintenance.tailrecursion.TailCalls.done;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration
public class GraphQLProvider {
    @Bean
    public GraphQL graphQL(@Autowired GraphQLSchema schema) {
        return GraphQL
                .newGraphQL(schema)
                .build();
    }

    @Bean
    public GraphQLSchema buildSchema(@Autowired RuntimeWiring runtimeWiring) throws IOException {
        ClassPathResource resource = new ClassPathResource("schema.graphqls");
        String sdl = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    @Bean
    public RuntimeWiring buildDataFetcherWiring(@Autowired List<DataFetcherWrapper<?>> dataFetcherWrappers) {
        return buildDataFetcherWiringRecursively(
                RuntimeWiring.newRuntimeWiring(),
                io.vavr.collection.List.ofAll(dataFetcherWrappers)
        ).invoke();
    }

    private TailCall<RuntimeWiring> buildDataFetcherWiringRecursively(
            RuntimeWiring.Builder runtimeWiringBuilder,
            io.vavr.collection.List<DataFetcherWrapper<?>> vavrList) {
        if (vavrList.isEmpty()) {
            return done(runtimeWiringBuilder.build());
        } else {
            DataFetcherWrapper<?> wrapper = vavrList.head();
            return () -> buildDataFetcherWiringRecursively(
                    runtimeWiringBuilder
                            .type(newTypeWiring(wrapper.getParentType())
                                    .dataFetcher(wrapper.getFieldName(), wrapper.getDataFetcher())),
                    vavrList.tail()
            );
        }
    }
}
