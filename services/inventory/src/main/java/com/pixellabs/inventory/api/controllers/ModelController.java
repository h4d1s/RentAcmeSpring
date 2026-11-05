package com.pixellabs.inventory.api.controllers;

import java.net.URI;
import java.util.UUID;

import com.pixellabs.inventory.application.useCases.models.commands.createModel.CreateModelCommand;
import com.pixellabs.inventory.application.useCases.models.commands.deleteModel.DeleteModelCommand;
import com.pixellabs.inventory.application.useCases.models.commands.updateModel.UpdateModelCommand;
import com.pixellabs.inventory.application.useCases.models.queries.getModel.GetModelQuery;
import com.pixellabs.inventory.application.useCases.models.queries.getModelList.GetModelListQuery;
import com.pixellabs.inventory.domain.exceptions.NotFoundException;
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

import com.pixellabs.inventory.application.useCases.models.commands.createModel.CreateModelHandler;
import com.pixellabs.inventory.application.useCases.models.commands.deleteModel.DeleteModelHandler;
import com.pixellabs.inventory.application.useCases.models.queries.getModelList.GetModelListHandler;
import com.pixellabs.inventory.application.useCases.models.queries.getModel.GetModelHandler;
import com.pixellabs.inventory.application.useCases.models.commands.updateModel.UpdateModelHandler;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
public class ModelController {
	private final CreateModelHandler createModelHandler;
	private final GetModelHandler getModelHandler;
	private final GetModelListHandler getListModelsHandler;
	private final UpdateModelHandler updateModelHandler;
	private final DeleteModelHandler deleteModelHandler;

	@GetMapping
	public Page<Model> getAllModels(
			@Valid GetModelListQuery query)
			throws NotFoundException {
		return getListModelsHandler.handle(query);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Model> getModel(
			@PathVariable("id") UUID id)
			throws NotFoundException {
		Model model = getModelHandler.handle(new GetModelQuery(id));
		return ResponseEntity.ok(model);
	}
	
	@PostMapping
	@PreAuthorize("@modelAuthorization.canCreate(authentication)")
	public ResponseEntity<Model> createModel(
			@Valid @RequestBody CreateModelCommand command)
			throws InventoryDomainException, BadRequestException {
		Model model = createModelHandler.handle(command);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(model.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(model);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@modelAuthorization.canUpdate(authentication)")
	public ResponseEntity<Model> updateModel(
			@PathVariable UUID id,
			@Valid @RequestBody UpdateModelCommand command)
			throws NotFoundException, InventoryDomainException, BadRequestException {
		Model model = updateModelHandler.handle(id, command);
		return ResponseEntity.ok(model);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@modelAuthorization.canDelete(authentication)")
	public ResponseEntity<Void> deleteModel(
			@PathVariable UUID id)
			throws NotFoundException {
		deleteModelHandler.handle(new DeleteModelCommand(id));
		return ResponseEntity.noContent().build();
	}
}
