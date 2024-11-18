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

import com.pixellabs.inventory.application.useCases.brands.CreateBrandUseCase;
import com.pixellabs.inventory.application.useCases.brands.DeleteBrandUseCase;
import com.pixellabs.inventory.application.useCases.brands.GetBrandUseCase;
import com.pixellabs.inventory.application.useCases.brands.GetBrandListUseCase;
import com.pixellabs.inventory.application.useCases.brands.UpdateBrandUseCase;
import com.pixellabs.inventory.application.useCases.brands.dtos.CreateBrandRequest;
import com.pixellabs.inventory.application.useCases.brands.dtos.GetBrandListRequest;
import com.pixellabs.inventory.application.useCases.brands.dtos.UpdateBrandRequest;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.exceptions.BrandNotFoundException;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
	private final CreateBrandUseCase createBrandUseCase;
	private final GetBrandUseCase getBrandUseCase;
	private final GetBrandListUseCase getListBrandsUseCase;
	private final UpdateBrandUseCase updateBrandUseCase;
	private final DeleteBrandUseCase deleteBrandUseCase;
	
	public BrandController(
			CreateBrandUseCase createBrandUseCase,
			GetBrandUseCase getBrandUseCase,
			GetBrandListUseCase getListBrandsUseCase,
			UpdateBrandUseCase updateBrandUseCase,
			DeleteBrandUseCase deleteBrandUseCase) {
		super();
		this.createBrandUseCase = createBrandUseCase;
		this.getBrandUseCase = getBrandUseCase;
		this.getListBrandsUseCase = getListBrandsUseCase;
		this.updateBrandUseCase = updateBrandUseCase;
		this.deleteBrandUseCase	= deleteBrandUseCase;
	}

	@GetMapping
	public Page<Brand> getAllBrands(@Valid GetBrandListRequest request) throws BrandNotFoundException {
		return getListBrandsUseCase.execute(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Brand> getBrand(@PathVariable("id") UUID id) throws BrandNotFoundException {
		Brand brand = getBrandUseCase.execute(id);
		return ResponseEntity.ok(brand);
	}
	
	@PostMapping
	public ResponseEntity<Brand> createBrand(@Valid @RequestBody CreateBrandRequest request) throws InventoryDomainException {
		Brand brand = createBrandUseCase.execute(request);
		String location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(brand.getId())
                .toString();
		return ResponseEntity.created(URI.create(location)).body(brand);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Brand> updateBrand(@PathVariable UUID id, @Valid @RequestBody UpdateBrandRequest request) throws BrandNotFoundException, InventoryDomainException {
		Brand brand = updateBrandUseCase.execute(id, request);
		return ResponseEntity.ok(brand);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable UUID id) throws InventoryDomainException {
		deleteBrandUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}
}
