package com.pixellabs.inventory.application.useCases.vehicles.queries.getVehicleList;

import com.pixellabs.inventory.infrastructure.caching.services.VehicleCacheService;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehiclePageCache;
import com.pixellabs.inventory.application.useCases.vehicles.dtos.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetVehicleListHandler {
	private final VehicleCacheService vehicleCacheService;

	public Page<VehicleResponse> handle(GetVehicleListQuery query) {
		VehiclePageCache vehiclePageCache = vehicleCacheService.getVehicles(query);

		return new PageImpl<>(
			vehiclePageCache.content(),
			PageRequest.of(vehiclePageCache.page(), vehiclePageCache.size()),
			vehiclePageCache.totalElements()
		);
	}
}
