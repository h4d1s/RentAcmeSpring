package com.pixellabs.inventory.api.controllers;

import java.net.URI;
import java.util.UUID;

import com.pixellabs.inventory.application.useCases.brands.commands.createBrand.CreateBrandCommand;
import com.pixellabs.inventory.application.useCases.brands.commands.deleteBrand.DeleteBrandCommand;
import com.pixellabs.inventory.application.useCases.brands.commands.updateBrand.UpdateBrandCommand;
import com.pixellabs.inventory.application.useCases.brands.queries.getBrand.GetBrandQuery;
import com.pixellabs.inventory.application.useCases.brands.queries.getBrandList.GetBrandListQuery;
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

import com.pixellabs.inventory.application.useCases.brands.commands.createBrand.CreateBrandHandler;
import com.pixellabs.inventory.application.useCases.brands.commands.deleteBrand.DeleteBrandHandler;
import com.pixellabs.inventory.application.useCases.brands.queries.getBrand.GetBrandHandler;
import com.pixellabs.inventory.application.useCases.brands.queries.getBrandList.GetBrandListHandler;
import com.pixellabs.inventory.application.useCases.brands.commands.updateBrand.UpdateBrandHandler;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
	private final CreateBrandHandler createBrandHandler;
	private final GetBrandHandler getBrandHandler;
	private final GetBrandListHandler getListBrandsHandler;
	private final UpdateBrandHandler updateBrandHandler;
	private final DeleteBrandHandler deleteBrandHandler;

	@GetMapping
	public Page<Brand> getBrands(
			@Valid GetBrandListQuery query)
			throws NotFoundException {
		return getListBrandsHandler.handle(query);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Brand> getBrand(
			@PathVariable("id") UUID id)
			throws NotFoundException {
		Brand brand = getBrandHandler.handle(new GetBrandQuery(id));
		return ResponseEntity.ok(brand);
	}
	
	@PostMapping
	@PreAuthorize("@brandAuthorization.canCreate(authentication)")
	public ResponseEntity<Brand> create(
			@Valid @RequestBody CreateBrandCommand command)
			throws InventoryDomainException, BadRequestException {
		Brand brand = createBrandHandler.handle(command);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(brand.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(brand);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@brandAuthorization.canUpdate(authentication)")
	public ResponseEntity<Brand> update(
			@PathVariable UUID id,
			@Valid @RequestBody UpdateBrandCommand command)
			throws InventoryDomainException, BadRequestException {
		Brand brand = updateBrandHandler.handle(id, command);
		return ResponseEntity.ok(brand);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@brandAuthorization.canDelete(authentication)")
	public ResponseEntity<Void> delete(
			@PathVariable UUID id) throws NotFoundException {
		deleteBrandHandler.handle(new DeleteBrandCommand(id));
		return ResponseEntity.noContent().build();
	}
}
