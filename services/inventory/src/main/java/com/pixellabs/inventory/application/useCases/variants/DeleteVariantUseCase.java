package com.pixellabs.inventory.application.useCases.variants;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;

@Component
public class DeleteVariantUseCase {

	@Autowired
	private VariantRepository variantRepository;

	public DeleteVariantUseCase(VariantRepository variantRepository) {
		super();
		this.variantRepository = variantRepository;
	}
	
	@Transactional
	public void execute(UUID id) throws InventoryDomainException {
		variantRepository.deleteById(id);
	}

}
