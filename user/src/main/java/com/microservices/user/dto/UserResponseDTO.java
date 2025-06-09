package com.microservices.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.microservices.user.model.Role;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDTO {
  private UUID        userId;
  private String      username;
  private String      email;
  private Role        role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
