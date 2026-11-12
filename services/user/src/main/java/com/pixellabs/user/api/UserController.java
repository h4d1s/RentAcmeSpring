package com.pixellabs.user.api;

import com.pixellabs.user.application.exceptions.NotFoundException;
import com.pixellabs.user.application.useCases.users.commands.deleteUser.DeleteUserCommand;
import com.pixellabs.user.application.useCases.users.commands.deleteUser.DeleteUserHandler;
import com.pixellabs.user.application.useCases.users.commands.updateUser.UpdateUserCommand;
import com.pixellabs.user.application.useCases.users.commands.updateUser.UpdateUserHandler;
import com.pixellabs.user.application.useCases.users.queries.getUser.GetUserHandler;
import com.pixellabs.user.application.useCases.users.queries.getUser.GetUserQuery;
import com.pixellabs.user.application.useCases.users.queries.getUserList.GetUserListHandler;
import com.pixellabs.user.application.useCases.users.queries.getUserList.GetUserListQuery;
import com.pixellabs.user.domain.userAggregate.User;
import com.pixellabs.user.domain.userAggregate.exceptions.UserDomainException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
	private final GetUserListHandler getUserListHandler;
	private final GetUserHandler getUserHandler;
	private final UpdateUserHandler updateUserHandler;
	private final DeleteUserHandler deleteUserHandler;

	@GetMapping
	@PreAuthorize("@userAuthorization.canViewAny(authentication)")
	public Page<User> getUsers(@Valid GetUserListQuery query) {
		return getUserListHandler.handle(query);
	}

	@GetMapping("/{id}")
	@PreAuthorize("@userAuthorization.canView(authentication, #id)")
	public ResponseEntity<User> getUser(@PathVariable("id") UUID id)
		throws NotFoundException {
		User booking = getUserHandler.handle(new GetUserQuery(id));
		return ResponseEntity.ok(booking);
	}

	@PutMapping("/{id}")
	@PreAuthorize("@userAuthorization.canUpdate(authentication, #id)")
	public ResponseEntity<User> update(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateUserCommand command) throws UserDomainException {
		User user = updateUserHandler.handle(id, command);
		return ResponseEntity.ok(user);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@userAuthorization.canDelete(authentication, #id)")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		deleteUserHandler.handle(new DeleteUserCommand(id));
		return ResponseEntity.noContent().build();
	}
}
