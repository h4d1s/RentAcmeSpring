package com.pixellabs.reservation.application.booking.commands;

import com.pixellabs.inventory.grpc.GetVehicleResponse;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingHandler;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingRepository;
import com.pixellabs.reservation.domain.exceptions.ReservationDomainException;
import com.pixellabs.reservation.infrastructure.grpc.InventoryGrpcClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReserveBookingCommandHandlerTests {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private InventoryGrpcClient inventoryGrpcClient;

    @InjectMocks
    private ReserveBookingHandler handler;

    @Test
    void handle_WhenVehicleExists_ShouldReserveBooking() throws ReservationDomainException {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LocalDateTime pickupDate = LocalDateTime.now().plusDays(1);
        LocalDateTime returnDate = pickupDate.plusDays(3);

        ReserveBookingCommand command = new ReserveBookingCommand(
            vehicleId,
            userId,
            pickupDate,
            returnDate
        );

        GetVehicleResponse response = GetVehicleResponse.newBuilder()
            .setRentalPricePerDay(100.0)
            .build();

        when(inventoryGrpcClient.getVehicle(vehicleId.toString()))
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

        verify(inventoryGrpcClient).getVehicle(vehicleId.toString());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void handle_WhenInventoryServiceReturnsNull_ShouldThrowIllegalStateException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        when(inventoryGrpcClient.getVehicle(command.vehicleId().toString()))
            .thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(IllegalStateException.class);

        verify(inventoryGrpcClient).getVehicle(command.vehicleId().toString());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void handle_WhenInventoryServiceFails_ShouldPropagateException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        when(inventoryGrpcClient.getVehicle(command.vehicleId().toString()))
            .thenThrow(new RuntimeException("Inventory service unavailable"));

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(RuntimeException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void handle_WhenPickupDateIsInvalid_ShouldThrowReservationDomainException() {
        // Arrange
        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(2)
        );

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);

        verifyNoInteractions(inventoryGrpcClient);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void handle_WhenReturnDateIsBeforePickupDate_ShouldThrowReservationDomainException() {
        // Arrange
        LocalDateTime pickupDate = LocalDateTime.now().plusDays(3);
        LocalDateTime returnDate = LocalDateTime.now().plusDays(1);

        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            pickupDate,
            returnDate
        );

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);

        verifyNoInteractions(inventoryGrpcClient);
        verifyNoInteractions(bookingRepository);
    }

    @Test
    void handle_WhenRentalPriceIsZero_ShouldThrowReservationDomainException() {
        // Arrange
        ReserveBookingCommand command = createValidCommand();

        GetVehicleResponse response = GetVehicleResponse.newBuilder()
            .setRentalPricePerDay(0.0)
            .build();

        when(inventoryGrpcClient.getVehicle(command.vehicleId().toString()))
            .thenReturn(response);

        // Act & Assert
        assertThatThrownBy(() -> handler.handle(command))
            .isInstanceOf(ReservationDomainException.class);

        verify(bookingRepository, never()).save(any());
    }

    private ReserveBookingCommand createValidCommand() {
        LocalDateTime pickupDate = LocalDateTime.now().plusDays(1);

        return new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            pickupDate,
            pickupDate.plusDays(3)
        );
    }
}
