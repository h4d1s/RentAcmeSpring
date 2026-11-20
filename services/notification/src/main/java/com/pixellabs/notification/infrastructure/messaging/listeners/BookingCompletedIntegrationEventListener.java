package com.pixellabs.notification.infrastructure.messaging.listeners;

import com.pixellabs.libs.eventbus.events.BookingCompletedIntegrationEvent;
import com.pixellabs.libs.reservation.grpc.GetBookingResponse;
import com.pixellabs.libs.user.grpc.UserGrpcClientService;
import com.pixellabs.libs.user.grpc.models.UserResponse;
import com.pixellabs.libs.reservation.grpc.ReservationGrpcClientService;
import com.pixellabs.notification.infrastructure.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.pixellabs.libs.eventbus.Queues;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCompletedIntegrationEventListener {
    private final EmailService emailService;
    private final ReservationGrpcClientService bookingService;
	private final UserGrpcClientService userService;

	@RabbitListener(queues = Queues.BOOKING_COMPLETED)
	public void handle(BookingCompletedIntegrationEvent event) {
		log.info("BookingCompletedIntegrationEvent {}", event);
		GetBookingResponse bookingResponse = bookingService
			.getBooking(event.bookingId());
		UserResponse userResponse = userService
			.getUser(UUID.fromString(bookingResponse.getUserId()));

		emailService.send(
			userResponse.email(),
			"Booking completed",
			"Booking successfully completed.");
    }
}
