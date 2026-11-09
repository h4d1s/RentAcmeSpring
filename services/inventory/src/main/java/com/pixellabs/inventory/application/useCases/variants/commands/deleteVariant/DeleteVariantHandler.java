package com.pixellabs.inventory.application.useCases.variants.commands.deleteVariant;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteVariantHandler {
	private final VariantRepository variantRepository;

	@CacheEvict(value = "variants", allEntries = true)
	@Transactional
	public void handle(DeleteVariantCommand command)
			throws InventoryDomainException, NotFoundException {
		Variant variant = variantRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Variant not found."));

		variantRepository.delete(variant);
	}
}
