package com.pixellabs.inventory.application.useCases.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.mappers.ModelMapper;
import com.pixellabs.inventory.application.useCases.models.dtos.CreateModelRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;

import jakarta.transaction.Transactional;

@Component
public class CreateModelUseCase {
	@Autowired
	private ModelRepository modelRepository;

	public CreateModelUseCase(ModelRepository modelRepository) {
		super();
		this.modelRepository = modelRepository;
	}
	
	@Transactional
	public Model execute(CreateModelRequest request) throws InventoryDomainException {
		Model newModel = ModelMapper.INSTANCE.createModelRequestToModel(request);
		Model model = modelRepository.save(newModel);
		return model;
	}
}
