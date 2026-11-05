package com.pixellabs.eventbus.commands;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnlockVehicleCommand {
    private UUID bookingId;
    private UUID vehicleId;
}