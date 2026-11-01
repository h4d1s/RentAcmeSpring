package com.pixellabs.eventbus.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

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
public class BookingReservedIntegrationEvent extends BaseIntegrationEvent {
	private UUID bookingId;
	private UUID userId;
	private UUID vehicleId;
	private LocalDate pickupDate;
	private LocalDate returnDate;
	private BigDecimal price;
}
