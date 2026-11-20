package com.pixellabs.reservation.infrastructure.grpc.server.booking;

import com.pixellabs.libs.reservation.grpc.BookingProtoServiceGrpc;
import com.pixellabs.libs.reservation.grpc.GetBookingRequest;
import com.pixellabs.libs.reservation.grpc.GetBookingResponse;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class BookingGrpcServerService
    extends BookingProtoServiceGrpc.BookingProtoServiceImplBase {
    private final BookingRepository bookingRepository;

    @PreAuthorize("@bookingAuthorization.canView(authentication, #request.id)")
    @Override
    public void getBooking(
        GetBookingRequest request,
        StreamObserver<GetBookingResponse> responseObserver) {
        UUID id = UUID.fromString(request.getId());
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Booking not found."));

        var response = GetBookingResponse.newBuilder()
            .setId(booking.getId().toString())
            .setVehicleId(booking.getVehicleId().toString())
            .setUserId(booking.getUserId().toString())
            .setPickupAt(booking.getPickupAt().toString())
            .setReturnAt(booking.getReturnAt().toString())
            .setBookingAt(booking.getBookingAt().toString())
            .setPrice(booking.getPrice().toString())
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
