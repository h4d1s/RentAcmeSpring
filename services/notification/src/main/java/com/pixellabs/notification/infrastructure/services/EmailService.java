package com.pixellabs.notification.infrastructure.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
	private static final String NO_REPLY_ADDRESS = "noreply@pixel-labs.net";
    private final JavaMailSender emailSender;

	public void send(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(NO_REPLY_ADDRESS);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		emailSender.send(message);
	}
}
