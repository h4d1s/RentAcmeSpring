package com.pixellabs.inventory.application.useCases.vehicles;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;

import jakarta.transaction.Transactional;

@Component
public class DeleteVehicleUseCase {

	@Autowired
	private VehicleRepository vehicleRepository;

	public DeleteVehicleUseCase(VehicleRepository vehicleRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
	}
	
	@Transactional
	public void execute(UUID id) throws InventoryDomainException {
		vehicleRepository.deleteById(id);
	}

}
