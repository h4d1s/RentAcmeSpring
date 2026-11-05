package com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicleList;

import com.pixellabs.inventory.domain.variantAggregate.Gearbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetVehicleListQuery(
    Gearbox gearbox,
    Integer yearOfProduction,
    Integer power,
    Integer engineSize,
    LocalDateTime pickupDate,
    LocalDateTime returnDate,
    BigDecimal rentalPricePerDayFrom,
    BigDecimal rentalPricePerDayTo,
    UUID modelId,

    String orderBy,
    String order,

    Integer page,
    Integer pageSize
) {
    public GetVehicleListQuery {
        orderBy = orderBy == null ? "" : orderBy;
        order = order == null ? "" : order;
        page = page != null ? Math.max(page, 1) : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}