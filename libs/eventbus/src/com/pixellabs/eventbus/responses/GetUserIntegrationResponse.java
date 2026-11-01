package com.pixellabs.eventbus.responses;

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
public class GetUserIntegrationResponse {
    UUID id;
    String firstName;
	String lastName;
    String email;
}
