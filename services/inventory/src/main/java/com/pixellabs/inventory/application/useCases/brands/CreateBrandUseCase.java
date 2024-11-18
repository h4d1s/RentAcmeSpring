package com.pixellabs.inventory.application.useCases.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.BrandMapper;
import com.pixellabs.inventory.application.useCases.brands.dtos.CreateBrandRequest;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.transaction.Transactional;

@Component
public class CreateBrandUseCase {
	@Autowired
	private final BrandRepository brandRepository;
	
	public CreateBrandUseCase(BrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;
	}
	
	@Transactional
	public Brand execute(CreateBrandRequest request) throws InventoryDomainException {
		Brand newBrand = BrandMapper.INSTANCE.createBrandRequestToBrand(request);
		Brand brand = brandRepository.save(newBrand);
		return brand;
	}
}
