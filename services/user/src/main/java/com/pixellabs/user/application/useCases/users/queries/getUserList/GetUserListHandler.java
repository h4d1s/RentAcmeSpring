package com.pixellabs.user.application.useCases.users.queries.getUserList;

import com.pixellabs.user.infrastructure.caching.services.UserCacheService;
import com.pixellabs.user.application.useCases.users.dtos.UserPageCache;
import com.pixellabs.user.domain.userAggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserListHandler {
    private final UserCacheService userCacheService;

    public Page<User> handle(GetUserListQuery query) {
        UserPageCache cached = userCacheService.getUsers(query);

        return new PageImpl<>(
            cached.content(),
            PageRequest.of(cached.page(), cached.size()),
            cached.totalElements()
        );
    }
}