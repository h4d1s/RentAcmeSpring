package com.pixellabs.reservation.application.useCases.bookings.dtos;

import java.time.LocalDate;
import java.util.UUID;

import com.pixellabs.reservation.application.validations.validVehicleId.ValidVehicleId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveBookingRequest {
  @ValidVehicleId
  private UUID vehicleId;
  private UUID userId;
  private LocalDate pickupDate;
  private LocalDate returnDate;
}
