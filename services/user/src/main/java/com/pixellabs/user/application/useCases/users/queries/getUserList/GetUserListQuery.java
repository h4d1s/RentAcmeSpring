package com.pixellabs.user.application.useCases.users.queries.getUserList;

public record GetUserListQuery(
    String orderBy,
    String order,
    Integer page,
    Integer pageSize
) {
    public GetUserListQuery {
        orderBy = orderBy == null ? "" : orderBy;
        order = order == null ? "" : order;
        page = page != null ? Math.max(page, 1) : 1;
        pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}