package com.pixellabs.inventory.infrastructure.messaging.listeners;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCanceledIntegrationEventListener {
	private final VehicleRepository vehicleRepository;

	@Transactional
	@RabbitListener(queues = Queues.BOOKING_CANCELED)
	public void consume(BookingCanceledIntegrationEvent event) {
		try {
			Vehicle vehicle = vehicleRepository
					.findById(UUID.fromString(event.vehicleId().toString()))
					.orElseThrow(() -> new NotFoundException("Vehicle not found."));

			vehicle.updateStatus(VehicleStatus.AVAILABLE);
			vehicleRepository.save(vehicle);
		} catch (Exception ex) {
			log.error("BookingCanceledIntegrationEvent", ex);
			throw ex;
		}
	}
}
