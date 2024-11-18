package com.pixellabs.notification.infrastructure.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	private static final String NOREPLY_ADDRESS = "noreply@pixel-labs.net";
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
    private JavaMailSender emailSender;

	public EmailServiceImpl() {
	}

	@Override
	public void send(String to, String subject, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(NOREPLY_ADDRESS);
			message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        
	        emailSender.send(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
