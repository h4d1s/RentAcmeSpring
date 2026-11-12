package com.pixellabs.reservation.application.validations.validUserId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
public @interface ValidUserId {
    String message() default "Invalid user ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}