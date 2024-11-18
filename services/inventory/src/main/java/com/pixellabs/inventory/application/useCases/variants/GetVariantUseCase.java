package com.pixellabs.inventory.application.useCases.variants;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;
import com.pixellabs.inventory.domain.variantAggregate.exceptions.VariantNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetVariantUseCase {
	
	@Autowired
	private VariantRepository variantRepository;

	public GetVariantUseCase(VariantRepository variantRepository) {
		super();
		this.variantRepository = variantRepository;
	}

	@Transactional
	public Variant execute(UUID id) throws VariantNotFoundException {
		Variant variant = variantRepository.findById(id)
				.orElseThrow(() -> new VariantNotFoundException());
		return variant;
	}
}
