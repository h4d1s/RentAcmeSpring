package com.pixellabs.reservation.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.eventbus.responses.GetVehicleIntegrationResponse;
import com.pixellabs.reservation.application.models.VehicleDTO;

@Mapper(componentModel = "spring")
public interface VehicleDTOMapper {
	VehicleDTOMapper INSTANCE = Mappers.getMapper(VehicleDTOMapper.class);
	
	GetVehicleIntegrationResponse vehicleDTOToGetVehicleIntegrationResponse(VehicleDTO vehicleDTO);
	VehicleDTO getVehicleIntegrationResponse(GetVehicleIntegrationResponse response);
}
