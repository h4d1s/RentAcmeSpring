package com.pixellabs.inventory.application.useCases.models.queries.getModelList;

public record GetModelListQuery(
    String orderBy,
    String order,
    Integer page,
    Integer pageSize
) {
    public GetModelListQuery {
        orderBy = orderBy == null ? "" : orderBy;
        order = order == null ? "" : order;
        page = page != null ? Math.max(page, 1) : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}
