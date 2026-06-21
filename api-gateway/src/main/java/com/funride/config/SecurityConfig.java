package com.funride.config;

import com.funride.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

/**
 * Security configuration for API Gateway.
 *
 * <p>
 * Public endpoints are accessible without authentication,
 * while all other endpoints require a valid JWT token.
 * </p>
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

/**
 * Configures Spring Security filter chain.
 */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/send-otp", "/auth/verify-otp", "/auth/refresh").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                )
                .addFilterBefore(
    jwtAuthenticationFilter,
    SecurityWebFiltersOrder.AUTHENTICATION
);

        return http.build();
    }
}
