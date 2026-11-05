package com.pixellabs.inventory.domain.variantAggregate;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, UUID> {
}
