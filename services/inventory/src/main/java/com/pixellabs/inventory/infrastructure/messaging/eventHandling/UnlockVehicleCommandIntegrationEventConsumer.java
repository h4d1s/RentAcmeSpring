package com.pixellabs.inventory.infrastructure.messaging.eventHandling;

import com.pixellabs.eventbus.BusConstants;
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
public class UnlockVehicleCommandIntegrationEventConsumer {
    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = BusConstants.QUEUE_VEHICLE_UNLOCK_COMMAND)
    @Transactional
    public void consume(UnlockVehicleCommand command) {
        UUID vehicleId = command.getVehicleId();
        UUID bookingId = command.getBookingId();

        try {
            var vehicle = vehicleRepository.findById(vehicleId);
            if (vehicle.isEmpty()) {
                publishVehicleUnavailable(bookingId);
                return;
            }

            vehicle.get().updateStatus(VehicleStatus.AVAILABLE);

            var event = new VehicleUnlockedIntegrationEvent(bookingId, vehicleId);
            rabbitTemplate.convertAndSend(
                BusConstants.EXCHANGE_INVENTORY,
                BusConstants.ROUTING_KEY_VEHICLE_UNLOCKED,
                event
            );
        } catch (Exception ex) {
            log.error("Failed to unlock vehicle {}", vehicleId, ex);
            throw ex;
        }
    }

    private void publishVehicleUnavailable(UUID bookingId) {
        log.warn("Vehicle not available for booking: {}", bookingId);

        var event = new VehicleUnavailableIntegrationEvent();
        event.setBookingId(bookingId);
        rabbitTemplate.convertAndSend(
            BusConstants.EXCHANGE_INVENTORY,
            BusConstants.ROUTING_KEY_VEHICLE_UNAVAILABLE,
            event
        );
    }
}
