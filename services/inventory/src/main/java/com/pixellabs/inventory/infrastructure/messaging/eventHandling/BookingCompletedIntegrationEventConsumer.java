package com.pixellabs.inventory.infrastructure.messaging.eventHandling;

import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCompletedIntegrationEvent;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCompletedIntegrationEventConsumer {
	private final VehicleRepository vehicleRepository;

	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_COMPLETED)
	@Transactional
	public void consume(BookingCompletedIntegrationEvent event) {
		try {
			UUID vehicleId = UUID.fromString(event.getVehicleId().toString());
			vehicleRepository.findById(vehicleId)
				.ifPresent(vehicle ->
					vehicle.updateStatus(VehicleStatus.AVAILABLE)
				);
		} catch (Exception ex) {
			log.error("BookingCompletedIntegrationEvent", ex);
			throw ex;
		}
	}
}
