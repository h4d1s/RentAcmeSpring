package com.pixellabs.inventory.domain.modelAggregate;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, UUID> {
}
