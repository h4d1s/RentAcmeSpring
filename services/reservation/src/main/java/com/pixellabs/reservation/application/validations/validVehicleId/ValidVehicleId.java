package com.pixellabs.reservation.application.validations.validVehicleId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleIdValidator.class) 
public @interface ValidVehicleId {
	String message() default "Invalid vehicle ID";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
