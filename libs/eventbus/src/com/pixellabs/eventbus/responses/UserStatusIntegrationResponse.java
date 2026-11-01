package com.pixellabs.eventbus.responses;

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
public class UserStatusIntegrationResponse extends BaseIntegrationEvent {
	boolean isExists;
}
