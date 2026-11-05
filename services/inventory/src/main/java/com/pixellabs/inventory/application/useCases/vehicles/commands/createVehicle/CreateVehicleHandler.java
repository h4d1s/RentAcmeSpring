package com.pixellabs.inventory.application.useCases.vehicles.commands.createVehicle;

import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateVehicleHandler {
	private final VehicleRepository vehicleRepository;
	private final Validator validator;

	@CacheEvict(value = "vehicles", allEntries = true)
	@Transactional
	public Vehicle handle(CreateVehicleCommand command)
			throws InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<CreateVehicleCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid vehicle data.");
		}

		Vehicle vehicle = new Vehicle(
			command.rentalPricePerDay(),
			command.registrationPlates(),
			VehicleStatus.AVAILABLE,
			command.mileageKm()
		);
		return vehicleRepository.save(vehicle);
	}
}
