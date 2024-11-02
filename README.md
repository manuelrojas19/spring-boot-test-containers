# Alerts API

This project is a REST API built with Spring Boot to send alerts via Kafka. The API checks for the existence of a user in a PostgreSQL database before publishing the alert message. If the user does not exist, the API returns a `404 Not Found` status code.

## Requirements

- **Java 17**
- **Maven**
- **Spring Boot 3.x**
- **Docker**
- **Docker Compose**

## Installation and Setup

To ensure the application and its dependencies run correctly, you need Docker and Docker Compose installed on your system. Follow these instructions to install them if you haven’t already:

### Installing Docker

1. Download Docker: Visit the official [Docker website](https://docs.docker.com/engine/install/) and download the appropriate version for your operating system.
2. Follow the installation instructions:
   - Windows/Mac: Run the installer and follow the on-screen instructions.
   - Linux: Use the installation guide for your specific distribution.
3. Verify installation. This should display the Docker version installed.
    ```sh
   docker --version
    ```
### Installing Docker Compose
1. Download Docker Compose: Docker Compose usually comes pre-installed with Docker Desktop on Windows and Mac. On Linux, you may need to install it manually. See the [Official Documentation](https://docs.docker.com/compose/install/)

### Build and start the downstream dependencies with Docker Compose:

1. Clone the repository:
    ```sh
    git clone https://github.com/manuelrojas19/spring-boot-test-containers
    cd digital-onboarding-api
    ```

2. Build and start dependencies with docker compose:
    ```sh
    cd docker-compose
    docker-compose up --build -d
    ```
3. Check dependencies successfully stared. You should see new docker containers started
    ```sh
    docker ps -a
    ```
4. Run the project
   ```sh
   mvn clean install
   mvn spring-boot:run
    ```

## Endpoints

### Send Alert

- **URL**: `/api/v1/alerts`
- **Method**: `POST`
- **Description**: Sends an alert message if the user exists. If not, returns a `404`.

#### Request

```json
{
    "userId": "1",
    "alertMessage": "This is an alert message"
}
```

#### Curl Command

```bash
curl -v -X POST http://localhost:8080/api/v1/alerts \
-H "Content-Type: application/json" \
-d '{
    "userId": "1",
    "alertMessage": "This is an alert message"
}' | jq .
```

## Test the application

You can use the following command to track message being posted on kafka container after make a valid request
```bash
docker exec -it docker-compose-kafka-1 kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic alerts-topic --from-beginning
```

### Running Tests

This application use [Test Containers](https://testcontainers.com/) in order to perform integration test with close real test dependencies
```sh
mvn clean test
```
In case you are using Colima for Mac and you see the following exception "Could not find a valid Docker environment", may you need to execute those (additional steps)[https://www.rockyourcode.com/testcontainers-with-colima/]