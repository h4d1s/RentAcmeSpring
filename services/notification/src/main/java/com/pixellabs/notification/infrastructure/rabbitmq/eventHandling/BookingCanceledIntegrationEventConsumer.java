package com.pixellabs.notification.infrastructure.rabbitmq.eventHandling;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
import com.pixellabs.eventbus.events.BookingCanceledIntegrationEvent;
import com.pixellabs.notification.appplication.model.BookingDTO;
import com.pixellabs.notification.appplication.model.UserDTO;
import com.pixellabs.notification.infrastructure.services.booking.BookingService;
import com.pixellabs.notification.infrastructure.services.email.EmailService;
import com.pixellabs.notification.infrastructure.services.user.UserService;

@Service
public class BookingCanceledIntegrationEventConsumer {

    @Autowired
    public EmailService emailService;
    
    @Autowired
    public BookingService bookingService;
    
    @Autowired
    public UserService userService;
	
	@RabbitListener(queues = BusConstants.QUEUE_BOOKING_RESERVED)
	public void receive(BookingCanceledIntegrationEvent event) {
		BookingDTO bookingDTO = bookingService.get(event.getBookingId());
		UserDTO userDTO = userService.get(bookingDTO.getUserId());
		
		emailService.send(userDTO.getEmail(), "Booking canceled", "Booking canceled.");
	}
	
}
