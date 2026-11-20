package com.pixellabs.reservation.application.validations.validUserId;

import com.pixellabs.libs.user.grpc.UserGrpcClientService;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserIdValidator implements ConstraintValidator<ValidUserId, UUID> {
    public final UserGrpcClientService userGrpcClientService;

    @Override
    public boolean isValid(UUID userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false;
        }

        try {
            return userGrpcClientService.existsUser(userId);
        } catch (StatusRuntimeException ex) {
            log.error("Failed to validate user existence: {}", userId, ex);
            return false;
        }
    }
}