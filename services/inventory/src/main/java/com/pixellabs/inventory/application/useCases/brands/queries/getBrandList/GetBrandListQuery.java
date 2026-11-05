package com.pixellabs.inventory.application.useCases.brands.queries.getBrandList;

public record GetBrandListQuery(
    String orderBy,
    String order,
    Integer page,
    Integer pageSize
) {
    public GetBrandListQuery {
        orderBy = orderBy == null ? "" : orderBy;
        order = order == null ? "" : order;
        page = page != null ? Math.max(page, 1) : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}
