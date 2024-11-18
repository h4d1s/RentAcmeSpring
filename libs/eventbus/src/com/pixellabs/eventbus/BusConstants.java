package com.pixellabs.eventbus;

public class BusConstants {
	// Define exchange names
    public static final String EXCHANGE_BOOKING = "booking.exchange";
    public static final String EXCHANGE_PAYMENT = "payment.exchange";
    public static final String EXCHANGE_USER = "user.exchange";
    public static final String EXCHANGE_INVENTORY = "inventory.exchange";

    // Define queue names
    public static final String QUEUE_BOOKING_RESERVED = "booking.reserved.queue";
    public static final String QUEUE_BOOKING_CANCELED = "booking.canceled.queue";
    public static final String QUEUE_BOOKING_COMPLETED = "booking.completed.queue";
    public static final String QUEUE_BOOKING_FAILED = "booking.failed.queue";
    public static final String QUEUE_BOOKING_GET_REQUEST = "booking.get.request.queue";
    
    public static final String QUEUE_PAYMENT_COMPLETE_COMMAND = "payment.complete.command.queue";
    public static final String QUEUE_PAYMENT_COMPLETED = "payment.completed.queue";
    public static final String QUEUE_PAYMENT_FAILED = "payment.failed.queue";
    
    public static final String QUEUE_USER_CREATED = "user.created.queue";
    public static final String QUEUE_USER_CHECK_REQUEST = "user.check.request.queue";
    public static final String QUEUE_USER_GET_REQUEST = "user.get.request.queue";

    public static final String QUEUE_VEHICLE_GET_REQUEST = "vehicle.get.request.queue";
    public static final String QUEUE_VEHICLE_CHECK_REQUEST = "vehicle.check.request.queue";
    
    // Define routing keys
    public static final String ROUTING_KEY_BOOKING_RESERVED = "booking.reserved.routing.key";
    public static final String ROUTING_KEY_BOOKING_CANCELED = "booking.canceled.routing.key";
    public static final String ROUTING_KEY_BOOKING_COMPLETED = "booking.completed.routing.key";
    public static final String ROUTING_KEY_BOOKING_GET_REQUEST = "booking.get.request.routing.key";
    
    public static final String ROUTING_KEY_PAYMENT_COMPLETED = "payment.completed.routing.key";
    public static final String ROUTING_KEY_PAYMENT_FAILED = "payment.failed.routing.key";
    public static final String ROUTING_KEY_PAYMENT_COMPLETE_COMMAND = "payment.complete.command.routing.key";
    
    public static final String ROUTING_KEY_USER_CREATED = "user.created.routing.key";
    public static final String ROUTING_KEY_USER_CHECK_REQUEST = "user.check.request.routing.key";
    public static final String ROUTING_KEY_USER_GET_REQUEST = "user.get.request.routing.key";
    
    public static final String ROUTING_KEY_VEHICLE_GET_REQUEST = "vehicle.get.request.routing.key";
    public static final String ROUTING_KEY_VEHICLE_CHECK_REQUEST = "vehicle.check.request.routing.key";
}
