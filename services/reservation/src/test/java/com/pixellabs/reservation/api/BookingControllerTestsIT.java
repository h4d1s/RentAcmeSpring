package com.pixellabs.reservation.api;

import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.cancelBoooking.CancelBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking.CompleteBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.completeBooking.CompleteBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingCommand;
import com.pixellabs.reservation.application.useCases.bookings.commands.reserveBooking.ReserveBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingHandler;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBooking.GetBookingQuery;
import com.pixellabs.reservation.application.useCases.bookings.queries.getBookingList.GetBookingListHandler;
import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.infrastructure.grpc.InventoryGrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingControllerTestsIT extends BookingIntegrationTestBaseIT {
    private static final String BASE_URL = "/api/v1/bookings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CancelBookingHandler cancelBookingHandler;

    @MockitoBean
    private CompleteBookingHandler completeBookingHandler;

    @MockitoBean
    private GetBookingHandler getBookingHandler;

    @MockitoBean
    private GetBookingListHandler getBookingListHandler;

    @MockitoBean
    private ReserveBookingHandler reserveBookingHandler;

    @MockitoBean
    private InventoryGrpcClient inventoryGrpcClient;

    @BeforeEach
    void setUp() {
        when(inventoryGrpcClient.checkVehicle(any())).thenReturn(true);
    }

    // GET /api/v1/bookings

    @Test
    void getAllBookings_WhenAuthorized_ShouldReturnOk() throws Exception {
        when(getBookingListHandler.handle(any()))
            .thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
            .with(csrf())
            .with(authentication(
                TestAuthentication.userWithAllBookingPermissions()
            )))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            ))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalElements").value(0));

        verify(getBookingListHandler).handle(any());
    }

    @Test
    void getAllBookings_WhenUserHasNoPermission_ShouldReturnForbidden()
        throws Exception {
        mockMvc.perform(get(BASE_URL)
                .with(authentication(
                    TestAuthentication.userWithoutPermissions()
                )))
            .andExpect(status().isForbidden());

        verifyNoInteractions(getBookingListHandler);
    }

    @Test
    void getAllBookings_WhenUnauthenticated_ShouldReturnUnauthorized()
        throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(getBookingListHandler);
    }

    // GET /api/v1/bookings/{id}

    @Test
    void getBooking_WhenAuthorizedAndExists_ShouldReturnOk()
        throws Exception {
        UUID bookingId = UUID.randomUUID();

        Booking booking = new Booking(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().plusMinutes(5),
            LocalDateTime.now().plusDays(3)
        );

        when(getBookingHandler.handle(any(GetBookingQuery.class)))
            .thenReturn(booking);

        mockMvc.perform(get(BASE_URL + "/{id}", bookingId)
                .with(authentication(
                    TestAuthentication.userWithAllBookingPermissions()
                )))
            .andExpect(status().isOk());

        verify(getBookingHandler).handle(any(GetBookingQuery.class));
    }

    @Test
    void getBooking_WhenIdIsInvalid_ShouldReturnBadRequest()
        throws Exception {

        mockMvc.perform(get(BASE_URL + "/{id}", "invalid-id")
                .with(authentication(
                    TestAuthentication.userWithAllBookingPermissions()
                )))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(getBookingHandler);
    }

    // POST /api/v1/bookings

    @Test
    void reserveBooking_WhenAuthorizedAndValid_ShouldReturnCreated()
        throws Exception {
        Booking booking = new Booking(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().plusMinutes(5),
            LocalDateTime.now().plusDays(3)
        );

        when(reserveBookingHandler.handle(any(ReserveBookingCommand.class)))
            .thenReturn(booking);

        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().plusMinutes(5),
            LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post(BASE_URL)
                .with(authentication(
                    TestAuthentication.userWithAllBookingPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").value(
                booking.getId().toString()
            ));

        verify(reserveBookingHandler)
            .handle(any(ReserveBookingCommand.class));
    }

    @Test
    void reserveBooking_WhenUserHasNoPermission_ShouldReturnForbidden()
        throws Exception {
        ReserveBookingCommand command = new ReserveBookingCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            LocalDateTime.now().plusMinutes(5),
            LocalDateTime.now().plusDays(2)
        );

        mockMvc.perform(post(BASE_URL)
                .with(authentication(
                    TestAuthentication.userWithoutPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isForbidden());

        verifyNoInteractions(reserveBookingHandler);
    }

    // POST /api/v1/bookings/cancel

    @Test
    void cancelBooking_WhenAuthorizedAndValid_ShouldReturnOk()
        throws Exception {
        CancelBookingCommand command = new CancelBookingCommand(UUID.randomUUID());

        mockMvc.perform(post(BASE_URL + "/cancel")
                .with(authentication(
                    TestAuthentication.userWithAllBookingPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isOk());

        verify(cancelBookingHandler)
            .handle(any(CancelBookingCommand.class));
    }

    @Test
    void cancelBooking_WhenUserHasNoPermission_ShouldReturnForbidden()
        throws Exception {
        CancelBookingCommand command = new CancelBookingCommand(UUID.randomUUID());
        mockMvc.perform(post(BASE_URL + "/cancel")
                .with(authentication(
                    TestAuthentication.userWithoutPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isForbidden());

        verifyNoInteractions(cancelBookingHandler);
    }

    // POST /api/v1/bookings/complete

    @Test
    void completeBooking_WhenAuthorizedAndValid_ShouldReturnOk()
        throws Exception {
        CompleteBookingCommand command = new CompleteBookingCommand(UUID.randomUUID());
        mockMvc.perform(post(BASE_URL + "/complete")
                .with(authentication(
                    TestAuthentication.userWithAllBookingPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isOk());

        verify(completeBookingHandler)
            .handle(any(CompleteBookingCommand.class));
    }

    @Test
    void completeBooking_WhenUserHasNoPermission_ShouldReturnForbidden()
        throws Exception {
        CompleteBookingCommand command = new CompleteBookingCommand(UUID.randomUUID());
        mockMvc.perform(post(BASE_URL + "/complete")
                .with(authentication(
                    TestAuthentication.userWithoutPermissions()
                ))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
            .andExpect(status().isForbidden());

        verifyNoInteractions(completeBookingHandler);
    }
}
