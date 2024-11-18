package com.pixellabs.reservation.infrastructure.services.inventory;

import java.util.UUID;

import com.pixellabs.reservation.application.models.VehicleDTO;

public interface InventoryService {
	public VehicleDTO getVehicle(UUID id);
	public boolean checkVehicle(UUID id);
}
