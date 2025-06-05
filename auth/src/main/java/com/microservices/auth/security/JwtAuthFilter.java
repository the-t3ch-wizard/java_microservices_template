package com.microservices.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

/**
 * This filter:
 *  - Reads the JWT from an HttpOnly cookie named "AuthToken"
 *  - Validates it; if valid, extracts both username + “role” → grants proper authority
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = WebUtils.getCookie(request, "AuthToken");
        if (cookie != null) {

            String token = cookie.getValue();

            if (token != null && jwtUtils.validateJwtToken(token)) {

                String username = jwtUtils.getUsernameFromJwt(token);
                String roleClaim = jwtUtils.getRoleFromJwt(token);

                if (roleClaim != null){
                    
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleClaim);

                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                            username,
                            null, 
                            List.of(authority)
                        );
    
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
