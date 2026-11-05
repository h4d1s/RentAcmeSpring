package com.pixellabs.inventory.application.mappers;

import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleResponse;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleSearchResponse;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
	@Mapping(target = "brand", source = "variant.model.brand.name")
	@Mapping(target = "model", source = "variant.model.name")
	@Mapping(target = "variant", source = "variant.name")
    VehicleResponse toResponse(Vehicle vehicle);

	@Mapping(target = "brand", source = "variant.model.brand.name")
	@Mapping(target = "model", source = "variant.model.name")
	@Mapping(target = "rentalPricePerDay", source = "rentalPricePerDay")
	@Mapping(target = "gearbox", source = "variant.gearbox")
	@Mapping(target = "category", source = "variant.model.category")
	@Mapping(target = "numberOfSeats", source = "variant.model.numberOfSeats")
	@Mapping(target = "power", source = "variant.power")
    VehicleSearchResponse toSearchResponse(Vehicle vehicle);
}
