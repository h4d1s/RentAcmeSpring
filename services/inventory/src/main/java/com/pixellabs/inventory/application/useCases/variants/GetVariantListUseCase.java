package com.pixellabs.inventory.application.useCases.variants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.useCases.variants.dtos.GetVariantListRequest;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;
import com.pixellabs.inventory.domain.variantAggregate.exceptions.VariantNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetVariantListUseCase {

	@Autowired
	private VariantRepository variantRepository;

	public GetVariantListUseCase(VariantRepository variantRepository) {
		super();
		this.variantRepository = variantRepository;
	}

	@Transactional
	public Page<Variant> execute(GetVariantListRequest request) throws VariantNotFoundException {
		Sort sort = Sort.by(request.getOrderBy());
		
		if(request.getOrder().equalsIgnoreCase("asc")) {
			sort.ascending();
		} else {
			sort.descending();
		}
		
		Pageable page = PageRequest.of(request.getPage(), request.getPageSize(), sort);
		return variantRepository.findAll(page);
	}

}
