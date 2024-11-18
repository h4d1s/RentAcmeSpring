package com.pixellabs.reservation.application.validations.validVehicleId;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.reservation.infrastructure.services.inventory.InventoryService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class VehicleIdValidator implements ConstraintValidator<ValidVehicleId, UUID> {

	@Autowired
	public InventoryService inventoryService;
	
	@Override
	public boolean isValid(UUID vehicleId, ConstraintValidatorContext context) {		
		if (vehicleId == null) {
			return false;
	    }
		
		return inventoryService.checkVehicle(vehicleId);
	}

}
