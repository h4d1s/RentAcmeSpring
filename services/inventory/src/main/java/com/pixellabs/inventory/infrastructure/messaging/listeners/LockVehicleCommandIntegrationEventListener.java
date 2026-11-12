package com.pixellabs.inventory.infrastructure.messaging.listeners;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
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
import com.pixellabs.eventbus.Queues;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockVehicleCommandIntegrationEventListener {
    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @RabbitListener(queues = Queues.VEHICLE_LOCK_COMMAND)
    public void consume(LockVehicleCommand command) {
        UUID vehicleId = command.vehicleId();
        UUID bookingId = command.bookingId();
        UUID correlateId = command.correlateId();

        try {
            var vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new NotFoundException("Vehicle not found."));

            if (vehicle.getStatus() == VehicleStatus.RESERVED ||
                vehicle.getStatus() == VehicleStatus.RENTED) {
                publishVehicleUnavailable(correlateId, bookingId);
                return;
            }

            vehicle.updateStatus(VehicleStatus.RESERVED);
            vehicleRepository.save(vehicle);
            publishVehicleLocked(correlateId, bookingId, vehicleId);
        } catch (Exception ex) {
            log.error("Failed to lock vehicle {}", vehicleId, ex);
            throw ex;
        }
    }

    private void publishVehicleLocked(UUID correlateId, UUID bookingId, UUID vehicleId) {
        var event = new VehicleLockedIntegrationEvent(
            correlateId,
            bookingId,
            vehicleId);
        rabbitTemplate.convertAndSend(
            Exchanges.INVENTORY,
            RoutingKeys.VEHICLE_LOCKED,
            event);
    }

    private void publishVehicleUnavailable(UUID correlateId, UUID bookingId) {
        log.warn("Vehicle not available for booking: {}", bookingId);

        var event = new VehicleUnavailableIntegrationEvent(
            correlateId,
            bookingId);
        rabbitTemplate.convertAndSend(
            Exchanges.INVENTORY,
            RoutingKeys.VEHICLE_UNAVAILABLE,
            event);
    }
}