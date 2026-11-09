package com.pixellabs.inventory.infrastructure.messaging.eventHandling;

import com.pixellabs.eventbus.commands.LockVehicleCommand;
import com.pixellabs.eventbus.events.VehicleLockedIntegrationEvent;
import com.pixellabs.eventbus.events.VehicleUnavailableIntegrationEvent;
import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pixellabs.eventbus.BusConstants;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockVehicleCommandIntegrationEventConsumer {
    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = BusConstants.QUEUE_VEHICLE_LOCK_COMMAND)
    @Transactional
    public void consume(LockVehicleCommand command) {
        UUID vehicleId = command.getVehicleId();
        UUID bookingId = command.getBookingId();

        try {
            var vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new NotFoundException("Vehicle not found."));

            if (vehicle.getStatus() == VehicleStatus.RESERVED ||
                vehicle.getStatus() == VehicleStatus.RENTED) {
                publishVehicleUnavailable(bookingId);
                return;
            }

            vehicle.updateStatus(VehicleStatus.RESERVED);
            vehicleRepository.save(vehicle);

            var event = new VehicleLockedIntegrationEvent(bookingId, vehicleId);
            rabbitTemplate.convertAndSend(
                BusConstants.EXCHANGE_INVENTORY,
                BusConstants.ROUTING_KEY_VEHICLE_LOCKED,
                event);
        } catch (Exception ex) {
            log.error("Failed to lock vehicle {}", vehicleId, ex);
            throw ex;
        }
    }

    private void publishVehicleUnavailable(UUID bookingId) {
        log.warn("Vehicle not available for booking: {}", bookingId);

        var event = new VehicleUnavailableIntegrationEvent(bookingId);
        rabbitTemplate.convertAndSend(
            BusConstants.EXCHANGE_INVENTORY,
            BusConstants.ROUTING_KEY_VEHICLE_UNAVAILABLE,
            event);
    }
}