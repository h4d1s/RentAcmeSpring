package com.pixellabs.inventory.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.inventory.application.useCases.brands.dtos.CreateBrandRequest;
import com.pixellabs.inventory.application.useCases.brands.dtos.UpdateBrandRequest;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;

@Mapper(componentModel = "spring")
public interface BrandMapper {
	BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);
	
	CreateBrandRequest brandToCreateBrandRequest(Brand brand) throws InventoryDomainException;
	Brand createBrandRequestToBrand(CreateBrandRequest request) throws InventoryDomainException;
	
	UpdateBrandRequest brandToUpdateBrandRequest(Brand brand) throws InventoryDomainException;
	Brand updateBrandRequestToBrand(UpdateBrandRequest request) throws InventoryDomainException;
}
