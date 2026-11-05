package com.pixellabs.inventory.application.useCases.vehicles.queries.searchVehicles;

import com.pixellabs.inventory.application.services.VehicleCacheService;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.SearchVehiclePageCache;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchVehiclesHandler {
    private final VehicleCacheService vehicleCacheService;

    public Page<VehicleSearchResponse> handle(SearchVehiclesQuery query) {
        SearchVehiclePageCache vehiclePageCache = vehicleCacheService.searchVehicles(query);

        return new PageImpl<>(
            vehiclePageCache.content(),
            PageRequest.of(vehiclePageCache.page(), vehiclePageCache.size()),
            vehiclePageCache.totalElements()
        );
    }
}
