package com.globant.training.docker.dctc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.training.docker.dctc.dto.AlertRequest;
import com.globant.training.docker.dctc.it.config.TestContainersConfig;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;

@Slf4j
class ApplicationIntegrationTests extends TestContainersConfig {

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldReturnSuccessfulResponseWhenUserExists() {
        AlertRequest alertRequest = AlertRequest.builder()
                .userId(9L)
                .alertMessage("Test Message")
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(alertRequest)
                .when()
                .post("/api/v1/alerts")
                .then()
                .statusCode(200);

        // Validate if message was posted
        Assertions.assertTrue(getPostedMessages().contains(alertRequest));
    }

    @Test
    void shouldReturnNotFoundResponseWhenUserDoesNotExists() {
        AlertRequest alertRequest = AlertRequest.builder()
                .userId(99L)
                .alertMessage("Test Message")
                .build();

        RestAssured.given()
                .contentType("application/json")
                .body(alertRequest)
                .when()
                .post("/api/v1/alerts")
                .then()
                .statusCode(404);

        // Validate if message was not posted
        Assertions.assertFalse(getPostedMessages().contains(alertRequest));
    }


    private Set<AlertRequest> getPostedMessages() {
        Properties consumerProps = getConsumerProperties();
        Set<AlertRequest> storedMessages = new HashSet<>();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singleton("alerts-topic"));

            // Poll for messages within a loop
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(7));

            if (records.isEmpty()) {
                log.info("No messages received within the poll interval.");
            }

            for (ConsumerRecord<String, String> record : records) {
                String recordValue = record.value();
                ObjectMapper mapper = new ObjectMapper();
                AlertRequest alertRequest = null;
                try {
                    alertRequest = mapper.readValue(recordValue, AlertRequest.class);
                } catch (Exception e) {
                    log.error("Error deserializing", e);
                }
                storedMessages.add(alertRequest);  // Store the message
                log.info("Record uploaded to Kafka container: {}", alertRequest);
            }

            return storedMessages;
        }
    }

    private static @NotNull Properties getConsumerProperties() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return consumerProps;
    }

}
