package com.pixellabs.eventbus.commands;

import java.math.BigDecimal;
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
public class CompletePaymentIntegrationCommand extends BaseIntegrationEvent {
	private UUID userId;
    private BigDecimal totalPrice;
}