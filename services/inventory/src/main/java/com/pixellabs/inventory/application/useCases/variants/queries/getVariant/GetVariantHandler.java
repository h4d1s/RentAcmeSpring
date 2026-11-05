package com.pixellabs.inventory.application.useCases.variants.queries.getVariant;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVariantHandler {
	private final VariantRepository variantRepository;

	@Cacheable(value = "variants", key = "#query.id()")
	@Transactional
	public Variant handle(GetVariantQuery query) throws NotFoundException {
		return variantRepository.findById(query.id())
				.orElseThrow(() -> new NotFoundException("Variant not found."));
	}
}
