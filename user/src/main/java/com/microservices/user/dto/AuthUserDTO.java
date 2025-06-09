package com.microservices.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import com.microservices.user.model.Role;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthUserDTO {
    private UUID        userId;
    private String      username;
    private String      email;
    private String      password;

    @JsonProperty("role")           // must match the JSON field
    private Role role;               // Role is your enum

    @JsonProperty("createdAt")
    private LocalDateTime createdAt; 
}
