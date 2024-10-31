package com.globant.training.docker.dctc;

import com.globant.training.docker.dctc.dto.AlertRequest;
import com.globant.training.docker.dctc.it.config.TestContainersConfig;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class DctcApplicationTests extends TestContainersConfig {

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldReturnSuccessfullResponseWhenUserExists() {
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
	}

}
