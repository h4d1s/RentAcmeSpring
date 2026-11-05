package com.pixellabs.inventory.domain.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class BadRequestException extends RuntimeException {

    private final Map<String, String[]> validationErrors;

    public BadRequestException(String message) {
        super(message);
        this.validationErrors = null;
    }

    public BadRequestException(
            String message,
            Map<String, String[]> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = null;
    }
}
