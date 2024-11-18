package com.pixellabs.inventory.application.useCases.variants;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.VariantMapper;
import com.pixellabs.inventory.application.useCases.variants.dtos.UpdateVariantRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;
import com.pixellabs.inventory.domain.variantAggregate.exceptions.VariantNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class UpdateVariantUseCase {

	@Autowired
	private VariantRepository variantRepository;

	public UpdateVariantUseCase(VariantRepository variantRepository) {
		super();
		this.variantRepository = variantRepository;
	}
	
	@Transactional
	public Variant execute(UUID id, UpdateVariantRequest request) throws VariantNotFoundException, InventoryDomainException {
		Variant variantToModify = variantRepository.findById(id)
				.orElseThrow(() -> new VariantNotFoundException());
		Variant variant = VariantMapper.INSTANCE.updateVariantRequestToVariant(request);
		variant.setId(variantToModify.getId());
		return variantRepository.save(variant);
	}

}
