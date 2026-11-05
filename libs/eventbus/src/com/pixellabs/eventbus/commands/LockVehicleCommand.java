package com.pixellabs.eventbus.commands;

import com.pixellabs.eventbus.BaseIntegrationEvent;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockVehicleCommand extends BaseIntegrationEvent {
    private UUID bookingId;
    private UUID vehicleId;
}
