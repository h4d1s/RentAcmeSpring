package com.pixellabs.inventory.infrastructure.caching.services;

import com.pixellabs.inventory.application.useCases.models.dtos.ModelPageCache;
import com.pixellabs.inventory.application.useCases.models.queries.getModelList.GetModelListQuery;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.modelAggregate.ModelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelCacheService {
    private final ModelRepository modelRepository;

    @Cacheable(
        value = "models",
        key = "'list:' + #query.page() + ':' + #query.pageSize() + ':' + #query.orderBy() + ':' + #query.order()"
    )
    @Transactional
    public ModelPageCache getModels(GetModelListQuery query) {
        int page = Math.max(query.page(), 1);
        int pageSize = Math.max(query.pageSize(), 1);

        String sortBy = "name".equalsIgnoreCase(query.orderBy())
            ? "name"
            : "id";

        Sort.Direction direction = "desc".equalsIgnoreCase(query.order())
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
            page - 1,
            pageSize,
            Sort.by(direction, sortBy)
        );
        Page<Model> result = modelRepository.findAll(pageable);

        return new ModelPageCache(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements()
        );
    }
}
