package com.pixellabs.inventory.application.useCases.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.pixellabs.inventory.application.useCases.brands.dtos.GetBrandListRequest;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.brandAggregate.exceptions.BrandNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetBrandListUseCase {
	@Autowired
	private final BrandRepository brandRepository;
	
	public GetBrandListUseCase(BrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;
	}
	
	@Transactional
	public Page<Brand> execute(GetBrandListRequest request) throws BrandNotFoundException {
		Sort sort = Sort.by(request.getOrderBy());
		
		if(request.getOrder() == "asc") {
			sort.ascending();
		} else {
			sort.descending();
		}
		
		Pageable page = PageRequest.of(request.getPage(), request.getPageSize(), sort);
		return brandRepository.findAll(page);
	}
}
