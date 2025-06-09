package com.microservices.auth.controller;

import com.microservices.auth.client.UserServiceClient;
import com.microservices.auth.dto.*;
import com.microservices.auth.security.JwtUtils;
import com.microservices.auth.security.PasswordEncoderAdapter;
import com.microservices.auth.utils.SuccessResponseHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final UserServiceClient userClient;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoderAdapter passwordEncoder;

    @Autowired
    public AuthController(UserServiceClient userClient,
                          AuthenticationManager authManager,
                          JwtUtils jwtUtils,
                          PasswordEncoderAdapter passwordEncoder) {
        this.userClient = userClient;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        try {

            // Hash password before sending to user-service
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            userClient.createUser(request);
            return SuccessResponseHandler.generaResponseEntity(
                "User registered successfully",
                HttpStatus.CREATED,
                null
            );
        } catch (FeignException.Conflict ex) {
            // e.g. username/email already exists
            return generateError("Username or email already in use", HttpStatus.CONFLICT);
        } catch (FeignException ex) {
            return generateError("Failed to register user: " + ex.getMessage(),
                                 HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
        @Valid @RequestBody SignInRequest request,
        HttpServletResponse servletResponse
    ) {
        try {

            System.out.println("TEST1 "+ request.getUsername()+" - "+request.getPassword());

            // 1) Lookup user over Feign
            UserDtoResponse user = userClient.getUserByUsername(request.getUsername());

            System.out.println("USER -> "+user);

            if (user == null) {
                return generateError("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }

            System.out.println("TEST2");

            System.out.println("req -> "+request.getPassword());

            System.out.println("user -> "+user.getPassword());

            // 2) Manually verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return generateError("Invalid username or password", HttpStatus.UNAUTHORIZED);
            }

            System.out.println("username -> "+ user.getUsername());
            System.out.println("roly -> "+ user.getRole());

            // 3) Build JWT
            String jwt = jwtUtils.generateJwtToken(
                user.getUsername(),
                user.getRole().name()
            );

            // 4) Set cookie
            ResponseCookie cookie = ResponseCookie.from("AuthToken", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)      // 1h
                .secure(false)     // true if HTTPS
                .sameSite("Strict")
                .build();
            servletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // 5) Return success
            return SuccessResponseHandler.generaResponseEntity(
                "Signed in successfully",
                HttpStatus.OK,
                new JwtResponse(jwt)
            );

        } catch (FeignException.NotFound ex) {
            // in case the user-service throws 404
            return generateError("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return generateError("Authentication error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletResponse servletResponse) {
        // clear cookie
        ResponseCookie cookie = ResponseCookie.from("AuthToken", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .secure(false)
            .sameSite("Strict")
            .build();
        servletResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return SuccessResponseHandler.generaResponseEntity(
            "Signed out successfully",
            HttpStatus.OK,
            null
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(name = "AuthToken", required = false) String token) {
        System.out.println("TOKEN "+ token);
        if (token == null || token.isBlank()) {
            return error("Not authenticated", HttpStatus.UNAUTHORIZED);
        }
        
        // 1) Validate token
        if (!jwtUtils.validateJwtToken(token)) {
            return error("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
        
        // TODO
        // remove console log

        // 2) Extract claims
        String username = jwtUtils.getUsernameFromJwt(token);
        System.out.println("USERNAME "+ username);
        String role     = jwtUtils.getRoleFromJwt(token);
        System.out.println("ROLE "+ role);

        // 3) Fetch user details
        UserDtoResponse user;
        try {
            user = userClient.getUserByUsername(username);
            System.out.println("sys -> "+user);
        } catch (FeignException.NotFound nf) {
            return error("User not found", HttpStatus.UNAUTHORIZED);
        } catch (FeignException fe) {
            return error("Failed to fetch user: " + fe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 4) Build safe profile map
        Map<String, Object> profile = Map.of(
            "id",        user.getUserId(),
            "username",  user.getUsername(),
            "email",     user.getEmail(),
            "role",      role,
            "createdAt", user.getCreatedAt()
        );

        return SuccessResponseHandler.generaResponseEntity(
            "Profile fetched successfully",
            HttpStatus.OK,
            profile
        );
    }

    private ResponseEntity<Map<String, String>> error(String msg, HttpStatus status) {
        return new ResponseEntity<>(Map.of("error", msg), status);
    }

    /** Utility to build a uniform error response **/
    private ResponseEntity<?> generateError(String message, HttpStatus status) {
        return new ResponseEntity<>(
            Collections.singletonMap("error", message),
            status
        );
    }
}
