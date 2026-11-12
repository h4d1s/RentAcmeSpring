package com.pixellabs.user.infrastructure.messaging.listeners;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.events.KeycloakAdminIntegrationEvent;
import com.pixellabs.eventbus.events.KeycloakClientIntegrationEvent;
import com.pixellabs.eventbus.events.KeycloakUserRepresentation;
import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
import com.pixellabs.user.domain.userAggregate.exceptions.UserDomainException;
import com.pixellabs.user.infrastructure.identity.KeycloakManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakUserLifecycleEventListener {
    private final UserRepository userRepository;
    private final KeycloakManagementService keycloakManagementService;
    private final ObjectMapper objectMapper;

    @Transactional
    @RabbitListener(
        queues = Queues.KEYCLOAK_CLIENT_EVENTS,
        containerFactory = "rawRabbitListenerContainerFactory"
    )
    public void handle(Message message) throws Exception {
        JsonNode json = objectMapper.readTree(message.getBody());
        if (json.has("operationType")) {
            KeycloakAdminIntegrationEvent event = objectMapper.treeToValue(json, KeycloakAdminIntegrationEvent.class);
            handleAdminEvent(event);
        } else if (json.has("type")) {
            KeycloakClientIntegrationEvent event = objectMapper.treeToValue(json, KeycloakClientIntegrationEvent.class);
            handleClientEvent(event);
        } else {
            log.warn("Unknown Keycloak event: {}", json);
        }
    }

    private void handleClientEvent(KeycloakClientIntegrationEvent event)
        throws UserDomainException {
        if (event.type() == null ||
            event.userId() == null ||
            event.userId().isBlank()) {
            return;
        }

        if (event.representation() == null) {
            return;
        }
        KeycloakUserRepresentation userRepresentation =
            objectMapper.readValue(
                event.representation(),
                KeycloakUserRepresentation.class
            );

        switch (event.type()) {
            case "REGISTER" -> createUser(event.userId(), userRepresentation);
            default -> log.info("Unsupported client event: {}", event.type());
        }
    }

    public void handleAdminEvent(KeycloakAdminIntegrationEvent event)
        throws UserDomainException {
        if (!"USER".equals(event.resourceType())) {
            return;
        }

        String resourcePath = event.resourcePath();
        if (resourcePath == null || !resourcePath.startsWith("users/")) {
            log.warn("Invalid Keycloak resource path: {}", resourcePath);
            return;
        }

        String userId = resourcePath.substring("users/".length());
        if (userId.isBlank()) {
            return;
        }

        if (event.representation() == null) {
            return;
        }
        KeycloakUserRepresentation userRepresentation =
            objectMapper.readValue(
                event.representation(),
                KeycloakUserRepresentation.class
            );

        log.info(userRepresentation.toString());

        switch (event.operationType()) {
            case "CREATE" -> createUser(userId, userRepresentation);
            case "UPDATE" -> updateUser(userId, userRepresentation);
            case "DELETE" -> deleteUser(userId);
            default -> log.info(
                "Unsupported Keycloak admin operation: {}",
                event.operationType()
            );
        }
    }

    private void createUser(
        String userId,
        KeycloakUserRepresentation userRepresentation) throws UserDomainException {
        try {
            User newUser = new User(
                userId,
                userRepresentation.username(),
                userRepresentation.email(),
                userRepresentation.firstName(),
                userRepresentation.lastName(),
                ""
            );
            userRepository.save(newUser);
        } catch (Exception ex) {
            log.error("Error processing Keycloak CREATE/REGISTER event for user {}", userId, ex);
            throw ex;
        }
    }

    private void updateUser(
        String userId,
        KeycloakUserRepresentation userRepresentation) throws UserDomainException {
        try {
            User user = userRepository
                .findByExternalId(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
            user.updateEmail(userRepresentation.email());
            user.updateUsername(userRepresentation.username());
            user.updateFirstName(userRepresentation.firstName());
            user.updateLastName(userRepresentation.lastName());
            userRepository.save(user);
        } catch (Exception ex) {
            log.error("Error processing Keycloak UPDATE event for user {}",
                userId, ex);
            throw ex;
        }
    }

    private void deleteUser(String userId) {
        try {
            User user = userRepository
                .findByExternalId(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

            userRepository.delete(user);
            keycloakManagementService.deleteUserById(userId);
        } catch (Exception ex) {
            log.error("Error processing Keycloak DELETE event for user {}",
                userId, ex);
            throw ex;
        }
    }
}
