package com.pixellabs.inventory.application.useCases.vehicles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.VehicleMapper;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.CreateVehicleRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;

import jakarta.transaction.Transactional;

@Component
public class CreateVehicleUseCase {
	@Autowired
	private VehicleRepository vehicleRepository;

	public CreateVehicleUseCase(VehicleRepository vehicleRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
	}
	
	@Transactional
	public Vehicle execute(CreateVehicleRequest request) throws InventoryDomainException {
		Vehicle newVehicle = VehicleMapper.INSTANCE.createVehicleRequestToVariant(request);
		Vehicle vehicle = vehicleRepository.save(newVehicle);
		return vehicle;
	}
}
