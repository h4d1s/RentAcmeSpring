package com.pixellabs.inventory.domain.brandAggregate;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository 
	extends
		JpaRepository<Brand, UUID>,
		PagingAndSortingRepository<Brand, UUID> {
}
