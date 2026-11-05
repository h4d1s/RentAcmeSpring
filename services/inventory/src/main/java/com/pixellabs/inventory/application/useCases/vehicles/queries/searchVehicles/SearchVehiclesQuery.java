package com.pixellabs.inventory.application.useCases.vehicles.queries.searchVehicles;

import com.pixellabs.inventory.domain.modelAggregate.Category;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SearchVehiclesQuery(
    Gearbox gearbox,
    LocalDateTime pickupDate,
    LocalDateTime returnDate,
    BigDecimal rentalPricePerDayFrom,
    BigDecimal rentalPricePerDayTo,
    Category category,
    String orderBy,
    String order,
    Integer page,
    Integer pageSize
) {
    public SearchVehiclesQuery {
        orderBy = orderBy == null ? "" : orderBy;
        order = order == null ? "" : order;
        page = page != null ? Math.max(page, 1) : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}
