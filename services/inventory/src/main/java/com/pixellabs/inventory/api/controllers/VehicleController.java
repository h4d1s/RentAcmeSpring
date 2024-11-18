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

import com.pixellabs.inventory.application.useCases.vehicles.CreateVehicleUseCase;
import com.pixellabs.inventory.application.useCases.vehicles.DeleteVehicleUseCase;
import com.pixellabs.inventory.application.useCases.vehicles.GetVehicleListUseCase;
import com.pixellabs.inventory.application.useCases.vehicles.GetVehicleUseCase;
import com.pixellabs.inventory.application.useCases.vehicles.UpdateVehicleUseCase;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.CreateVehicleRequest;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.GetVehicleListRequest;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.UpdateVehicleRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.exceptions.VehicleNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	private final CreateVehicleUseCase createVehicleUseCase;
	private final GetVehicleUseCase getVehicleUseCase;
	private final GetVehicleListUseCase getVehicleListUseCase;
	private final UpdateVehicleUseCase updateVehicleUseCase;
	private final DeleteVehicleUseCase deleteVehicleUseCase;
	
	public VehicleController(
			CreateVehicleUseCase createVehicleUseCase,
			GetVehicleUseCase getVehicleUseCase,
			GetVehicleListUseCase getVehicleListUseCase,
			UpdateVehicleUseCase updateVehicleUseCase,
			DeleteVehicleUseCase deleteVehicleUseCase) {
		super();
		this.createVehicleUseCase = createVehicleUseCase;
		this.getVehicleUseCase = getVehicleUseCase;
		this.getVehicleListUseCase = getVehicleListUseCase;
		this.updateVehicleUseCase = updateVehicleUseCase;
		this.deleteVehicleUseCase = deleteVehicleUseCase;
	}

	@GetMapping
	public Page<Vehicle> getAllVehicles(@Valid GetVehicleListRequest request)
			throws VehicleNotFoundException {
		return getVehicleListUseCase.execute(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Vehicle> getVehicle(@PathVariable("id") UUID id)
			throws VehicleNotFoundException {
		Vehicle vehicle = getVehicleUseCase.execute(id);
		return ResponseEntity.ok(vehicle);
	}
	
	@PostMapping
	public ResponseEntity<Vehicle> createVariant(@Valid @RequestBody CreateVehicleRequest request)
			throws InventoryDomainException {
		Vehicle vehicle = createVehicleUseCase.execute(request);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(vehicle.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(vehicle);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Vehicle> updateBrand(@PathVariable UUID id, @Valid @RequestBody UpdateVehicleRequest request)
			throws VehicleNotFoundException, InventoryDomainException {
		Vehicle vehicle = updateVehicleUseCase.execute(id, request);
		return ResponseEntity.ok(vehicle);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id)
			throws InventoryDomainException {
		deleteVehicleUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}
}
