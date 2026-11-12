package com.pixellabs.notification.infrastructure.services.booking;

import java.util.UUID;

import com.pixellabs.eventbus.Exchanges;
import com.pixellabs.eventbus.RoutingKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.requests.GetBookingIntegrationRequest;
import com.pixellabs.eventbus.responses.GetBookingIntegrationResponse;
import com.pixellabs.notification.appplication.mappers.BookingDTOMapper;
import com.pixellabs.notification.appplication.model.BookingDTO;
import com.pixellabs.notification.infrastructure.rabbitmq.IntegrationEventService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	public IntegrationEventService integrationEventService;
	
	public BookingServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public BookingDTO get(UUID id) {
		GetBookingIntegrationRequest request = new GetBookingIntegrationRequest(UUID.randomUUID(), id);
		GetBookingIntegrationResponse response = (GetBookingIntegrationResponse) integrationEventService.sendAndReceive(
			request,
			Exchanges.BOOKING,
			RoutingKeys.BOOKING_GET_REQUEST);
		BookingDTO bookingDTO = BookingDTOMapper.INSTANCE.getBookingIntegrationResponseToBookingDTO(response);
		
		return bookingDTO;
	}

}
