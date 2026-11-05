package com.pixellabs.inventory.application.useCases.variants.queries.getVariantList;

import com.pixellabs.inventory.application.services.VariantCacheService;
import com.pixellabs.inventory.application.useCases.variants.dtos.VariantPageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import com.pixellabs.inventory.domain.variantAggregate.Variant;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVariantListHandler {
	private final VariantCacheService variantCacheService;

	public Page<Variant> handle(GetVariantListQuery query) {
		VariantPageCache cached = variantCacheService.getVariants(query);

		return new PageImpl<>(
			cached.content(),
			PageRequest.of(cached.page(), cached.size()),
			cached.totalElements()
		);
	}
}
