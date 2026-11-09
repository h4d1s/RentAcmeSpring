package com.pixellabs.inventory.application.useCases.vehicles.commands.deleteVehicle;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteVehicleHandler {
	private final VehicleRepository vehicleRepository;

	@CacheEvict(value = "vehicles", allEntries = true)
	@Transactional
	public void handle(DeleteVehicleCommand command)
			throws InventoryDomainException, NotFoundException {
		Vehicle vehicle = vehicleRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Model not found."));

		vehicleRepository.delete(vehicle);
	}
}
