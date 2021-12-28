package com.jpr.maintenance.database.repository;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = {AbstractIntegrationTest.Initializer.class})
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer postgreSQLContainer =
                new PostgreSQLContainer("postgres:14.0")
                        .withDatabaseName("maintenance")
                        .withUsername("user")
                        .withPassword("pass");

        private static void startContainers() {
            Startables.deepStart(Stream.of(postgreSQLContainer)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "datasource.port", String.valueOf(postgreSQLContainer.getFirstMappedPort())
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            startContainers();

            ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();

            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers", createConnectionConfiguration()
            );

            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
