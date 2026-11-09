package com.pixellabs.inventory.application.useCases.models.queries.getModel;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetModelHandler {
	private final ModelRepository modelRepository;

	@Cacheable(value = "models", key = "#query.id()")
	@Transactional
	public Model handle(GetModelQuery query)
			throws NotFoundException {
		return modelRepository.findById(query.id())
				.orElseThrow(() -> new NotFoundException("Model not found."));
	}
}
