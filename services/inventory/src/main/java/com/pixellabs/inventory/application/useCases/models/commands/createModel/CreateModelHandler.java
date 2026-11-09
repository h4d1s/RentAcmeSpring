package com.pixellabs.inventory.application.useCases.models.commands.createModel;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.application.exceptions.NotFoundException;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateModelHandler {
	private final BrandRepository brandRepository;
	private final ModelRepository modelRepository;
	private final Validator validator;

	@CacheEvict(value = "models", allEntries = true)
	@Transactional
	public Model handle(CreateModelCommand command)
			throws InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<CreateModelCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid model data.");
		}

		Brand brand = brandRepository.findById(command.brandId())
				.orElseThrow(() -> new NotFoundException("Brand not found."));

		Model newModel = new Model(
			command.name(),
			command.yearOfProduction(),
			command.numberOfSeats(),
			command.category(),
			brand
		);
		return modelRepository.save(newModel);
	}
}
