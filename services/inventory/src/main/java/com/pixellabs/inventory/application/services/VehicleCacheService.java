package com.pixellabs.inventory.application.services;

import com.pixellabs.inventory.application.mappers.VehicleMapper;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.SearchVehiclePageCache;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehiclePageCache;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleResponse;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleSearchResponse;
import com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicleList.GetVehicleListQuery;
import com.pixellabs.inventory.application.useCases.vehicles.queries.searchVehicles.SearchVehiclesQuery;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleCacheService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Cacheable(
        value = "vehicles",
        key = "'list:' + #query.gearbox()"
            + " + ':' + #query.yearOfProduction()"
            + " + ':' + #query.power()"
            + " + ':' + #query.engineSize()"
            + " + ':' + #query.pickupDate()"
            + " + ':' + #query.returnDate()"
            + " + ':' + #query.rentalPricePerDayFrom()"
            + " + ':' + #query.rentalPricePerDayTo()"
            + " + ':' + #query.modelId()"
            + " + ':' + #query.orderBy()"
            + " + ':' + #query.order()"
            + " + ':' + #query.page()"
            + " + ':' + #query.pageSize()"
    )
    @Transactional
    public VehiclePageCache getVehicles(
        GetVehicleListQuery query
    ) {
        int page = Math.max(query.page(), 1);
        int pageSize = Math.max(query.pageSize(), 1);

        String orderBy = query.orderBy() == null
                ? "id"
                : query.orderBy().toLowerCase();

        String sortBy = switch (orderBy) {
            case "brand" -> "variant.model.brand.name";
            case "model" -> "variant.model.name";
            case "variant" -> "variant.name";
            case "price" -> "rentalPricePerDay";
            case "power" -> "variant.power";
            default -> "id";
        };

        Sort.Direction direction =
                "desc".equalsIgnoreCase(query.order())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
            page - 1,
            pageSize,
            Sort.by(direction, sortBy)
        );

        Page<Vehicle> vehicles =
            vehicleRepository.findAvailableVehicles(
                VehicleStatus.AVAILABLE,
                query.rentalPricePerDayFrom(),
                query.rentalPricePerDayTo(),
                pageable
            );

        Page<VehicleResponse> responses =
                vehicles.map(vehicleMapper::toResponse);

        return new VehiclePageCache(
            responses.getContent(),
            responses.getNumber(),
            responses.getSize(),
            responses.getTotalElements(),
            responses.getTotalPages()
        );
    }

    @Cacheable(
        value = "vehicles",
        key = "'search:' + #query.gearbox()"
            + " + ':' + #query.pickupDate()"
            + " + ':' + #query.returnDate()"
            + " + ':' + #query.rentalPricePerDayFrom()"
            + " + ':' + #query.rentalPricePerDayTo()"
            + " + ':' + #query.category()"
            + " + ':' + #query.orderBy()"
            + " + ':' + #query.order()"
            + " + ':' + #query.page()"
            + " + ':' + #query.pageSize()"
    )
    @Transactional
    public SearchVehiclePageCache searchVehicles(
        SearchVehiclesQuery query
    ) {
        int page = Math.max(query.page(), 1);
        int pageSize = Math.max(query.pageSize(), 1);

        String sortBy = switch (query.orderBy().toLowerCase()) {
            case "brand" -> "variant.model.brand.name";
            case "model" -> "variant.model.name";
            case "variant" -> "variant.name";
            case "price" -> "rentalPricePerDay";
            case "power" -> "variant.power";
            default -> "id";
        };

        Sort.Direction direction = "desc".equalsIgnoreCase(query.order())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
            page - 1,
            pageSize,
            Sort.by(direction, sortBy)
        );

        Page<Vehicle> vehicles = vehicleRepository.searchVehicles(
            VehicleStatus.AVAILABLE,
            query.gearbox(),
            query.category(),
            query.rentalPricePerDayFrom(),
            query.rentalPricePerDayTo(),
            pageable
        );

        Page<VehicleSearchResponse> responses = vehicles.map(vehicleMapper::toSearchResponse);

        return new SearchVehiclePageCache(
            responses.getContent(),
            responses.getNumber(),
            responses.getSize(),
            responses.getTotalElements(),
            responses.getTotalPages()
        );
    }
}
