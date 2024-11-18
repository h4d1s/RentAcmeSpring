package com.pixellabs.eventbus.events;

import java.util.UUID;

import com.pixellabs.eventbus.BaseIntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCanceledIntegrationEvent extends BaseIntegrationEvent {
	private UUID bookingId;
    private UUID vehicleId;
}
