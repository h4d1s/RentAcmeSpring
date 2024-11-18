package com.pixellabs.inventory.application.useCases.brands;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.BrandMapper;
import com.pixellabs.inventory.application.useCases.brands.dtos.UpdateBrandRequest;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.brandAggregate.exceptions.BrandNotFoundException;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.transaction.Transactional;

@Component
public class UpdateBrandUseCase {
	@Autowired
	private final BrandRepository brandRepository;
	
	public UpdateBrandUseCase(BrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;
	}
	
	@Transactional
	public Brand execute(UUID id, UpdateBrandRequest request) throws BrandNotFoundException, InventoryDomainException {
		Brand brandToModify = brandRepository.findById(id)
				.orElseThrow(() -> new BrandNotFoundException());
		Brand brand = BrandMapper.INSTANCE.updateBrandRequestToBrand(request);
		brand.setId(brandToModify.getId());
		return brandRepository.save(brand);
	}
}
