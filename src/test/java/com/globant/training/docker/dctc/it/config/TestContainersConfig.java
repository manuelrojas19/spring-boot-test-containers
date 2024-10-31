package com.globant.training.docker.dctc.it.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestContainersConfig {

    private static final Instant start = Instant.now();

    @LocalServerPort
    protected Integer port = 0;

    @Container
    static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("alerts-db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("test-data.sql");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        log.info("🐳TestContainers started in {}", Duration.between(start, Instant.now()));
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
        log.info("🐳TestContainers stopped in {}", Duration.between(start, Instant.now()));
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        log.info("Setting properties");
        Supplier<Object> getJdbcUrl = postgreSQLContainer::getJdbcUrl;
        log.info("🐳 JDBC url from dynamic container {}", getJdbcUrl.get());
        registry.add("spring.datasource.url",
                getJdbcUrl);
    }

}
