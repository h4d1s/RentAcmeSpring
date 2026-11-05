package com.pixellabs.inventory.domain.vehicleAggregate;

import java.math.BigDecimal;
import java.util.UUID;

import com.pixellabs.inventory.domain.modelAggregate.Category;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    @Query("""
        SELECT v FROM Vehicle v
            JOIN FETCH v.variant variant
            JOIN FETCH variant.model model
            JOIN FETCH model.brand
        WHERE v.status = :status
            AND (:priceFrom IS NULL OR v.rentalPricePerDay >= :priceFrom)
            AND (:priceTo IS NULL OR v.rentalPricePerDay <= :priceTo)
    """)
    Page<Vehicle> findAvailableVehicles(
        @Param("status") VehicleStatus status,
        @Param("priceFrom") BigDecimal priceFrom,
        @Param("priceTo") BigDecimal priceTo,
        Pageable pageable
    );

    @Query("""
        SELECT v FROM Vehicle v
            JOIN FETCH v.variant variant
            JOIN FETCH variant.model model
            JOIN FETCH model.brand
        WHERE v.status = :status
            AND (:gearbox IS NULL OR variant.gearbox = :gearbox)
            AND (:category IS NULL OR model.category = :category)
            AND (:priceFrom IS NULL OR v.rentalPricePerDay >= :priceFrom)
            AND (:priceTo IS NULL OR v.rentalPricePerDay <= :priceTo)
    """)
    Page<Vehicle> searchVehicles(
        VehicleStatus status,
        Gearbox gearbox,
        Category category,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        Pageable pageable
    );
}
