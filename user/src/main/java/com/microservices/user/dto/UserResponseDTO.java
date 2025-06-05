package com.microservices.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservices.user.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDTO {

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private Role role;
  private LocalDateTime createdAt;

}
