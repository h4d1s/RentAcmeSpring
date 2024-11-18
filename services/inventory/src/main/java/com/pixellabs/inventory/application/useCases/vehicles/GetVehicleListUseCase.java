package com.pixellabs.inventory.application.useCases.vehicles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.useCases.vehicles.dtos.GetVehicleListRequest;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;
import com.pixellabs.inventory.domain.vehicleAggregate.VehicleRepository;
import com.pixellabs.inventory.domain.vehicleAggregate.exceptions.VehicleNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetVehicleListUseCase {

	@Autowired
	private VehicleRepository vehicleRepository;

	public GetVehicleListUseCase(VehicleRepository vehicleRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
	}

	@Transactional
	public Page<Vehicle> execute(GetVehicleListRequest request) throws VehicleNotFoundException {
		Sort sort = Sort.by(request.getOrderBy());
		
		if(request.getOrder() == "asc") {
			sort.ascending();
		} else {
			sort.descending();
		}
		
		Pageable page = PageRequest.of(request.getPage(), request.getPageSize(), sort);
		return vehicleRepository.findAll(page);
	}

}
