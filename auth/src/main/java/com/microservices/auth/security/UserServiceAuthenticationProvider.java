package com.microservices.auth.security;

import com.microservices.auth.client.UserServiceClient;
import com.microservices.auth.dto.UserDtoResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Instead of using WebClient, we inject the Feign client (UserServiceClient).
 */
@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider {

    private final UserServiceClient userClient;
    private final PasswordEncoderAdapter passwordEncoder;

    @Autowired
    public UserServiceAuthenticationProvider(UserServiceClient userClient, PasswordEncoderAdapter passwordEncoder) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        UserDtoResponse userDto;
        try {
            userDto = userClient.getUserByUsername(username);
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (userDto == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        boolean matches = passwordEncoder.matches(rawPassword, userDto.getPassword());

        if (!matches) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String roleName = userDto.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        return new UsernamePasswordAuthenticationToken(
            username,
            userDto.getPassword(),
            Collections.singletonList(authority)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
