package com.pixellabs.inventory.api.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pixellabs.inventory.application.useCases.variants.CreateVariantUseCase;
import com.pixellabs.inventory.application.useCases.variants.DeleteVariantUseCase;
import com.pixellabs.inventory.application.useCases.variants.GetVariantListUseCase;
import com.pixellabs.inventory.application.useCases.variants.GetVariantUseCase;
import com.pixellabs.inventory.application.useCases.variants.UpdateVariantUseCase;
import com.pixellabs.inventory.application.useCases.variants.dtos.CreateVariantRequest;
import com.pixellabs.inventory.application.useCases.variants.dtos.GetVariantListRequest;
import com.pixellabs.inventory.application.useCases.variants.dtos.UpdateVariantRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.exceptions.VariantNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/variants")
public class VariantController {
	private final CreateVariantUseCase createVariantUseCase;
	private final GetVariantUseCase getVariantUseCase;
	private final GetVariantListUseCase getVariantListUseCase;
	private final UpdateVariantUseCase updateVariantUseCase;
	private final DeleteVariantUseCase deleteVariantUseCase;
	
	public VariantController(
			CreateVariantUseCase createVariantUseCase,
			GetVariantUseCase getVariantUseCase,
			GetVariantListUseCase getVariantListUseCase,
			UpdateVariantUseCase updateVariantUseCase,
			DeleteVariantUseCase deleteVariantUseCase) {
		super();
		this.createVariantUseCase = createVariantUseCase;
		this.getVariantUseCase = getVariantUseCase;
		this.getVariantListUseCase = getVariantListUseCase;
		this.updateVariantUseCase = updateVariantUseCase;
		this.deleteVariantUseCase = deleteVariantUseCase;
	}

	@GetMapping
	public Page<Variant> getAllVariants(@Valid GetVariantListRequest request) throws VariantNotFoundException {
		return getVariantListUseCase.execute(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Variant> getVariant(@PathVariable("id") UUID id) throws VariantNotFoundException {
		Variant variant = getVariantUseCase.execute(id);
		return ResponseEntity.ok(variant);
	}
	
	@PostMapping
	public ResponseEntity<Variant> createVariant(@Valid @RequestBody CreateVariantRequest request) throws InventoryDomainException {
		Variant variant = createVariantUseCase.execute(request);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(variant.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(variant);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Variant> updateBrand(@PathVariable UUID id, @Valid @RequestBody UpdateVariantRequest request) throws VariantNotFoundException, InventoryDomainException {
		Variant variant = updateVariantUseCase.execute(id, request);
		return ResponseEntity.ok(variant);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) throws InventoryDomainException {
		deleteVariantUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}
}
