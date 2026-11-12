package com.pixellabs.user.infrastructure.caching.services;

import com.pixellabs.user.application.useCases.users.dtos.UserPageCache;
import com.pixellabs.user.application.useCases.users.queries.getUserList.GetUserListQuery;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.UserRepository;
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
public class UserCacheService {
    private final UserRepository userRepository;

    @Cacheable(
        value = "users",
        key = "'list:' + #query.page() +" +
            "':' + #query.pageSize() +" +
            "':' + #query.orderBy() +" +
            "':' + #query.order()"
    )
    @Transactional
    public UserPageCache getUsers(GetUserListQuery query) {
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
        Page<User> result = userRepository.findAll(pageable);

        return new UserPageCache(
            result.getContent(),
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
    }
}