package com.pixellabs.inventory.application.useCases.models;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import com.pixellabs.inventory.domain.modelAggregate.exceptions.ModelNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetModelUseCase {

	@Autowired
	private ModelRepository modelRepository;

	public GetModelUseCase(ModelRepository modelRepository) {
		super();
		this.modelRepository = modelRepository;
	}

	@Transactional
	public Model execute(UUID id) throws ModelNotFoundException {
		Model model = modelRepository.findById(id)
				.orElseThrow(() -> new ModelNotFoundException());
		return model;
	}
}
