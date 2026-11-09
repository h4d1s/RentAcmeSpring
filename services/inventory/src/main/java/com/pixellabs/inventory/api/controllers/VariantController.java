package com.pixellabs.inventory.api.controllers;

import java.net.URI;
import java.util.UUID;

import com.pixellabs.inventory.application.useCases.variants.commands.createVariant.CreateVariantCommand;
import com.pixellabs.inventory.application.useCases.variants.commands.deleteVariant.DeleteVariantCommand;
import com.pixellabs.inventory.application.useCases.variants.commands.updateVariant.UpdateVariantCommand;
import com.pixellabs.inventory.application.useCases.variants.queries.getVariant.GetVariantQuery;
import com.pixellabs.inventory.application.useCases.variants.queries.getVariantList.GetVariantListQuery;
import com.pixellabs.inventory.application.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pixellabs.inventory.application.useCases.variants.commands.createVariant.CreateVariantHandler;
import com.pixellabs.inventory.application.useCases.variants.commands.deleteVariant.DeleteVariantHandler;
import com.pixellabs.inventory.application.useCases.variants.queries.getVariantList.GetVariantListHandler;
import com.pixellabs.inventory.application.useCases.variants.queries.getVariant.GetVariantHandler;
import com.pixellabs.inventory.application.useCases.variants.commands.updateVariant.UpdateVariantHandler;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {
	private final CreateVariantHandler createVariantHandler;
	private final GetVariantHandler getVariantHandler;
	private final GetVariantListHandler getVariantListHandler;
	private final UpdateVariantHandler updateVariantHandler;
	private final DeleteVariantHandler deleteVariantHandler;

	@GetMapping
	public Page<Variant> getAllVariants(
			@Valid GetVariantListQuery query) {
		return getVariantListHandler.handle(query);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Variant> getVariant(
			@PathVariable("id") UUID id)
			throws NotFoundException {
		Variant variant = getVariantHandler.handle(new GetVariantQuery(id));
		return ResponseEntity.ok(variant);
	}
	
	@PostMapping
	@PreAuthorize("@variantAuthorization.canCreate(authentication)")
	public ResponseEntity<Variant> createVariant(
			@Valid @RequestBody CreateVariantCommand command)
            throws NotFoundException, BadRequestException, InventoryDomainException {
		Variant variant = createVariantHandler.handler(command);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(variant.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(variant);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@variantAuthorization.canUpdate(authentication)")
	public ResponseEntity<Variant> updateVariant(
			@PathVariable UUID id,
			@Valid @RequestBody UpdateVariantCommand command)
			throws NotFoundException, InventoryDomainException, BadRequestException {
		Variant variant = updateVariantHandler.handle(id, command);
		return ResponseEntity.ok(variant);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@variantAuthorization.canDelete(authentication)")
	public ResponseEntity<Void> deleteVariant(@PathVariable UUID id)
			throws InventoryDomainException, NotFoundException {
		deleteVariantHandler.handle(new DeleteVariantCommand(id));
		return ResponseEntity.noContent().build();
	}
}
