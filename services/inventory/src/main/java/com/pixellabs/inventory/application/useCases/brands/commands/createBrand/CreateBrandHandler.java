package com.pixellabs.inventory.application.useCases.brands.commands.createBrand;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateBrandHandler {
	private final BrandRepository brandRepository;
	private final Validator validator;

	@CacheEvict(value = "brands", allEntries = true)
	@Transactional
	public Brand handle(CreateBrandCommand command)
			throws InventoryDomainException, BadRequestException {
		Set<ConstraintViolation<CreateBrandCommand>> violations =
				validator.validate(command);

		if (!violations.isEmpty()) {
			throw new BadRequestException("Invalid brand data.");
		}

		Brand brand = new Brand(command.name());

		log.info("Creating brand - Brand: {}", brand);

		return brandRepository.save(brand);
	}
}
