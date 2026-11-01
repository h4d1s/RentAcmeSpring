package com.pixellabs.eventbus.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBookingIntegrationResponse {
    private UUID id;
    private UUID vehicleId;
    private UUID userId;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private LocalDateTime bookingDate;
    private BigDecimal price;
}
