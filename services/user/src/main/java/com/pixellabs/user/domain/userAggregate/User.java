package com.pixellabs.user.domain.userAggregate;

import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.user.domain.userAggregate.events.UserCreatedDomainEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAggregateRoot<User> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String username;
	private String password;
	private String email;
	private String roles;
	private String firstName;
	private String lastName;

	public User(String username, String password, String email, String roles, String firstName, String lastName) {
		super();
		this.id = UUID.randomUUID();
		
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.firstName = firstName;
		this.lastName = lastName;
		
		registerEvent(new UserCreatedDomainEvent(this.id));
	}
}
