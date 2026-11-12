package com.pixellabs.inventory.infrastructure.messaging.listeners;

import com.pixellabs.eventbus.Queues;
import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import com.pixellabs.eventbus.commands.UnlockVehicleCommand;
import com.pixellabs.eventbus.events.VehicleUnavailableIntegrationEvent;
import com.pixellabs.eventbus.events.VehicleUnlockedIntegrationEvent;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnlockVehicleCommandIntegrationEventListener {
    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @RabbitListener(queues = Queues.VEHICLE_UNLOCK_COMMAND)
    public void consume(UnlockVehicleCommand command) {
        UUID vehicleId = command.vehicleId();
        UUID bookingId = command.bookingId();
        UUID correlateId = command.correlateId();

        try {
            var vehicle = vehicleRepository.findById(vehicleId)
                .orElse(null);

            if (vehicle == null) {
                publishVehicleUnavailable(correlateId, bookingId);
                return;
            }

            if (vehicle.getStatus() == VehicleStatus.AVAILABLE) {
                log.info("Vehicle {} is already available", vehicleId);
                return;
            }

            publishVehicleUnlocked(correlateId, bookingId, vehicleId);
        } catch (Exception ex) {
            log.error("Failed to unlock vehicle {}", vehicleId, ex);
            throw ex;
        }
    }

    private void publishVehicleUnlocked(
        UUID correlateId,
        UUID bookingId,
        UUID vehicleId) {

        var event = new VehicleUnlockedIntegrationEvent(
            correlateId,
            bookingId,
            vehicleId
        );
        rabbitTemplate.convertAndSend(
            Exchanges.INVENTORY,
            RoutingKeys.VEHICLE_UNLOCKED,
            event
        );
    }

    private void publishVehicleUnavailable(
        UUID correlateId,
        UUID bookingId) {
        log.warn("Vehicle not available for booking: {}", bookingId);

        var event = new VehicleUnavailableIntegrationEvent(correlateId, bookingId);
        rabbitTemplate.convertAndSend(
            Exchanges.INVENTORY,
            RoutingKeys.VEHICLE_UNAVAILABLE,
            event
        );
    }
}
