package com.pixellabs.reservation.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.UUID;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public abstract class BookingIntegrationTestBaseIT {
    @Container
    protected static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("reservation_test_db")
            .withUsername("rent-acme")
            .withPassword("Passw0rd!");

    @Container
    protected static final GenericContainer<?> redis =
        new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @Container
    static RabbitMQContainer rabbitmq =
        new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExposedPorts(5672);

    @DynamicPropertySource
    static void rabbitmqProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
            () -> redis.getMappedPort(6379));
        registry.add("spring.data.redis.password", () -> "");

        // RabbitMQ
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port",
            () -> rabbitmq.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);
    }
}