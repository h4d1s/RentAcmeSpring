package com.pixellabs.inventory.application.useCases.vehicles;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.VehicleMapper;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.UpdateVehicleRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.exceptions.VehicleNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class UpdateVehicleUseCase {

	@Autowired
	private VehicleRepository vehicleRepository;

	public UpdateVehicleUseCase(VehicleRepository vehicleRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
	}
	
	@Transactional
	public Vehicle execute(UUID id, UpdateVehicleRequest request) throws VehicleNotFoundException, InventoryDomainException {
		Vehicle vehicleToModify = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException());
		Vehicle vehicle = VehicleMapper.INSTANCE.updateVehicleRequestToVehicle(request);
		vehicle.setId(vehicleToModify.getId());
		return vehicleRepository.save(vehicle);
	}

}
