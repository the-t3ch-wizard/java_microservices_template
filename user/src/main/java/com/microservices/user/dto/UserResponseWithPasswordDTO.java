package com.microservices.user.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseWithPasswordDTO {

  private UUID userId;
  private String username;
  private String email;
  private String password;

}
