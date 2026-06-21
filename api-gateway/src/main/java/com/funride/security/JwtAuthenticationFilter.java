package com.funride.security;

import com.funride.exception.ExpiredTokenException;
import com.funride.exception.InvalidTokenException;
import com.funride.util.ResponseUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Locale;


/**
 * JWT authentication filter for API Gateway.
 *
 * <p>
 * Validates incoming JWT tokens,
 * extracts user information,
 * and populates the security context.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    @PostConstruct
    public void init() {
        log.info("JWT FILTER BEAN CREATED");
    }

    /**
     * Header used to forward authenticated user phone number
     * to downstream microservices.
     */
    public static final String USER_PHONE_HEADER = "X-User-Phone";

    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;


    /**
     * Validates JWT token from Authorization header.
     *
     * @param exchange current server exchange
     * @return filter execution result
     */
    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        log.info("Processing request through JWT filter");

        String path = exchange.getRequest()
                .getPath()
                .value();

        log.info("REQUEST PATH = {}", path);

        if (
                path.equals("/auth/send-otp")
                        || path.equals("/auth/verify-otp")
                        || path.equals("/auth/refresh")
        ) {

            log.info("PUBLIC ROUTE DETECTED");

            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        try {
            if (authHeader == null) {
                return ResponseUtil.writeResponse(
                        exchange,
                        HttpStatus.UNAUTHORIZED,
                        messageSource.getMessage(
                                "error.auth.header.missing",
                                null,
                                Locale.getDefault()
                        )
                );
            }

            if (!authHeader.startsWith("Bearer ")) {

                return ResponseUtil.writeResponse(
                        exchange,
                        HttpStatus.UNAUTHORIZED,
                        messageSource.getMessage(
                                "error.auth.header.invalid",
                                null,
                                Locale.getDefault()
                        )
                );
            }

            log.info("Authorization header found");
            String token = authHeader.substring(7);

            log.info("JWT validation successful");
            jwtUtil.validateToken(token);

            String phone = jwtUtil.extractPhone(token);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            phone,
                            null,
                            AuthorityUtils.NO_AUTHORITIES
                    );

            ServerWebExchange mutatedExchange =
                    exchange.mutate()
                            .request(
                                exchange.getRequest()
                                        .mutate()
                                        .header(
                                            USER_PHONE_HEADER,
                                            phone
                                        )
                                        .build()
                            )
                        .build();

            return chain.filter(mutatedExchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder
                                    .withAuthentication(auth)
                    );

        } catch (ExpiredTokenException ex) {

            log.warn("Expired JWT token for path={}", path);
            return ResponseUtil.writeResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage(
                            "error.token.expired",
                            null,
                            Locale.getDefault()
                    )
            );

        } catch (InvalidTokenException ex) {

            log.warn("Invalid JWT token for path={}", path);
            return ResponseUtil.writeResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage(
                            "error.token.invalid",
                            null,
                            Locale.getDefault()
                    )
            );

        } catch (Exception ex) {

            log.error("Gateway authentication error", ex);
            return ResponseUtil.writeResponse(
                    exchange,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage(
                            "error.internal.server",
                            null,
                            Locale.getDefault()
                    )
            );
        }
    }
}