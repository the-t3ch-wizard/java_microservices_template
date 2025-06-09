package com.habit.habit_service.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class HabitRequestDTO {

  @NotBlank
  @Size(max = 100)
  private String name;

  @Size(max = 500)
  private String description;

  @NotBlank
  @Size(max = 50)
  private String categoryName;

  private UUID userId;

}
