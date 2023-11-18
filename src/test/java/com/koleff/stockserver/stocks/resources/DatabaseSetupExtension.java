package com.koleff.stockserver.stocks.resources;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback {
    /**
     * Test container setup
     */

    @LocalServerPort
    private Integer port;

    @Value("spring.datasource.username") //TODO: find a way to inject in container initialization (don't use static)
    private static String usernameDB;

    @Value("spring.datasource.password")
    private static String passwordDB;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer<?>) new PostgreSQLContainer
            ("postgres:16.0")
            .withDatabaseName("stocks")
            .withUsername(usernameDB)
            .withPassword(passwordDB)
            .withReuse(false);


    @DynamicPropertySource //TODO: use 1 class for all tests (This annotation doesn't work with JUnit 5 @ExtendWith() -> manually add for each test class...)
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        postgreSQLContainer.start();
    }

    @AfterAll
    @Override
    public void afterAll(ExtensionContext extensionContext) {
        postgreSQLContainer.stop();
    }
}
