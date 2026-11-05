package com.pixellabs.eventbus.events;

import com.pixellabs.eventbus.BaseIntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLockedIntegrationEvent extends BaseIntegrationEvent {
    private UUID bookingId;
    private UUID vehicleId;
}
