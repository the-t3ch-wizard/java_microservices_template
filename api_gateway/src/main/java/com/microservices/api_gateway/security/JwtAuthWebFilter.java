package com.microservices.api_gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthWebFilter implements WebFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthWebFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 1. Look for "AuthToken" cookie
        var cookie = request.getCookies().getFirst("AuthToken");
        if (cookie == null) {
            // no cookie → continue without setting authentication
            return chain.filter(exchange);
        }

        String token = cookie.getValue();
        if (token == null || !jwtUtils.validateJwtToken(token)) {
            return chain.filter(exchange);
        }

        // 2. Extract username + role
        String username = jwtUtils.getUsernameFromJwt(token);
        String roleClaim = jwtUtils.getRoleFromJwt(token);
        if (username == null || roleClaim == null) {
            return chain.filter(exchange);
        }

        // 3. Build a reactive SecurityContext with a GrantedAuthority
        var authority = new SimpleGrantedAuthority("ROLE_" + roleClaim);
        var auth = new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
        var context = new SecurityContextImpl(auth);

        // 4. Propagate the SecurityContext downstream
        return chain
            .filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }
}
