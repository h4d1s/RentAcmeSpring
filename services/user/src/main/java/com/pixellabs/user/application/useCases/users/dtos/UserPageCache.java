package com.pixellabs.user.application.useCases.users.dtos;

import com.pixellabs.user.domain.userAggregate.User;

import java.util.List;

public record UserPageCache(
    List<User> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {}