package com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pixellabs.reservation.application.validations.validVehicleId.ValidVehicleId;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReserveBookingCommand(
    @NotNull(message = "Vehicle ID is required")
    @ValidVehicleId
    UUID vehicleId,

    @NotNull(message = "User ID is required")
    UUID userId,

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date must be today or in the future")
    LocalDateTime pickupDate,

    @NotNull(message = "Return date is required")
    @Future(message = "Return date must be in the future")
    LocalDateTime returnDate
) {}