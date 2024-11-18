package com.pixellabs.inventory.application.useCases.models;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.ModelMapper;
import com.pixellabs.inventory.application.useCases.models.dtos.UpdateModelRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import com.pixellabs.inventory.domain.modelAggregate.exceptions.ModelNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class UpdateModelUseCase {

	@Autowired
	private ModelRepository modelRepository;

	public UpdateModelUseCase(ModelRepository modelRepository) {
		super();
		this.modelRepository = modelRepository;
	}
	
	@Transactional
	public Model execute(UUID id, UpdateModelRequest request) throws ModelNotFoundException, InventoryDomainException {
		Model modelToModify = modelRepository.findById(id)
				.orElseThrow(() -> new ModelNotFoundException());
		Model model = ModelMapper.INSTANCE.updateModelRequestToModel(request);
		model.setId(modelToModify.getId());
		return modelRepository.save(model);
	}
}
