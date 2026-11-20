package com.pixellabs.libs.reservation.grpc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationGrpcClientService {
    private final BookingProtoServiceGrpc.BookingProtoServiceBlockingStub bookingStub;

    public GetBookingResponse getBooking(UUID id) {
        GetBookingRequest request = GetBookingRequest.newBuilder()
            .setId(id.toString())
            .build();
        return bookingStub.getBooking(request);
    }
}
