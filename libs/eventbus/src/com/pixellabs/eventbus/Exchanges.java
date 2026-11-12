package com.pixellabs.eventbus;

public final class Exchanges {
    private Exchanges() {}

    public static final String BOOKING = "booking.exchange";
    public static final String PAYMENT = "payment.exchange";
    public static final String USER = "user.exchange";
    public static final String INVENTORY = "inventory.exchange";
    public static final String KEYCLOAK = "amq.topic";
}
