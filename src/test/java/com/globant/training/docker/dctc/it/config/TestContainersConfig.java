package com.globant.training.docker.dctc.it.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

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
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("alerts-db")
            .withInitScript("test-data.sql");

    @Container
    public static final KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka-native:3.8.0");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        kafkaContainer.start();
        log.info("🐳TestContainers started in {}", Duration.between(start, Instant.now()));
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
        kafkaContainer.stop();
        log.info("🐳TestContainers stopped in {}", Duration.between(start, Instant.now()));
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        log.info("🐳Setting properties for postgreSQL");

        Supplier<Object> getJdbcUrl = postgreSQLContainer::getJdbcUrl;
        Supplier<Object> getUsername = postgreSQLContainer::getUsername;
        Supplier<Object> getPassword = postgreSQLContainer::getPassword;

        log.info("🐳JDBC url from dynamic container {}", getJdbcUrl.get());
        log.info("🐳JDBC url from dynamic container {}", getJdbcUrl.get());
        log.info("🐳JDBC url from dynamic container {}", getJdbcUrl.get());

        registry.add("spring.datasource.url", getJdbcUrl);
        registry.add("spring.datasource.username", getUsername);
        registry.add("spring.datasource.password", getPassword);

        log.info("🐳Setting properties for Kafka");

        Supplier<Object> kafkaServer = kafkaContainer::getBootstrapServers;
        log.info("🐳Kafka url from dynamic container {}", kafkaServer.get());
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaServer);
    }

}
