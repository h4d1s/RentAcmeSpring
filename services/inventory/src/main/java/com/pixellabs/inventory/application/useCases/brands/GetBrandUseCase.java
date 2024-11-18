package com.pixellabs.inventory.application.useCases.brands;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.brandAggregate.exceptions.BrandNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetBrandUseCase {
	@Autowired
	private final BrandRepository brandRepository;
	
	public GetBrandUseCase(BrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;
	}
	
	@Transactional
	public Brand execute(UUID id) throws BrandNotFoundException {
		Brand brand = brandRepository.findById(id)
				.orElseThrow(() -> new BrandNotFoundException());
		return brand;
	}
}
