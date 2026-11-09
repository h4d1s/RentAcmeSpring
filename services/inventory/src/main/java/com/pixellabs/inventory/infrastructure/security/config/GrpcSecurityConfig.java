package com.pixellabs.inventory.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;

@Configuration
public class GrpcSecurityConfig {
    @Bean
    @GlobalServerInterceptor
    AuthenticationProcessInterceptor customGrpcSecurity(GrpcSecurity grpc)
        throws Exception {
        return grpc
            .authorizeRequests(requests ->
                requests.allRequests().permitAll()
            )
            .build();
    }
}
