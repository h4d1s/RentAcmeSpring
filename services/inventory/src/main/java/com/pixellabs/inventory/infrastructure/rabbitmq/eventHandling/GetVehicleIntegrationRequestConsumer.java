package com.pixellabs.inventory.infrastructure.rabbitmq.eventHandling;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.GetVehicleIntegrationRequest;
import com.pixellabs.eventbus.responses.GetVehicleIntegrationResponse;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class GetVehicleIntegrationRequestConsumer {
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private IntegrationEventService integrationEventService;
	
	@RabbitListener(queues = BusConstants.QUEUE_VEHICLE_GET_REQUEST)
	public void receive(@Payload GetVehicleIntegrationRequest request) {		
		Optional<Vehicle> optionalVehicle = vehicleRepository.findById(request.getId());
		if(optionalVehicle.isPresent()) {
			Vehicle vehicle = optionalVehicle.get();
			GetVehicleIntegrationResponse response = new GetVehicleIntegrationResponse(
					vehicle.getId(), vehicle.getRentalPricePerDay(), vehicle.getRegistrationPlates());
			integrationEventService.publishEvent(response, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_GET_REQUEST);
		} else {
			GetVehicleIntegrationResponse response = new GetVehicleIntegrationResponse(
					null, null, null);
			integrationEventService.publishEvent(response, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_GET_REQUEST);
		}
	}
}