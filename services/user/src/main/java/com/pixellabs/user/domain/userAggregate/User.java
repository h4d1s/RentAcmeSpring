package com.pixellabs.user.domain.userAggregate;

import java.util.UUID;

import com.pixellabs.user.domain.userAggregate.exceptions.UserDomainException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.user.domain.userAggregate.events.UserCreatedDomainEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "app_user")
public class User extends AbstractAggregateRoot<User> {
	@Id
	private UUID id;
	private String externalId;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private String phoneNumber;

	@Embedded
	private Address address;

	public User(
		String externalId,
		String username,
		String email,
		String firstName,
		String lastName,
		String phoneNumber) throws UserDomainException {
		super();
		this.id = UUID.randomUUID();
		this.externalId = externalId;
		updateUsername(username);
		updateEmail(email);
		updateFirstName(firstName);
		updateLastName(lastName);
		updatePhoneNumber(phoneNumber);

		registerEvent(new UserCreatedDomainEvent(this.id));
	}

	public void updateEmail(String email) throws UserDomainException {
		validateEmail(email);
		this.email = email;
	}

	public void updateUsername(String username) throws UserDomainException {
		validateUsername(username);
		this.username = username;
	}

	public void updateFirstName(String firstName) throws UserDomainException {
		validateFirstName(firstName);
		this.firstName = firstName;
	}

	public void updateLastName(String lastName) throws UserDomainException {
		validateLastName(lastName);
		this.lastName = lastName;
	}

	public void updateAddress(
		String street,
		String city,
		String postalCode,
		String country) throws UserDomainException {
		this.address = new Address(street, city, postalCode, country);
	}

	public void updatePhoneNumber(String phoneNumber) throws UserDomainException {
		validatePhoneNumber(phoneNumber);
		this.phoneNumber = phoneNumber;
	}

	private void validateEmail(String email) throws UserDomainException {
		if (email == null || email.isBlank()) {
			return;
		}

		EmailValidator validator = new EmailValidator();
		if (!validator.isValid(email, null)) {
			throw new UserDomainException("Email is not a valid email address.");
		}
	}

	private void validateUsername(String username) throws UserDomainException {
		validateNotEmpty(username, "username");
	}

	private void validateFirstName(String firstName) throws UserDomainException {
	}

	private void validateLastName(String lastName) throws UserDomainException {
	}

	private void validatePhoneNumber(String phoneNumber) throws UserDomainException {
	}

	private void validateNotEmpty(String value, String fieldName) throws UserDomainException {
		if (value == null || value.isBlank()) {
			throw new UserDomainException(fieldName + " must not be empty.");
		}
	}
}
