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

import com.pixellabs.inventory.application.useCases.models.CreateModelUseCase;
import com.pixellabs.inventory.application.useCases.models.DeleteModelUseCase;
import com.pixellabs.inventory.application.useCases.models.GetModelListUseCase;
import com.pixellabs.inventory.application.useCases.models.GetModelUseCase;
import com.pixellabs.inventory.application.useCases.models.UpdateModelUseCase;
import com.pixellabs.inventory.application.useCases.models.dtos.CreateModelRequest;
import com.pixellabs.inventory.application.useCases.models.dtos.GetModelListRequest;
import com.pixellabs.inventory.application.useCases.models.dtos.UpdateModelRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.exceptions.ModelNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/models")
public class ModelController {
	private final CreateModelUseCase createModelUseCase;
	private final GetModelUseCase getModelUseCase;
	private final GetModelListUseCase getListModelsUseCase;
	private final UpdateModelUseCase updateModelUseCase;
	private final DeleteModelUseCase deleteModelUseCase;
	
	public ModelController(
			CreateModelUseCase createModelUseCase,
			GetModelUseCase getModelUseCase,
			GetModelListUseCase getListModelsUseCase,
			UpdateModelUseCase updateModelUseCase,
			DeleteModelUseCase deleteModelUseCase) {
		super();
		this.createModelUseCase = createModelUseCase;
		this.getModelUseCase = getModelUseCase;
		this.getListModelsUseCase = getListModelsUseCase;
		this.updateModelUseCase = updateModelUseCase;
		this.deleteModelUseCase	= deleteModelUseCase;
	}

	@GetMapping
	public Page<Model> getAllModels(@Valid GetModelListRequest request) throws ModelNotFoundException {
		return getListModelsUseCase.execute(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Model> getBrand(@PathVariable("id") UUID id) throws ModelNotFoundException {
		Model model = getModelUseCase.execute(id);
		return ResponseEntity.ok(model);
	}
	
	@PostMapping
	public ResponseEntity<Model> createBrand(@Valid @RequestBody CreateModelRequest request) throws InventoryDomainException {
		Model model = createModelUseCase.execute(request);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(model.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(model);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Model> updateBrand(@PathVariable UUID id, @Valid @RequestBody UpdateModelRequest request) throws ModelNotFoundException, InventoryDomainException {
		Model model = updateModelUseCase.execute(id, request);
		return ResponseEntity.ok(model);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) throws InventoryDomainException {
		deleteModelUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}
}
