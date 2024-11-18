package com.pixellabs.reservation.application.useCases.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

import com.pixellabs.reservation.application.useCases.bookings.dtos.GetListBookingRequest;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.bookingAggregate.exceptions.BookingNotFoundException;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;

import jakarta.transaction.Transactional;

@Component
public class GetListBookingUseCase {
	@Autowired
	private final BookingRepository bookingRepository;
	
	public GetListBookingUseCase(BookingRepository bookingRepository) {
		super();
		this.bookingRepository = bookingRepository;
	}
	
	@Transactional
	public Page<Booking> execute(GetListBookingRequest request) throws BookingNotFoundException, ReservationDomainException {		
		Sort sort = Sort.by(request.getOrderBy());
		
		if(request.getOrder() == "asc") {
			sort.ascending();
		} else {
			sort.descending();
		}
		
		Pageable page = PageRequest.of(request.getPage(), request.getPageSize(), sort);
		return bookingRepository.findAll(page);
	}
}
