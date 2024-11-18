package com.pixellabs.payment.infrastructure.services.gateway;

import java.math.BigDecimal;

import com.pixellabs.payment.application.models.CustomerDTO;
import com.stripe.exception.StripeException;

public interface PaymentGateway {
    void charge(String customerId, BigDecimal amount) throws StripeException;
    CustomerDTO getCustomerByEmail(String email) throws StripeException;
    boolean createCustomer(String firstName, String lastName, String email) throws StripeException;
}
