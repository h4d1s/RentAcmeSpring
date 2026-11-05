package com.pixellabs.reservation.application.useCases.bookings.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetListBookingRequest {
	@NotBlank
	private String order;
	@NotBlank
	private String orderBy;
	@Min(value = 0)
	private int page;
	@Positive
	private int pageSize;
}
