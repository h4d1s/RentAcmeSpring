package com.pixellabs.inventory.application.useCases.variants.commands.updateVariant;

import java.util.Set;
import java.util.UUID;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateVariantHandler {
	private final VariantRepository variantRepository;
	private final ModelRepository modelRepository;
	private final Validator validator;

	@CacheEvict(value = "variants", allEntries = true)
	@Transactional
	public Variant handle(UUID id, UpdateVariantCommand command)
            throws NotFoundException, InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<UpdateVariantCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid variant data.");
		}

		Variant variant = variantRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Variant not found."));

		variant.updateName(command.name());
		variant.updateGearbox(command.gearbox());
		variant.updateFuelType(command.fuelType());
		variant.updatePower(command.power());
		variant.updateEngineSize(command.engineSize());

		Model model = modelRepository.findById(command.modelId())
				.orElseThrow(() -> new NotFoundException("Model not found."));
		variant.updateModel(model);

		return variantRepository.save(variant);
	}
}
