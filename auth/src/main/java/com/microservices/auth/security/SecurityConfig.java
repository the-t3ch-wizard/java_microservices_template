package com.microservices.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserServiceAuthenticationProvider authProvider;
    private final JwtUtils jwtUtils;

    @Autowired
    public SecurityConfig(UserServiceAuthenticationProvider authProvider, JwtUtils jwtUtils) {
        this.authProvider = authProvider;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1) Disable CSRF (we use stateless JWT + cookies)
        http.csrf().disable();

        // 2) Session is stateless
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 3) Public endpoints:
        http.authorizeHttpRequests()
            .requestMatchers("/auth/signup", "/auth/signin", "/auth/signout").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().hasAnyRole("USER", "ADMIN");

        // 4) Add our custom JWT filter BEFORE BasicAuthenticationFilter
        http.addFilterBefore(new JwtAuthFilter(jwtUtils), BasicAuthenticationFilter.class);

        // 5) Exception handling (401 for unauthorized)
        http.exceptionHandling()
            .authenticationEntryPoint(unauthorizedEntryPoint());

        return http.build();
    }

    // Returns 401 instead of redirecting to a login page
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }
}
