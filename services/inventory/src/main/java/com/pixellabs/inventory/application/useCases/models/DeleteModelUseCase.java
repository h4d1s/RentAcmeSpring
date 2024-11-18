package com.pixellabs.inventory.application.useCases.models;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;

import jakarta.transaction.Transactional;

@Component
public class DeleteModelUseCase {

	@Autowired
	private ModelRepository modelRepository;

	public DeleteModelUseCase(ModelRepository modelRepository) {
		super();
		this.modelRepository = modelRepository;
	}
	
	@Transactional
	public void execute(UUID id) throws InventoryDomainException {
		modelRepository.deleteById(id);
	}
}