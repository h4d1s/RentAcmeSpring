package com.pixellabs.inventory.infrastructure.caching.services;

import com.pixellabs.inventory.application.useCases.variants.dtos.VariantPageCache;
import com.pixellabs.inventory.application.useCases.variants.queries.getVariantList.GetVariantListQuery;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.variantAggregate.VariantRepository;
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
public class VariantCacheService {
    private final VariantRepository variantRepository;

    @Cacheable(
        value = "variants",
        key = "'list:' + #query.page() + ':' + #query.pageSize() + ':' + #query.orderBy() + ':' + #query.order()"
    )
    @Transactional
    public VariantPageCache getVariants(GetVariantListQuery query) {
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
        Page<Variant> result = variantRepository.findAll(pageable);

        return new VariantPageCache(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements()
        );
    }
}
