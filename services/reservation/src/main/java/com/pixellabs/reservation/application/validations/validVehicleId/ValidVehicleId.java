package com.pixellabs.reservation.application.validations.validVehicleId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleIdValidator.class) 
public @interface ValidVehicleId {
	
	String message() default "Invalid vehicle ID";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
