package com.pixellabs.inventory.application.useCases.brands.queries.getBrand;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
public class GetBrandHandler {
	private final BrandRepository brandRepository;

	@Cacheable(value = "brands", key = "#query.id()")
	@Transactional
	public Brand handle(GetBrandQuery query) throws NotFoundException {
        return brandRepository.findById(query.id())
                .orElseThrow(() -> new NotFoundException("Brand not found."));
	}
}
