package com.microservices.auth.controller;

import com.microservices.auth.client.UserServiceClient;
import com.microservices.auth.dto.SignInRequest;
import com.microservices.auth.dto.SignUpRequest;
import com.microservices.auth.dto.UserDtoResponse;
import com.microservices.auth.dto.JwtResponse;
import com.microservices.auth.security.JwtUtils;
import com.microservices.auth.security.PasswordEncoderAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceClient userClient;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoderAdapter passwordEncoder; 

    @Autowired
    public AuthController(UserServiceClient userClient, AuthenticationManager authManager, JwtUtils jwtUtils, PasswordEncoderAdapter passwordEncoder) {
        this.userClient = userClient;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            String rawPwd = request.getPassword();

            String hashed = passwordEncoder.encode(rawPwd);

            request.setPassword(hashed);

            userClient.createUser(request);

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception ex) {
            return ResponseEntity
                .badRequest()
                .body("Failed to register: " + ex.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest request, HttpServletResponse response) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        );
        try {
            Authentication authResult = authManager.authenticate(authToken);
            
            UserDtoResponse userDetails = userClient.getUserByUsername(request.getUsername());
            if (userDetails == null) return ResponseEntity.status(401).body("Invalid username or password");
            
            String role = userDetails.getRole().name();

            String jwt = jwtUtils.generateJwtToken(request.getUsername(), role);

            ResponseCookie cookie = ResponseCookie.from("AuthToken", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60)        // 1h
                .secure(false)          // set true if HTTPS
                .sameSite("Strict")
                .build();
            response.setHeader("Set-Cookie", cookie.toString());
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("AuthToken", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .secure(false)
            .sameSite("Strict")
            .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok("Signed out");
    }
}
