package com.pixellabs.user.application.useCases.users.commands.updateUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserCommand (
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 3)
    String userName,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    String phoneNumber,

    String street,

    String city,

    @NotBlank
    String postalCode,

    String country
) {}
