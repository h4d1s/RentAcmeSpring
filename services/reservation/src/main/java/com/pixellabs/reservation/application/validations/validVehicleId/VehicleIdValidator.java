package com.pixellabs.reservation.application.validations.validVehicleId;

import java.util.UUID;

import com.pixellabs.reservation.infrastructure.grpc.InventoryGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class VehicleIdValidator implements ConstraintValidator<ValidVehicleId, UUID> {
	public final InventoryGrpcClient inventoryGrpcClient;
	
	@Override
	public boolean isValid(UUID vehicleId, ConstraintValidatorContext context) {		
		if (vehicleId == null) {
			return true;
	    }
		
		return inventoryGrpcClient.checkVehicle(vehicleId.toString());
	}
}
