package com.pixellabs.inventory.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.inventory.application.useCases.vehicles.dtos.CreateVehicleRequest;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.UpdateVehicleRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
	VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);
	
	CreateVehicleRequest vehicleToCreateVehicleRequest(Vehicle vehicle) throws InventoryDomainException;
	Vehicle createVehicleRequestToVariant(CreateVehicleRequest request) throws InventoryDomainException;
	
	UpdateVehicleRequest vehicleToUpdateVehicleRequest(Vehicle variant) throws InventoryDomainException;
	Vehicle updateVehicleRequestToVehicle(UpdateVehicleRequest request) throws InventoryDomainException;
}
