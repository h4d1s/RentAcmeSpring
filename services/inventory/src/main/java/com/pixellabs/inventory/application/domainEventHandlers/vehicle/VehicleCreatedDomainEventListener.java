package com.pixellabs.inventory.application.domainEventHandlers.vehicle;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.pixellabs.inventory.domain.vehicleAggregate.events.VehicleCreatedDomainEvent;

@Service
public class VehicleCreatedDomainEventListener {
	
	@EventListener
	public void handleVehicleCreatedEvent(VehicleCreatedDomainEvent event)
	{}	
}
