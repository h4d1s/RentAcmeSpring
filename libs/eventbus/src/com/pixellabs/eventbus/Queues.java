package com.pixellabs.eventbus;

public class Queues {
    private Queues() {}

    public static final String BOOKING_RESERVED = "booking.reserved.queue";
    public static final String BOOKING_CANCELED = "booking.canceled.queue";
    public static final String BOOKING_COMPLETED = "booking.completed.queue";
    public static final String BOOKING_FAILED = "booking.failed.queue";
    public static final String BOOKING_GET_REQUEST = "booking.get.request.queue";
    
    public static final String PAYMENT_COMPLETE_COMMAND = "payment.complete.command.queue";
    public static final String PAYMENT_COMPLETED = "payment.completed.queue";
    public static final String PAYMENT_FAILED = "payment.failed.queue";
    public static final String CREATE_PAYMENT_INTENT_COMMAND = "payment.create.intent.command.queue";
    
    public static final String USER_CREATED = "user.created.queue";
    public static final String USER_CHECK_REQUEST = "user.check.request.queue";
    public static final String USER_GET_REQUEST = "user.get.request.queue";

    public static final String VEHICLE_GET_REQUEST = "vehicle.get.request.queue";
    public static final String VEHICLE_CHECK_REQUEST = "vehicle.check.request.queue";
    public static final String VEHICLE_LOCK_COMMAND = "vehicle.lock.command.queue";
    public static final String VEHICLE_UNLOCK_COMMAND = "vehicle.unlock.command.queue";

    public static final String KEYCLOAK_CLIENT_EVENTS = "keycloak-client-event-queue";
}
