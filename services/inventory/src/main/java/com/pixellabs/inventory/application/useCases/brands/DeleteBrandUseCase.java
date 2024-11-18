package com.pixellabs.inventory.application.useCases.brands;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.brandAggregate.events.BrandDeletedDomainEvent;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.transaction.Transactional;

@Component
public class DeleteBrandUseCase {
	@Autowired
	private final BrandRepository brandRepository;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;

	public DeleteBrandUseCase(BrandRepository brandRepository, ApplicationEventPublisher eventPublisher) {
		super();
		this.brandRepository = brandRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public void execute(UUID id) throws InventoryDomainException {
		brandRepository.deleteById(id);
		eventPublisher.publishEvent(new BrandDeletedDomainEvent(id));
	}
}
