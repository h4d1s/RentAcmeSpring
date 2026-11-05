package com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicle;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVehicleHandler {
	private final VehicleRepository vehicleRepository;

	@Cacheable(value = "vehicle", key = "#query.id()")
	@Transactional
	public Vehicle handle(GetVehicleQuery query)
			throws NotFoundException {
		return vehicleRepository.findById(query.id())
				.orElseThrow(() -> new NotFoundException("Vehicle not found."));
	}
}
