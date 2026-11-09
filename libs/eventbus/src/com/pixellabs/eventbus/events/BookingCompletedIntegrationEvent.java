package com.pixellabs.eventbus.events;

import java.time.LocalDateTime;
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
public class BookingCompletedIntegrationEvent extends BaseIntegrationEvent {
	private UUID bookingId;
	private UUID vehicleId;
	private LocalDateTime pickupDate;
	private LocalDateTime returnDate;
}
