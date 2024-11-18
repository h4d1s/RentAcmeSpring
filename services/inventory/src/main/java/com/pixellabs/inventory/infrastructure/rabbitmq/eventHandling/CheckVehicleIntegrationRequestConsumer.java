package com.pixellabs.inventory.infrastructure.rabbitmq.eventHandling;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.requests.CheckVehicleIntegrationRequest;
import com.pixellabs.eventbus.responses.VehicleStatusIntegrationResponse;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class CheckVehicleIntegrationRequestConsumer {
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private IntegrationEventService integrationEventService;
	
	@RabbitListener(queues = BusConstants.QUEUE_VEHICLE_CHECK_REQUEST)
	public void receive(@Payload CheckVehicleIntegrationRequest request) {
		if(request.getId() == null) {
			return;
		}
		
		Optional<Vehicle> optionalVehicle = vehicleRepository.findById(request.getId());

		VehicleStatusIntegrationResponse response = new VehicleStatusIntegrationResponse(optionalVehicle.isPresent());
		integrationEventService.publishEvent(response, BusConstants.EXCHANGE_INVENTORY, BusConstants.ROUTING_KEY_VEHICLE_CHECK_REQUEST);
	}
}