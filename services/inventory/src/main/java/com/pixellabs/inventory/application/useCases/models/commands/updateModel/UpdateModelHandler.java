package com.pixellabs.inventory.application.useCases.models.commands.updateModel;

import java.util.Set;
import java.util.UUID;

import com.pixellabs.inventory.domain.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateModelHandler {
	private final ModelRepository modelRepository;
	private final Validator validator;

	@CacheEvict(value = "models", allEntries = true)
	@Transactional
	public Model handle(UUID id, UpdateModelCommand command)
			throws NotFoundException, InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<UpdateModelCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid model data.");
		}

		Model model = modelRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Model not found."));
		model.updateName(command.name());
		model.updateYearOfProduction(command.yearOfProduction());
		model.updateNumberOfSeats(command.numberOfSeats());
		model.updateCategory(command.category());

		return modelRepository.save(model);
	}
}
