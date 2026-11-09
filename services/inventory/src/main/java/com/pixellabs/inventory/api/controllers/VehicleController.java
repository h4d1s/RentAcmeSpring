package com.pixellabs.inventory.api.controllers;

import java.net.URI;
import java.util.UUID;

import com.pixellabs.inventory.application.useCases.vehicles.commands.createVehicle.CreateVehicleCommand;
import com.pixellabs.inventory.application.useCases.vehicles.commands.deleteVehicle.DeleteVehicleCommand;
import com.pixellabs.inventory.application.useCases.vehicles.commands.updateVehicle.UpdateVehicleCommand;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleResponse;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleSearchResponse;
import com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicle.GetVehicleQuery;
import com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicleList.GetVehicleListQuery;
import com.pixellabs.inventory.application.useCases.vehicles.queries.searchVehicles.SearchVehiclesHandler;
import com.pixellabs.inventory.application.useCases.vehicles.queries.searchVehicles.SearchVehiclesQuery;
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

import com.pixellabs.inventory.application.useCases.vehicles.commands.createVehicle.CreateVehicleHandler;
import com.pixellabs.inventory.application.useCases.vehicles.commands.deleteVehicle.DeleteVehicleHandler;
import com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicleList.GetVehicleListHandler;
import com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicle.GetVehicleHandler;
import com.pixellabs.inventory.application.useCases.vehicles.commands.updateVehicle.UpdateVehicleHandler;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {
	private final CreateVehicleHandler createVehicleHandler;
	private final GetVehicleHandler getVehicleHandler;
	private final SearchVehiclesHandler searchVehiclesHandler;
	private final GetVehicleListHandler getVehicleListHandler;
	private final UpdateVehicleHandler updateVehicleHandler;
	private final DeleteVehicleHandler deleteVehicleHandler;

	@GetMapping
	public Page<VehicleResponse> getAllVehicles(@Valid GetVehicleListQuery query) {
		return getVehicleListHandler.handle(query);
	}

	@GetMapping("/search")
	public Page<VehicleSearchResponse> searchVehicles(@Valid SearchVehiclesQuery query) {
		return searchVehiclesHandler.handle(query);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Vehicle> getVehicle(@PathVariable("id") UUID id)
			throws NotFoundException {
		Vehicle vehicle = getVehicleHandler.handle(new GetVehicleQuery(id));
		return ResponseEntity.ok(vehicle);
	}
	
	@PostMapping
	@PreAuthorize("@vehicleAuthorization.canCreate(authentication)")
	public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody CreateVehicleCommand command)
            throws InventoryDomainException, BadRequestException {
		Vehicle vehicle = createVehicleHandler.handle(command);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicle.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(vehicle);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@vehicleAuthorization.canUpdate(authentication)")
	public ResponseEntity<Vehicle> updateVehicle(
			@PathVariable UUID id, @Valid @RequestBody UpdateVehicleCommand command)
            throws NotFoundException, InventoryDomainException, BadRequestException {
		Vehicle vehicle = updateVehicleHandler.handle(id, command);
		return ResponseEntity.ok(vehicle);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@vehicleAuthorization.canDelete(authentication)")
	public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id)
            throws InventoryDomainException, NotFoundException {
		deleteVehicleHandler.handle(new DeleteVehicleCommand(id));
		return ResponseEntity.noContent().build();
	}
}
