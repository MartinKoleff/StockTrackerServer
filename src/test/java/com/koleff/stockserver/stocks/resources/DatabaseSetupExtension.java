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

    @Value("spring.datasource.username")
    private static String usernameDB;

    @Value("spring.datasource.password")
    private static String passwordDB;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = (PostgreSQLContainer<?>) new PostgreSQLContainer
            ("postgres:16.0")
            .withDatabaseName("stocks")
            .withUsername(usernameDB)
            .withPassword(passwordDB)
            .withInitScript("schema/schema-postgresql.sql") //Fixes Spring Batch error...
            .withReuse(true);


    @DynamicPropertySource
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
