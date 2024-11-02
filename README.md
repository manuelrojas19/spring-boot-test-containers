# Alerts API

This project is a REST API built with Spring Boot to send alerts via Kafka. The API checks for the existence of a user in a PostgreSQL database before publishing the alert message. If the user does not exist, the API returns a `404 Not Found` status code.

## Requirements

- **Java 17**
- **Spring Boot 3.x**
- **PostgreSQL**
- **Apache Kafka**

## Configuration

### Database

1. Set up a PostgreSQL database and create a `users` table with the following schema:

    ```sql
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL
    );
    ```

2. Update your database connection credentials in the `application.yml` or `application.properties` file.

    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/alerts_db
        username: your_username
        password: your_password
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true
    ```

### Kafka

Configure the connection to the Kafka server in the `application.yml` file:

    ```yaml
    spring:
      kafka:
        bootstrap-servers: localhost:9092
        producer:
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
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
