package com.pixellabs.inventory.application.useCases.models.commands.deleteModel;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteModelHandler {
	private final ModelRepository modelRepository;

	@CacheEvict(value = "models", allEntries = true)
	@Transactional
	public void handle(DeleteModelCommand command)
			throws NotFoundException {
		Model model = modelRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Model not found."));

		modelRepository.delete(model);
	}
}