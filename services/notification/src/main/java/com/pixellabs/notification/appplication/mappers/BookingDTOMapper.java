package com.pixellabs.notification.appplication.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.eventbus.responses.GetBookingIntegrationResponse;
import com.pixellabs.notification.appplication.model.BookingDTO;

@Mapper(componentModel = "spring")
public interface BookingDTOMapper {
	BookingDTOMapper INSTANCE = Mappers.getMapper(BookingDTOMapper.class);
	
	GetBookingIntegrationResponse bookingDTOToGetBookingIntegrationResponse(BookingDTO bookingDTO);
	BookingDTO getBookingIntegrationResponseToBookingDTO(GetBookingIntegrationResponse response);
}
