package com.pixellabs.inventory.infrastructure.messaging.eventHandling;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCanceledIntegrationEventConsumer {
	private final VehicleRepository vehicleRepository;

	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_CANCELED)
	@Transactional
	public void consume(BookingCanceledIntegrationEvent event) {
		try {
			Vehicle vehicle = vehicleRepository
					.findById(UUID.fromString(event.getVehicleId().toString()))
					.orElseThrow(() -> new NotFoundException("Vehicle not found."));

			vehicle.updateStatus(VehicleStatus.AVAILABLE);
			vehicleRepository.save(vehicle);
		} catch (Exception ex) {
			log.error("BookingCanceledIntegrationEvent", ex);
			throw ex;
		}
	}
}
