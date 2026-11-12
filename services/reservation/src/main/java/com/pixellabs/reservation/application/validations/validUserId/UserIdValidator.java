package com.pixellabs.reservation.application.validations.validUserId;

import com.pixellabs.reservation.infrastructure.grpc.user.UserGrpcClient;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdValidator implements ConstraintValidator<ValidUserId, UUID> {
    public final UserGrpcClient userGrpcClient;

    @Override
    public boolean isValid(UUID userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false;
        }

        try {
            return userGrpcClient.existsUser(userId);
        } catch (StatusRuntimeException ex) {
            log.error("Failed to validate user existence: {}", userId, ex);
            return false;
        }
    }
}