package com.pixellabs.payment.infrastructure.services.gateway;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pixellabs.payment.application.models.CustomerDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentIntentCreateParams.AutomaticPaymentMethods;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripePaymentGatewayImpl implements PaymentGateway {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGatewayImpl.class);

    @Override
    public void charge(String customerId, BigDecimal amount) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount.longValue())
                        .setCurrency("usd")
                        .setCustomer(customerId)
                        .setAutomaticPaymentMethods(
                                AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .setConfirm(true)
                        .setOffSession(true)
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);
        logger.info("Payment Intent created: {}", intent.getId());
    }

    @Override
    public CustomerDTO getCustomerByEmail(String email) throws StripeException {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(email)
                .build();

        List<Customer> customers = Customer.list(params).getData();

        if (customers.isEmpty()) {
            return null;
        }

        Customer stripeCustomer = customers.get(0);

        return new CustomerDTO(stripeCustomer.getId(), stripeCustomer.getEmail(), stripeCustomer.getName());
    }

    @Override
    public boolean createCustomer(String firstName, String lastName, String email) {
        logger.info("Creating customer in Stripe.");

        try {
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(firstName + " " + lastName)
                    .build();

            Customer customer = Customer.create(params);

            logger.info("Customer successfully created: {}", customer.getId());
            return true;
        } catch (StripeException e) {
            logger.error("An error occurred during customer creation: {}", e.getMessage());
            return false;
        }
    }
}
