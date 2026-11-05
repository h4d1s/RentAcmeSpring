package com.pixellabs.inventory.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
                    response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");

                    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "Authentication is required or the access token is invalid."
                    );
                    problem.setTitle("Unauthorized");
                    problem.setType(URI.create("about:blank"));

                    objectMapper.writeValue(response.getOutputStream(), problem);
                })
                .accessDeniedHandler((request, response, exception) -> {
                    BearerTokenAccessDeniedHandler delegate = new BearerTokenAccessDeniedHandler();
                    delegate.handle(request, response, exception);

                    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

                    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        "You do not have permission to access this resource."
                    );
                    problem.setTitle("Forbidden");
                    problem.setType(URI.create("about:blank"));

                    objectMapper.writeValue(response.getOutputStream(), problem);
                })
            )
            .oauth2ResourceServer(oauth ->
                oauth.jwt(Customizer.withDefaults())
            )
            .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();

        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");

        JwtGrantedAuthoritiesConverter entitlements = new JwtGrantedAuthoritiesConverter();

        entitlements.setAuthoritiesClaimName("entitlements");
        entitlements.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new HashSet<>();

            authorities.addAll(scopes.convert(jwt));
            authorities.addAll(roles.convert(jwt));
            authorities.addAll(entitlements.convert(jwt));

            return authorities;
        });

        return converter;
    }
}