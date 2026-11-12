package com.pixellabs.user.application.useCases.users.commands.deleteUser;

import java.util.UUID;

public record DeleteUserCommand (
    UUID id
) {}
