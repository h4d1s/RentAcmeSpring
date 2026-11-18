package com.pixellabs.payment.domain.paymentAggregate;

import com.pixellabs.payment.domain.exceptions.PaymentDomainException;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentCanceledDomainEvent;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentCreatedDomainEvent;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentFailedDomainEvent;
import com.pixellabs.payment.domain.paymentAggregate.events.PaymentIntentSucceededDomainEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AbstractAggregateRoot<Payment> {
    @Id
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID bookingId;
    private UUID userId;
    @Column(nullable = false, unique = true)
    private String stripePaymentIntentId;
    private Long amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Instant statusChangedAt;

    public Payment(
        UUID bookingId,
        UUID userId,
        String stripePaymentIntentId,
        Long amount,
        String currency
    ) throws PaymentDomainException {
        super();

        this.id = UUID.randomUUID();
        updateBookingId(bookingId);
        updateUserId(userId);
        updateStripePaymentIntentId(stripePaymentIntentId);
        updateAmount(amount);
        updateCurrency(currency);
        this.status = PaymentStatus.PENDING;

        registerEvent(new PaymentIntentCreatedDomainEvent(this));
    }

    public void updateBookingId(UUID bookingId)
        throws PaymentDomainException {
        validateBookingId(bookingId);
        this.bookingId = bookingId;
    }

    public void updateUserId(UUID userId)
        throws PaymentDomainException {
        validateUserId(userId);
        this.userId = userId;
    }

    public void updateStripePaymentIntentId(String stripePaymentIntentId)
        throws PaymentDomainException {
        validateStripePaymentIntentId(stripePaymentIntentId);
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public void updateAmount(Long amount)
        throws PaymentDomainException {
        validateAmount(amount);
        this.amount = amount;
    }

    public void updateCurrency(String currency)
        throws PaymentDomainException {
        validateCurrency(currency);
        this.currency = currency;
    }

    public void markAsSucceeded() throws PaymentDomainException {
        if (status != PaymentStatus.PENDING) {
            statusChangeException(PaymentStatus.SUCCEEDED);
        }

        this.status = PaymentStatus.SUCCEEDED;
        this.statusChangedAt = Instant.now();

        registerEvent(new PaymentIntentSucceededDomainEvent(this));
    }

    public void markAsFailed() throws PaymentDomainException {
        if (status != PaymentStatus.PENDING) {
            statusChangeException(PaymentStatus.FAILED);
        }

        this.status = PaymentStatus.FAILED;

        registerEvent(new PaymentIntentFailedDomainEvent(this));
    }

    public void markAsCanceled() throws PaymentDomainException {
        if (status != PaymentStatus.PENDING) {
            statusChangeException(PaymentStatus.CANCELED);
        }

        this.status = PaymentStatus.CANCELED;

        registerEvent(new PaymentIntentCanceledDomainEvent(this));
    }

    private void validateBookingId(UUID bookingId)
        throws PaymentDomainException {
        if (bookingId == null) {
            throw new PaymentDomainException("Booking ID cannot be null.");
        }
    }

    private void validateUserId(UUID userId)
        throws PaymentDomainException {
        if (userId == null) {
            throw new PaymentDomainException("User ID cannot be null.");
        }
    }

    private void validateStripePaymentIntentId(String stripePaymentIntentId)
        throws PaymentDomainException {
        if (stripePaymentIntentId == null || stripePaymentIntentId.isBlank()) {
            throw new PaymentDomainException(
                "Stripe PaymentIntent ID cannot be null or blank."
            );
        }
    }

    private void validateAmount(Long amount) throws PaymentDomainException {
        if (amount == null || amount <= 0) {
            throw new PaymentDomainException(
                "Payment amount must be greater than zero."
            );
        }
    }

    private void validateCurrency(String currency) throws PaymentDomainException {
        if (currency == null || !currency.matches("[a-zA-Z]{3}")) {
            throw new PaymentDomainException(
                "Currency must be a valid three-letter currency code."
            );
        }
    }

    private void statusChangeException(PaymentStatus statusToChange)
        throws PaymentDomainException {
        throw new PaymentDomainException(
            "It is not possible to change the payment status from "
                + status + " to " + statusToChange + "."
        );
    }
}