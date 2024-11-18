package com.pixellabs.reservation.infrastructure.services.inventory;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.CheckVehicleIntegrationRequest;
import com.pixellabs.eventbus.requests.GetVehicleIntegrationRequest;
import com.pixellabs.eventbus.responses.GetVehicleIntegrationResponse;
import com.pixellabs.eventbus.responses.VehicleStatusIntegrationResponse;
import com.pixellabs.reservation.application.mappers.VehicleDTOMapper;
import com.pixellabs.reservation.application.models.VehicleDTO;
import com.pixellabs.reservation.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	public IntegrationEventService integrationEventService; 
	
	@Override
	public VehicleDTO getVehicle(UUID id) {
		GetVehicleIntegrationRequest request = new GetVehicleIntegrationRequest(null);
		GetVehicleIntegrationResponse response = (GetVehicleIntegrationResponse) integrationEventService.sendAndReceive(request, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_GET_REQUEST);
		VehicleDTO vehicle = VehicleDTOMapper.INSTANCE.getVehicleIntegrationResponse(response);
		return vehicle;
	}

	@Override
	public boolean checkVehicle(UUID id) {
		CheckVehicleIntegrationRequest request = new CheckVehicleIntegrationRequest(id);
		VehicleStatusIntegrationResponse response = (VehicleStatusIntegrationResponse) integrationEventService.sendAndReceive(request, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_CHECK_REQUEST);
		System.out.println("RESPONSE: " + response);
		return response.isExists();
	}
}
