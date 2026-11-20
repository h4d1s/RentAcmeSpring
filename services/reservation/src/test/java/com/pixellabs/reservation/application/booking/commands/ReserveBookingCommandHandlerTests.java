package com.pixellabs.reservation.application.booking.commands;

import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingHandler;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import com.pixellabs.reservation.infrastructure.grpc.client.inventory.InventoryGrpcClientService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReserveBookingCommandHandlerTests {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private InventoryGrpcClientService inventoryGrpcClientService;

    @InjectMocks
    private ReserveBookingHandler handler;

    @Test
    void handle_WhenVehicleExists_ShouldReserveBooking() throws ReservationDomainException {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Instant pickupDate = Instant.now().plus(1, ChronoUnit.DAYS);
        Instant returnDate = pickupDate.plus(3, ChronoUnit.DAYS);

        ReserveBookingCommand command = new ReserveBookingCommand(
            vehicleId,
            userId,
            pickupDate,
            returnDate
        );

        GetVehicleResponse response = GetVehicleResponse.newBuilder()
            .setRentalPricePerDay(100.0)
            .build();

        when(inventoryGrpcClientService.getVehicle(vehicleId))
            .thenReturn(response);
        when(bookingRepository.save(any(Booking.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Booking result = handler.handle(command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVehicleId()).isEqualTo(vehicleId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getPrice()).isEqualByComparingTo(
            new BigDecimal("400.0")
        );
    }

    @Test
    void handle_WhenInventoryServiceReturnsNull_ShouldThrowIllegalStateException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        when(inventoryGrpcClientService.getVehicle(command.vehicleId()))
            .thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void handle_WhenInventoryServiceFails_ShouldPropagateException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        when(inventoryGrpcClientService.getVehicle(command.vehicleId()))
            .thenThrow(new RuntimeException("Inventory service unavailable"));

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void handle_WhenPickupDateIsInvalid_ShouldThrowReservationDomainException() {
        // Arrange
        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            Instant.now().minus(1, ChronoUnit.DAYS),
            Instant.now().plus(2, ChronoUnit.DAYS)
        );

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);
    }

    @Test
    void handle_WhenReturnDateIsBeforePickupDate_ShouldThrowReservationDomainException() {
        // Arrange
        Instant pickupDate = Instant.now().plus(3, ChronoUnit.DAYS);
        Instant returnDate = Instant.now().plus(1, ChronoUnit.DAYS);

        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            pickupDate,
            returnDate
        );

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);
    }

    @Test
    void handle_WhenRentalPriceIsZero_ShouldThrowReservationDomainException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        GetVehicleResponse response = GetVehicleResponse.newBuilder()
            .setRentalPricePerDay(0.0)
            .build();

        when(inventoryGrpcClientService.getVehicle(command.vehicleId()))
            .thenReturn(response);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);
    }

    private ReserveBookingCommand createValidCommand() {
        Instant pickupDate = Instant.now().plus(1, ChronoUnit.DAYS);

        return new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            pickupDate,
            pickupDate.plus(3, ChronoUnit.DAYS)
        );
    }
}
