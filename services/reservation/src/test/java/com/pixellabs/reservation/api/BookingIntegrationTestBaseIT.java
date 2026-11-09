package com.pixellabs.reservation.api;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
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
import java.util.Properties;
import java.util.UUID;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
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
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitmq.getMappedPort(5672));
    }

    @Autowired
    protected BookingRepository bookingRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
            () -> redis.getMappedPort(6379));
    }

    @BeforeEach
    void cleanDatabase() {
        bookingRepository.deleteAll();
    }

    protected UUID seedBooking() throws ReservationDomainException {
        LocalDateTime pickupDate = LocalDateTime.now().plusDays(1);
        LocalDateTime returnDate = pickupDate.plusDays(3);

        Booking booking = new Booking(
            UUID.randomUUID(),
            UUID.randomUUID(),
            pickupDate,
            returnDate
        );
        return bookingRepository.save(booking).getId();
    }
}