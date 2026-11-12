package com.pixellabs.eventbus;

public final class RoutingKeys {
    private RoutingKeys() {}

    public static final String BOOKING_RESERVED = "booking.reserved";
    public static final String BOOKING_CANCELED = "booking.canceled";
    public static final String BOOKING_COMPLETED = "booking.completed";
    public static final String BOOKING_GET_REQUEST = "booking.get.request";

    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String PAYMENT_FAILED = "payment.failed";
    public static final String PAYMENT_COMPLETE_COMMAND = "payment.complete.command";

    public static final String USER_CREATED = "user.created";
    public static final String USER_CHECK_REQUEST = "user.check.request";
    public static final String USER_GET_REQUEST = "user.get.request";

    public static final String VEHICLE_GET_REQUEST = "vehicle.get.request";
    public static final String VEHICLE_CHECK_REQUEST = "vehicle.check.request";
    public static final String VEHICLE_LOCKED = "vehicle.locked";
    public static final String VEHICLE_UNLOCKED = "vehicle.unlocked";
    public static final String VEHICLE_UNAVAILABLE = "vehicle.unavailable";

    public static final String KEYCLOAK_CLIENT_EVENTS = "KK.EVENT.CLIENT.rent-acme.#";
    public static final String KEYCLOAK_ADMIN_EVENTS = "KK.EVENT.ADMIN.rent-acme.#";
}
