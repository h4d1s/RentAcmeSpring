package com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking;

import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.reservation.infrastructure.grpc.InventoryGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReserveBookingHandler {
	private final BookingRepository bookingRepository;
	private final InventoryGrpcClient inventoryGrpcClient;

	@CacheEvict(value = "bookings", allEntries = true)
	@Transactional
	public Booking handle(ReserveBookingCommand command)
			throws NotFoundException, ReservationDomainException {
		Booking booking = new Booking(
			command.vehicleId(),
			command.userId(),
			command.pickupDate(),
			command.returnDate());

		GetVehicleResponse response = inventoryGrpcClient.getVehicle(booking.getVehicleId().toString());
        if(response == null) {
			throw new IllegalStateException("Inventory service returned null response");
		}

        booking.updatePrice(BigDecimal.valueOf(response.getRentalPricePerDay()));
		return bookingRepository.save(booking);
	}
}
