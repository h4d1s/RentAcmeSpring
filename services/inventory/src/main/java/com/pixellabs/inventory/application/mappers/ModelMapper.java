package com.pixellabs.inventory.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.pixellabs.inventory.application.useCases.models.dtos.CreateModelRequest;
import com.pixellabs.inventory.application.useCases.models.dtos.UpdateModelRequest;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;

@Mapper(componentModel = "spring")
public interface ModelMapper {
	ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);
	
	CreateModelRequest modelToCreateModelRequest(Model model) throws InventoryDomainException;
	Model createModelRequestToModel(CreateModelRequest request) throws InventoryDomainException;
	
	UpdateModelRequest modelToUpdateModelRequest(Model brand) throws InventoryDomainException;
	Model updateModelRequestToModel(UpdateModelRequest request) throws InventoryDomainException;
}