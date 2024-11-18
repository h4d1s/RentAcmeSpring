package com.pixellabs.reservation.domain.bookingAggregate;

public enum BookingStatus {
    RESERVED(1),
    CANCELED(2),
    COMPLETED(3);

    private final int code;

    BookingStatus(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
