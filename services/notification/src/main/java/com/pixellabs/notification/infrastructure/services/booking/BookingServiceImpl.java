package com.pixellabs.notification.infrastructure.services.booking;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pixellabs.eventbus.BusConstants;
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
		GetBookingIntegrationRequest request = new GetBookingIntegrationRequest(id);
		GetBookingIntegrationResponse response = (GetBookingIntegrationResponse) integrationEventService.sendAndReceive(request, BusConstants.EXCHANGE_BOOKING, BusConstants.ROUTING_KEY_BOOKING_GET_REQUEST);
		BookingDTO bookingDTO = BookingDTOMapper.INSTANCE.getBookingIntegrationResponseToBookingDTO(response);
		
		return bookingDTO;
	}

}
