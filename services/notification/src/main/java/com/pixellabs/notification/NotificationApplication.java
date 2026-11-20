package com.pixellabs.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationPropertiesScan
@ComponentScan(basePackages = {
	"com.pixellabs.notification",
	"com.pixellabs.libs.security.grpc",
	"com.pixellabs.libs.user.grpc",
	"com.pixellabs.libs.reservation.grpc"
})
@SpringBootApplication
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}
