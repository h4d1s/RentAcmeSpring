package com.pixellabs.user.infrastructure.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KeycloakManagementService {
    private static final String USERS_ENDPOINT = "http://keycloak:8080/admin/realms/rent-acme/users/{id}";
    private final RestClient restClient;

    public KeycloakUserResponse getUserById(String id) {
        try {
            return restClient.get()
                .uri(USERS_ENDPOINT, id)
                .retrieve()
                .body(KeycloakUserResponse.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean deleteUserById(String id) {
        try {
            return restClient.delete()
                .uri(USERS_ENDPOINT, id)
                .retrieve()
                .toBodilessEntity()
                .getStatusCode()
                .is2xxSuccessful();

        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
