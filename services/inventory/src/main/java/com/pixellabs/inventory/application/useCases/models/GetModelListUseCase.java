package com.pixellabs.inventory.application.useCases.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.pixellabs.inventory.application.useCases.models.dtos.GetModelListRequest;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import com.pixellabs.inventory.domain.modelAggregate.exceptions.ModelNotFoundException;

import jakarta.transaction.Transactional;

@Component
public class GetModelListUseCase {

	@Autowired
	private ModelRepository modelRepository;

	public GetModelListUseCase(ModelRepository modelRepository) {
		super();
		this.modelRepository = modelRepository;
	}

	@Transactional
	public Page<Model> execute(GetModelListRequest request) throws ModelNotFoundException {
		Sort sort = Sort.by(request.getOrderBy());
		
		if(request.getOrder() == "asc") {
			sort.ascending();
		} else {
			sort.descending();
		}
		
		Pageable page = PageRequest.of(request.getPage(), request.getPageSize(), sort);
		return modelRepository.findAll(page);
	}
}
