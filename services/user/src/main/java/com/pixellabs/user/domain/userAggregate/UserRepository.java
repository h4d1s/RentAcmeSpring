package com.pixellabs.user.domain.userAggregate;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByExternalId(String externalId);
	boolean existsByIdAndExternalId(UUID id, String externalId);
}
