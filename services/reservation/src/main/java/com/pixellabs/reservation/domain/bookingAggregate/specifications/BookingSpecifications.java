package com.pixellabs.reservation.domain.bookingAggregate.specifications;

import com.pixellabs.reservation.domain.bookingAggregate.Booking;
import com.pixellabs.reservation.domain.bookingAggregate.BookingStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingSpecifications {
    public static Specification<Booking> withFilters(
        UUID vehicleId,
        List<UUID> userIds,
        LocalDateTime pickupDate,
        LocalDateTime returnDate,
        BookingStatus status
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (vehicleId != null) {
                predicates.add(
                    cb.equal(root.get("vehicleId"), vehicleId)
                );
            }

            if (userIds != null && !userIds.isEmpty()) {
                predicates.add(
                    root.get("userId").in(userIds)
                );
            }

            if (pickupDate != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("pickupDate"), pickupDate
                    )
                );
            }

            if (returnDate != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("returnDate"), returnDate
                    )
                );
            }

            if (status != null) {
                predicates.add(
                    cb.equal(root.get("status"), status)
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
