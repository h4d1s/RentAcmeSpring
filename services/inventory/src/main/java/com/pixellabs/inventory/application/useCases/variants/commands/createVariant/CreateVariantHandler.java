package com.pixellabs.inventory.application.useCases.variants.commands.createVariant;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateVariantHandler {
	private final VariantRepository variantRepository;
	private final ModelRepository modelRepository;
	private final Validator validator;

	@CacheEvict(value = "variants", allEntries = true)
	@Transactional
	public Variant handler(CreateVariantCommand command)
			throws BadRequestException, NotFoundException, InventoryDomainException {
		Set<ConstraintViolation<CreateVariantCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid variant data.");
		}

		Model model = modelRepository.findById(command.modelId())
				.orElseThrow(() -> new NotFoundException("Model not found."));

		Variant variant = new Variant(
			command.name(),
			command.gearbox(),
			command.fuelType(),
			command.power(),
			command.engineSize(),
			model
		);
		return variantRepository.save(variant);
	}
}
