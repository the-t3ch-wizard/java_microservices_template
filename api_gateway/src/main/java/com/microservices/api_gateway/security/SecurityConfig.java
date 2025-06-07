package com.microservices.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public JwtAuthWebFilter jwtAuthWebFilter() {
        return new JwtAuthWebFilter(jwtUtils);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
          // 1. Disable CSRF if you’re not using cookies for form login
          .csrf(csrf -> csrf.disable())

          // 2. Register our custom JWT filter BEFORE authentication/authorization
          .addFilterAt(jwtAuthWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

          // 3. Configure route‐based authorization
          .authorizeExchange(exchanges -> exchanges
              .pathMatchers("/auth-service/**").permitAll()       // open endpoints for login/signup
              .pathMatchers("/user-service/**").authenticated()
              .pathMatchers("/other-service/**")
              .authenticated()
              .anyExchange().denyAll()
          )

          // 4. (Optional) If you want to support HTTP Basic on top of JWT:
          .httpBasic();

        return http.build();
    }
}
