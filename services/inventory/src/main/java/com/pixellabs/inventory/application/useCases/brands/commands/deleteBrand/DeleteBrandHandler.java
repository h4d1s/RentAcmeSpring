package com.pixellabs.inventory.application.useCases.brands.commands.deleteBrand;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.application.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;

import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.brandAggregate.events.BrandDeletedDomainEvent;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBrandHandler {
	private final BrandRepository brandRepository;
	private final ApplicationEventPublisher eventPublisher;

	@CacheEvict(value = "brands", allEntries = true)
	@Transactional
	public void handle(DeleteBrandCommand command) throws NotFoundException {
		Brand brand = brandRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("Brand not found."));

		brandRepository.delete(brand);
		eventPublisher.publishEvent(new BrandDeletedDomainEvent(brand.getId()));
	}
}
