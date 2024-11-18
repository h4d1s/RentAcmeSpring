package com.pixellabs.inventory.application.useCases.variants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.VariantMapper;
import com.pixellabs.inventory.application.useCases.variants.dtos.CreateVariantRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;

@Component
public class CreateVariantUseCase {

	@Autowired
	private VariantRepository variantRepository;

	public CreateVariantUseCase(VariantRepository variantRepository) {
		super();
		this.variantRepository = variantRepository;
	}
	
	@Transactional
	public Variant execute(CreateVariantRequest request) throws InventoryDomainException {
		Variant newVariant = VariantMapper.INSTANCE.createVariantRequestToVariant(request);
		Variant variant = variantRepository.save(newVariant);
		return variant;
	}

}
