package com.pixellabs.inventory.application.useCases.models.queries.getModelList;

import com.pixellabs.inventory.infrastructure.caching.services.ModelCacheService;
import com.pixellabs.inventory.application.useCases.models.dtos.ModelPageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import com.pixellabs.inventory.domain.modelAggregate.Model;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetModelListHandler {
	private final ModelCacheService modelCacheService;

	public Page<Model> handle(GetModelListQuery query) {
		ModelPageCache cached = modelCacheService.getModels(query);

		return new PageImpl<>(
			cached.content(),
			PageRequest.of(cached.page(), cached.size()),
			cached.totalElements()
		);
	}
}
