package com.pixellabs.payment.application.models;

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
public class CustomerDTO {
    private String id;
    private String name;
    private String email;
}
