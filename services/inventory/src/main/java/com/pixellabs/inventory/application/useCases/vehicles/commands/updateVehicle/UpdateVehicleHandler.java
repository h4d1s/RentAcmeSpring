package com.pixellabs.inventory.application.useCases.vehicles.commands.updateVehicle;

import java.util.Set;
import java.util.UUID;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateVehicleHandler {
	private final VehicleRepository vehicleRepository;
	private final Validator validator;

	@CacheEvict(value = "vehicles", allEntries = true)
	@Transactional
	public Vehicle handle(UUID id, UpdateVehicleCommand command)
            throws NotFoundException, InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<UpdateVehicleCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid vehicle data.");
		}

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Vehicle not found."));

		vehicle.updateRegistrationPlates(command.registrationPlates());
		vehicle.updateRentalPrice(command.rentalPricePerDay());

		return vehicleRepository.save(vehicle);
	}
}
