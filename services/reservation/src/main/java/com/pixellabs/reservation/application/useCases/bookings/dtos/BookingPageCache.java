package com.pixellabs.reservation.application.useCases.bookings.dtos;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;

import java.util.List;

public record BookingPageCache(
    List<Booking> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {}
