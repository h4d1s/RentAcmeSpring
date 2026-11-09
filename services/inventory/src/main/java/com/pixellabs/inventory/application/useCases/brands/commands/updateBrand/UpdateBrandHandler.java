package com.pixellabs.inventory.application.useCases.brands.commands.updateBrand;

import java.util.Set;
import java.util.UUID;

import com.pixellabs.inventory.application.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBrandHandler {
	private final BrandRepository brandRepository;
	private final Validator validator;

	@CacheEvict(value = "brands", allEntries = true)
	@Transactional
	public Brand handle(UUID id, UpdateBrandCommand command)
			throws InventoryDomainException, BadRequestException, NotFoundException {
		Set<ConstraintViolation<UpdateBrandCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid brand data.");
		}

		Brand brand = brandRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Brand not found."));
		brand.updateName(command.name());

		return brandRepository.save(brand);
	}
}
