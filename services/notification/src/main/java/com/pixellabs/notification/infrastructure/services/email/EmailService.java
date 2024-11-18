package com.pixellabs.notification.infrastructure.services.email;

public interface EmailService {
	void send(String to, String subject, String body);
}
