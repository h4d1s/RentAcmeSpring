package com.pixellabs.inventory.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.inventory.application.useCases.variants.dtos.CreateVariantRequest;
import com.pixellabs.inventory.application.useCases.variants.dtos.UpdateVariantRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.Variant;

@Mapper(componentModel = "spring")
public interface VariantMapper {
	VariantMapper INSTANCE = Mappers.getMapper(VariantMapper.class);
	
	CreateVariantRequest variantToCreateVariantRequest(Variant variant) throws InventoryDomainException;
	Variant createVariantRequestToVariant(CreateVariantRequest request) throws InventoryDomainException;
	
	UpdateVariantRequest variantToUpdateVariantRequest(Variant variant) throws InventoryDomainException;
	Variant updateVariantRequestToVariant(UpdateVariantRequest request) throws InventoryDomainException;
}