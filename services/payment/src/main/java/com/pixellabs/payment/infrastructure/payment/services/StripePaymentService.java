package com.pixellabs.payment.infrastructure.payment.services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.pixellabs.payment.infrastructure.payment.configs.PaymentProperties;
import com.stripe.StripeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentIntentCreateParams;

@Service
@RequiredArgsConstructor
public class StripePaymentService {
    private final StripeClient stripeClient;
    private final PaymentProperties paymentProperties;

    public PaymentIntent createIntent(
        UUID bookingId,
        UUID userId,
        BigDecimal amount,
        String customerId) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount.movePointRight(2).longValueExact())
            .setCurrency(paymentProperties.currency())
            .setCustomer(customerId)
            .putMetadata("bookingId", bookingId.toString())
            .putMetadata("userId", userId.toString())
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
                )
                .build();
        return stripeClient.v1()
            .paymentIntents()
            .create(params);
    }

    public Optional<Customer> getCustomerByEmail(String email) throws StripeException {
        CustomerListParams params = CustomerListParams.builder()
            .setEmail(email)
            .build();

        return stripeClient.v1()
            .customers()
            .list(params)
            .getData()
            .stream()
            .findFirst();
    }

    public Customer createCustomer(
        UUID userId,
        String firstName,
        String lastName,
        String email) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .setName(firstName + " " + lastName)
            .putMetadata("userId", userId.toString())
            .build();
        return stripeClient.v1()
            .customers()
            .create(params);
    }
}
