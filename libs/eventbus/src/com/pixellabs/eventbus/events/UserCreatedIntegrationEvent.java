package com.pixellabs.eventbus.events;

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
public class UserCreatedIntegrationEvent extends BaseIntegrationEvent {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
}
