package com.pixellabs.notification.infrastructure.messaging.listeners;

import com.pixellabs.libs.reservation.grpc.GetBookingResponse;
import com.pixellabs.libs.user.grpc.UserGrpcClientService;
import com.pixellabs.libs.user.grpc.models.UserResponse;
import com.pixellabs.libs.reservation.grpc.ReservationGrpcClientService;
import com.pixellabs.notification.infrastructure.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.pixellabs.libs.eventbus.Queues;
import com.pixellabs.libs.eventbus.events.BookingCanceledIntegrationEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingCanceledIntegrationEventListener {
	private final EmailService emailService;
	private final ReservationGrpcClientService bookingService;
	private final UserGrpcClientService userService;
	
	@RabbitListener(queues = Queues.BOOKING_CANCELED)
	public void handle(BookingCanceledIntegrationEvent event) {
		GetBookingResponse bookingResponse = bookingService
			.getBooking(event.bookingId());
		UserResponse userResponse = userService
			.getUser(UUID.fromString(bookingResponse.getUserId()));

		emailService.send(
			userResponse.email(),
			"Booking canceled",
			"Booking canceled.");
    }
}
