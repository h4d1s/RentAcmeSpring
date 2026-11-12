package com.pixellabs.inventory.application.useCases.brands.queries.getBrandList;

import com.pixellabs.inventory.infrastructure.caching.services.BrandCacheService;
import com.pixellabs.inventory.application.useCases.brands.dtos.BrandPageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import com.pixellabs.inventory.domain.brandAggregate.Brand;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBrandListHandler {
	private final BrandCacheService brandCacheService;

	public Page<Brand> handle(GetBrandListQuery query) {
		BrandPageCache cached = brandCacheService.getBrands(query);

		return new PageImpl<>(
			cached.content(),
			PageRequest.of(cached.page(), cached.size()),
			cached.totalElements()
		);
	}
}
