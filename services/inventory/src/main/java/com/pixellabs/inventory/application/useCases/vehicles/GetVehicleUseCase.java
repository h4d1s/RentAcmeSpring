package com.pixellabs.inventory.application.useCases.vehicles;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.exceptions.VehicleNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetVehicleUseCase {
	
	@Autowired
	private VehicleRepository vehicleRepository;

	public GetVehicleUseCase(VehicleRepository vehicleRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
	}

	@Transactional
	public Vehicle execute(UUID id) throws VehicleNotFoundException {
		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException());
		return vehicle;
	}
}
