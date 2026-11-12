package com.pixellabs.inventory.infrastructure.caching.services;

import com.pixellabs.inventory.application.useCases.brands.dtos.BrandPageCache;
import com.pixellabs.inventory.application.useCases.brands.queries.getBrandList.GetBrandListQuery;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
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
public class BrandCacheService {
    private final BrandRepository brandRepository;

    @Cacheable(
        value = "brands",
        key = "'list:' + #query.page() +" +
            "':' + #query.pageSize() +" +
            "':' + #query.orderBy() +" +
            "':' + #query.order()"
    )
    @Transactional
    public BrandPageCache getBrands(GetBrandListQuery query) {
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
        Page<Brand> result = brandRepository.findAll(pageable);

        return new BrandPageCache(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements()
        );
    }
}
